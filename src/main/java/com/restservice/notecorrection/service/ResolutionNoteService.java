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
     * en appliquant les paramètres de résolution configurés
     */
    @Transactional
    public BigDecimal resoudreNote(Integer idMatiere, Integer idCandidat) {
        
        // 1. Récupérer TOUS les paramètres pour cette matière
        List<Parametre> parametres = parametreRepository.findByMatiereId(idMatiere);
        if (parametres.isEmpty()) {
            throw new RuntimeException("Aucun paramètre trouvé pour la matière ID: " + idMatiere);
        }
        
        // 2. Récupérer toutes les notes pour ce candidat et cette matière
        List<Note> notes = noteRepository.findNotesByMatiereAndCandidat(idMatiere, idCandidat);
        
        if (notes.isEmpty()) {
            throw new RuntimeException("Aucune note trouvée pour le candidat " + idCandidat + " en matière " + idMatiere);
        }
        
        // 3. Calculer la SOMME des différences entre toutes les paires de notes
        BigDecimal sommeDifferences = calculerSommeDifferences(notes);
        
        // 4. Trouver le paramètre qui correspond à cette somme
        Parametre parametreCorrespondant = trouverParametreCorrespondant(parametres, sommeDifferences);
        
        BigDecimal noteFinale;
        Resolution resolutionUtilisee;
        
        // 5. Si un paramètre correspond, appliquer sa résolution
        if (parametreCorrespondant != null) {
            noteFinale = appliquerResolution(notes, parametreCorrespondant.getResolution());
            resolutionUtilisee = parametreCorrespondant.getResolution();
            System.out.println("Paramètre correspondant trouvé: seuil=" + parametreCorrespondant.getEcartMax() 
                + ", opérateur=" + parametreCorrespondant.getOperateur().getSymbole()
                + ", résolution=" + resolutionUtilisee.getLibelleNote());
        } else {
            // Sinon, faire la moyenne des notes
            noteFinale = calculerMoyenne(notes);
            // Récupérer la résolution "Moyenne"
            resolutionUtilisee = resolutionRepository.findByLibelleNoteContainingIgnoreCase("moyenne")
                    .orElseThrow(() -> new RuntimeException("Résolution 'Moyenne' non trouvée"));
            System.out.println("Aucun paramètre correspondant, utilisation de la moyenne");
        }
        
        // 6. Sauvegarder la note finale avec l'ID de résolution
        sauvegarderNoteFinale(idMatiere, idCandidat, noteFinale, resolutionUtilisee);
        
        return noteFinale;
    }
    
    /**
     * Trouve le paramètre qui correspond à la somme des différences
     * Parcourt tous les paramètres et vérifie si la condition est remplie
     */
    private Parametre trouverParametreCorrespondant(List<Parametre> parametres, BigDecimal sommeDifferences) {
        for (Parametre parametre : parametres) {
            if (verifierCondition(sommeDifferences, parametre.getOperateur(), parametre.getEcartMax())) {
                return parametre;
            }
        }
        return null; // Aucun paramètre ne correspond
    }
    
    /**
     * Récupère tous les paramètres pour une matière
     */
    public List<Parametre> getParametresByMatiere(Integer idMatiere) {
        return parametreRepository.findByMatiereId(idMatiere);
    }
    
    /**
     * Calcule la SOMME des différences entre toutes les paires de notes
     */
    public BigDecimal calculerSommeDifferences(List<Note> notes) {
        if (notes == null || notes.size() < 2) {
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
     * Vérifie si la valeur satisfait la condition avec l'opérateur
     */
    public boolean verifierCondition(BigDecimal valeur, Operateur operateur, BigDecimal seuil) {
        switch (operateur.getSymbole()) {
            case ">":
                return valeur.compareTo(seuil) > 0;
            case ">=":
                return valeur.compareTo(seuil) >= 0;
            case "<":
                return valeur.compareTo(seuil) < 0;
            case "<=":
                return valeur.compareTo(seuil) <= 0;
            case "=":
                return valeur.compareTo(seuil) == 0;
            default:
                return false;
        }
    }
    
    /**
     * Sauvegarde la note finale avec la résolution utilisée
     */
    @Transactional
    public NoteFinale sauvegarderNoteFinale(Integer idMatiere, Integer idCandidat, 
                                           BigDecimal note, Resolution resolution) {
        
        Matiere matiere = matiereRepository.findById(idMatiere)
                .orElseThrow(() -> new RuntimeException("Matière non trouvée"));
        
        Candidat candidat = candidatRepository.findById(idCandidat)
                .orElseThrow(() -> new RuntimeException("Candidat non trouvé"));
        
        // Vérifier si une note finale existe déjà
        NoteFinale noteFinale = noteFinaleRepository
                .findByMatiereIdAndCandidatId(idMatiere, idCandidat)
                .orElse(new NoteFinale());
        
        noteFinale.setMatiere(matiere);
        noteFinale.setCandidat(candidat);
        noteFinale.setValeurNoteFinale(note);
        noteFinale.setResolutionUtilisee(resolution);
        noteFinale.setDateCalcul(LocalDateTime.now());
        
        return noteFinaleRepository.save(noteFinale);
    }
    
    /**
     * Vérifie si la somme des différences correspond à un paramètre
     */
    public Parametre trouverParametrePourSomme(Integer idMatiere, BigDecimal sommeDifferences) {
        List<Parametre> parametres = parametreRepository.findByMatiereId(idMatiere);
        return trouverParametreCorrespondant(parametres, sommeDifferences);
    }
    
    /**
     * Récupère les notes brutes avec détails des correcteurs
     */
    public List<Map<String, Object>> getNotesAvecDetails(Integer idMatiere, Integer idCandidat) {
        List<Note> notes = noteRepository.findNotesByMatiereAndCandidat(idMatiere, idCandidat);
        
        return notes.stream().map(note -> {
            Map<String, Object> detail = new HashMap<>();
            detail.put("id", note.getId());
            detail.put("valeur", note.getValeurNote());
            detail.put("correcteur", note.getCorrecteur().getNom() + " " + note.getCorrecteur().getPrenom());
            return detail;
        }).collect(Collectors.toList());
    }
    
    /**
     * Applique la résolution spécifiée sur la liste des notes
     */
    private BigDecimal appliquerResolution(List<Note> notes, Resolution resolution) {
        List<BigDecimal> valeurs = notes.stream()
                .map(Note::getValeurNote)
                .collect(Collectors.toList());
        
        String libelle = resolution.getLibelleNote().toLowerCase();
        
        if (libelle.contains("plus petit") || libelle.contains("min") || libelle.contains("petite")) {
            // Note la plus petite
            return valeurs.stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
            
        } else if (libelle.contains("plus grand") || libelle.contains("max") || libelle.contains("grand")) {
            // Note la plus grande
            return valeurs.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
            
        } else if (libelle.contains("moyenne") || libelle.contains("arithm")) {
            // Moyenne arithmétique
            return calculerMoyenne(notes);
            
        } else if (libelle.contains("troisième") || libelle.contains("3") || libelle.contains("troisieme")) {
            // Troisième correction
            return appliquerTroisiemeCorrection(notes);
            
        } else {
            // Par défaut, moyenne
            return calculerMoyenne(notes);
        }
    }
    
    /**
     * Calcule la moyenne des notes
     */
    private BigDecimal calculerMoyenne(List<Note> notes) {
        BigDecimal somme = notes.stream()
                .map(Note::getValeurNote)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return somme.divide(BigDecimal.valueOf(notes.size()), 2, RoundingMode.HALF_UP);
    }
    
    /**
     * Applique la troisième correction
     */
    private BigDecimal appliquerTroisiemeCorrection(List<Note> notes) {
        List<BigDecimal> valeurs = notes.stream()
                .map(Note::getValeurNote)
                .sorted()
                .collect(Collectors.toList());
        
        int taille = valeurs.size();
        
        if (taille == 1) {
            return valeurs.get(0);
        } else if (taille == 2) {
            return valeurs.get(0).add(valeurs.get(1))
                    .divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
        } else {
            return valeurs.get(taille / 2); // Médiane
        }
    }
}