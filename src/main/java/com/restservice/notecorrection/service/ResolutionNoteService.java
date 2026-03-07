package com.restservice.notecorrection.service;

import com.restservice.notecorrection.entity.*;
import com.restservice.notecorrection.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ResolutionNoteService {

    @Autowired
    private NoteRepository noteRepository;
    
    @Autowired
    private ParametreRepository parametreRepository;
    
    @Autowired
    private ResolutionRepository resolutionRepository;
    
    @Autowired
    private NoteFinaleRepository noteFinaleRepository;
    
    @Autowired
    private MatiereRepository matiereRepository;
    
    @Autowired
    private CandidatRepository candidatRepository;
    
    /**
     * Résout la note finale pour un candidat et une matière donnés
     */
    @Transactional
    public BigDecimal resoudreNote(Integer idMatiere, Integer idCandidat) {
        
        System.out.println("\n========== DÉBUT RÉSOLUTION ==========");
        System.out.println("Matière ID: " + idMatiere + ", Candidat ID: " + idCandidat);
        
        // 1. Récupérer toutes les notes pour ce candidat et cette matière
        List<Note> notes = noteRepository.findNotesByMatiereAndCandidat(idMatiere, idCandidat);
        System.out.println("Nombre de notes trouvées: " + notes.size());
        
        if (notes.isEmpty()) {
            throw new RuntimeException("Aucune note trouvée pour le candidat " + idCandidat + " en matière " + idMatiere);
        }
        
        // Afficher les notes
        for (int i = 0; i < notes.size(); i++) {
            System.out.println("Note " + (i+1) + ": " + notes.get(i).getValeurNote() + 
                              " (Correcteur: " + notes.get(i).getCorrecteur().getNom() + ")");
        }
        
        BigDecimal noteFinale;
        Resolution resolutionUtilisee;
        
        // 2. CAS SPÉCIAL: Une seule note
        if (notes.size() == 1) {
            noteFinale = notes.get(0).getValeurNote();
            System.out.println("✅ CAS SPÉCIAL: Une seule note = " + noteFinale);
            
            // Chercher une résolution "Note unique" ou prendre "Moyenne" par défaut
            resolutionUtilisee = resolutionRepository.findByLibelleNoteContainingIgnoreCase("unique")
                    .orElseGet(() -> {
                        System.out.println("⚠️ Résolution 'unique' non trouvée, utilisation de 'moyenne'");
                        return resolutionRepository.findByLibelleNoteContainingIgnoreCase("moyenne")
                                .orElse(null);
                    });
            
            if (resolutionUtilisee == null) {
                // Si vraiment aucune résolution trouvée, en créer une temporaire
                System.out.println("⚠️ Aucune résolution trouvée, création d'une résolution par défaut");
                Resolution defaultRes = new Resolution();
                defaultRes.setLibelleNote("Note unique");
                resolutionUtilisee = defaultRes;
            }
            
        } else {
            // 3. CAS NORMAL: Plusieurs notes
            System.out.println("📊 CAS NORMAL: " + notes.size() + " notes");
            
            // Récupérer TOUS les paramètres pour cette matière
            List<Parametre> parametres = parametreRepository.findByMatiereId(idMatiere);
            System.out.println("Nombre de paramètres trouvés: " + parametres.size());
            
            if (parametres.isEmpty()) {
                throw new RuntimeException("Aucun paramètre trouvé pour la matière ID: " + idMatiere);
            }
            
            // Afficher les paramètres
            for (Parametre p : parametres) {
                System.out.println("Paramètre: seuil=" + p.getEcartMax() + 
                                 ", opérateur=" + p.getOperateur().getSymbole() + 
                                 ", résolution=" + p.getResolution().getLibelleNote());
            }
            
            // Calculer la SOMME des différences
            BigDecimal sommeDifferences = calculerSommeDifferences(notes);
            System.out.println("Somme des différences calculée: " + sommeDifferences);
            
            // Tester chaque paramètre
            Parametre parametreCorrespondant = null;
            for (Parametre p : parametres) {
                boolean condition = verifierCondition(sommeDifferences, p.getOperateur(), p.getEcartMax());
                System.out.println("Test: " + sommeDifferences + " " + p.getOperateur().getSymbole() + " " + p.getEcartMax() + " = " + condition);
                if (condition) {
                    parametreCorrespondant = p;
                    System.out.println("✅ CORRESPONDANCE: " + p.getResolution().getLibelleNote());
                    break;
                }
            }
            
            if (parametreCorrespondant != null) {
                noteFinale = appliquerResolution(notes, parametreCorrespondant.getResolution());
                resolutionUtilisee = parametreCorrespondant.getResolution();
                System.out.println("✅ Résolution appliquée: " + resolutionUtilisee.getLibelleNote() + " -> " + noteFinale);
            } else {
                noteFinale = calculerMoyenne(notes);
                resolutionUtilisee = resolutionRepository.findByLibelleNoteContainingIgnoreCase("moyenne")
                        .orElseThrow(() -> new RuntimeException("Résolution 'Moyenne' non trouvée"));
                System.out.println("❌ Aucune correspondance, moyenne -> " + noteFinale);
            }
        }
        
        // 4. AVANT SUPPRESSION: Vérifier si une note finale existe
        Optional<NoteFinale> ancienneNote = noteFinaleRepository.findByMatiereIdAndCandidatId(idMatiere, idCandidat);
        if (ancienneNote.isPresent()) {
            System.out.println("🗑️ Ancienne note finale trouvée: ID=" + ancienneNote.get().getId() + 
                             ", valeur=" + ancienneNote.get().getValeurNoteFinale() + 
                             ", résolution=" + ancienneNote.get().getResolutionUtilisee().getLibelleNote());
            
            // Supprimer
            noteFinaleRepository.delete(ancienneNote.get());
            noteFinaleRepository.flush();
            System.out.println("✅ Ancienne note finale supprimée");
        } else {
            System.out.println("ℹ️ Aucune ancienne note finale trouvée");
        }
        
        // 5. Sauvegarder la NOUVELLE note finale
        NoteFinale nouvelleNote = sauvegarderNoteFinale(idMatiere, idCandidat, noteFinale, resolutionUtilisee);
        System.out.println("✅ Nouvelle note finale sauvegardée: ID=" + nouvelleNote.getId() + 
                          ", valeur=" + nouvelleNote.getValeurNoteFinale() + 
                          ", résolution=" + nouvelleNote.getResolutionUtilisee().getLibelleNote());
        
        System.out.println("========== FIN RÉSOLUTION ==========\n");
        
        return noteFinale;
    }
    
    /**
     * Sauvegarde la note finale
     */
    @Transactional
    public NoteFinale sauvegarderNoteFinale(Integer idMatiere, Integer idCandidat, 
                                           BigDecimal note, Resolution resolution) {
        
        Matiere matiere = matiereRepository.findById(idMatiere)
                .orElseThrow(() -> new RuntimeException("Matière non trouvée avec ID: " + idMatiere));
        
        Candidat candidat = candidatRepository.findById(idCandidat)
                .orElseThrow(() -> new RuntimeException("Candidat non trouvé avec ID: " + idCandidat));
        
        NoteFinale noteFinale = new NoteFinale();
        noteFinale.setMatiere(matiere);
        noteFinale.setCandidat(candidat);
        noteFinale.setValeurNoteFinale(note);
        noteFinale.setResolutionUtilisee(resolution);
        noteFinale.setDateCalcul(LocalDateTime.now());
        
        return noteFinaleRepository.save(noteFinale);
    }
    
    /**
     * Calcule la SOMME des différences
     */
    public BigDecimal calculerSommeDifferences(List<Note> notes) {
        if (notes == null || notes.size() < 2) {
            System.out.println("⚠️ calculerSommeDifferences: pas assez de notes (" + 
                             (notes == null ? 0 : notes.size()) + ")");
            return BigDecimal.ZERO;
        }
        
        List<BigDecimal> valeurs = notes.stream()
                .map(Note::getValeurNote)
                .collect(Collectors.toList());
        
        BigDecimal somme = BigDecimal.ZERO;
        
        for (int i = 0; i < valeurs.size(); i++) {
            for (int j = i + 1; j < valeurs.size(); j++) {
                BigDecimal diff = valeurs.get(i).subtract(valeurs.get(j)).abs();
                somme = somme.add(diff);
            }
        }
        
        return somme;
    }
    
    /**
     * Vérifie la condition
     */
    public boolean verifierCondition(BigDecimal valeur, Operateur operateur, BigDecimal seuil) {
        if (valeur == null || operateur == null || seuil == null) {
            return false;
        }
        
        int comparison = valeur.compareTo(seuil);
        
        switch (operateur.getSymbole()) {
            case ">": return comparison > 0;
            case ">=": return comparison >= 0;
            case "<": return comparison < 0;
            case "<=": return comparison <= 0;
            case "=": return comparison == 0;
            default: return false;
        }
    }
    
    /**
     * Récupère tous les paramètres
     */
    public List<Parametre> getParametresByMatiere(Integer idMatiere) {
        return parametreRepository.findByMatiereId(idMatiere);
    }
    
    /**
     * Trouve le paramètre pour une somme
     */
    public Parametre trouverParametrePourSomme(Integer idMatiere, BigDecimal sommeDifferences) {
        List<Parametre> parametres = parametreRepository.findByMatiereId(idMatiere);
        for (Parametre parametre : parametres) {
            if (verifierCondition(sommeDifferences, parametre.getOperateur(), parametre.getEcartMax())) {
                return parametre;
            }
        }
        return null;
    }
    
    /**
     * Applique la résolution
     */
    private BigDecimal appliquerResolution(List<Note> notes, Resolution resolution) {
        if (notes == null || notes.isEmpty() || resolution == null) {
            return BigDecimal.ZERO;
        }
        
        List<BigDecimal> valeurs = notes.stream()
                .map(Note::getValeurNote)
                .collect(Collectors.toList());
        
        String libelle = resolution.getLibelleNote().toLowerCase();
        
        if (libelle.contains("plus petit") || libelle.contains("min") || libelle.contains("petite")) {
            return valeurs.stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        } else if (libelle.contains("plus grand") || libelle.contains("max") || libelle.contains("grand")) {
            return valeurs.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        } else {
            return calculerMoyenne(notes);
        }
    }
    
    /**
     * Calcule la moyenne
     */
    private BigDecimal calculerMoyenne(List<Note> notes) {
        if (notes == null || notes.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal somme = notes.stream()
                .map(Note::getValeurNote)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return somme.divide(BigDecimal.valueOf(notes.size()), 2, RoundingMode.HALF_UP);
    }
}