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
        
        // 1. Récupérer les paramètres pour cette matière
        Parametre parametre = parametreRepository.findByMatiereId(idMatiere);
        if (parametre == null) {
            throw new RuntimeException("Aucun paramètre trouvé pour la matière ID: " + idMatiere);
        }
        
        // 2. Récupérer toutes les notes pour ce candidat et cette matière
        List<Note> notes = noteRepository.findNotesByMatiereAndCandidat(idMatiere, idCandidat);
        
        if (notes.isEmpty()) {
            throw new RuntimeException("Aucune note trouvée pour le candidat " + idCandidat + " en matière " + idMatiere);
        }
        
        // 3. Calculer l'écart entre les notes
        BigDecimal ecart = calculerEcart(notes);
        
        // 4. Récupérer l'opérateur et le seuil
        Operateur operateur = parametre.getOperateur();
        BigDecimal ecartMax = parametre.getEcartMax();
        
        // 5. Vérifier si l'écart correspond à la condition
        boolean conditionRemplie = verifierCondition(ecart, operateur, ecartMax);
        
        BigDecimal noteFinale;
        Resolution resolutionUtilisee;
        
        // 6. Appliquer la résolution appropriée
        if (conditionRemplie) {
            // Si la condition est remplie (ex: écart > 2), appliquer la résolution spécifiée
            noteFinale = appliquerResolution(notes, parametre.getResolution());
            resolutionUtilisee = parametre.getResolution();
        } else {
            // Sinon, faire la moyenne des notes
            noteFinale = calculerMoyenne(notes);
            // Récupérer la résolution "Moyenne"
            resolutionUtilisee = resolutionRepository.findByLibelleNoteContainingIgnoreCase("moyenne")
                    .orElseThrow(() -> new RuntimeException("Résolution 'Moyenne' non trouvée"));
        }
        
        // 7. Sauvegarder la note finale avec l'ID de résolution
        sauvegarderNoteFinale(idMatiere, idCandidat, noteFinale, resolutionUtilisee);
        
        return noteFinale;
    }
    
    /**
     * Vérifie si l'écart satisfait la condition avec l'opérateur
     */
    private boolean verifierCondition(BigDecimal ecart, Operateur operateur, BigDecimal seuil) {
        switch (operateur.getSymbole()) {
            case ">":
                return ecart.compareTo(seuil) > 0;
            case ">=":
                return ecart.compareTo(seuil) >= 0;
            case "<":
                return ecart.compareTo(seuil) < 0;
            case "<=":
                return ecart.compareTo(seuil) <= 0;
            case "=":
                return ecart.compareTo(seuil) == 0;
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
     * Calcule l'écart entre les notes (max - min)
     */
    public BigDecimal calculerEcart(List<Note> notes) {
        if (notes == null || notes.size() < 2) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal min = notes.stream()
                .map(Note::getValeurNote)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        
        BigDecimal max = notes.stream()
                .map(Note::getValeurNote)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        
        return max.subtract(min);
    }
    
    /**
     * Récupère les paramètres pour une matière
     */
    public Parametre getParametreByMatiere(Integer idMatiere) {
        return parametreRepository.findByMatiereId(idMatiere);
    }
    
    /**
     * Vérifie si l'écart dépasse le seuil selon les paramètres
     */
    public boolean verifierEcartAvecParametre(List<Note> notes, Parametre parametre) {
        if (notes.size() < 2 || parametre == null) {
            return false;
        }
        
        BigDecimal ecart = calculerEcart(notes);
        return verifierCondition(ecart, parametre.getOperateur(), parametre.getEcartMax());
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
    
    /**
     * Calcule la somme des différences entre toutes les paires de notes
     * (utile pour certains cas particuliers)
     */
    public BigDecimal calculerSommeDifferences(List<Note> notes) {
        if (notes.size() < 2) {
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
}