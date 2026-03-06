package com.restservice.notecorrection.service;

import com.restservice.notecorrection.entity.*;
import com.restservice.notecorrection.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteFinaleService {

    @Autowired
    private NoteFinaleRepository noteFinaleRepository;
    
    @Autowired
    private MatiereRepository matiereRepository;
    
    @Autowired
    private CandidatRepository candidatRepository;
    
    @Autowired
    private ResolutionRepository resolutionRepository;
    
    /**
     * Sauvegarde ou met à jour une note finale
     */
    @Transactional
    public NoteFinale sauvegarderNoteFinale(Integer idMatiere, Integer idCandidat, 
                                           BigDecimal note, Integer idResolution) {
        
        Matiere matiere = matiereRepository.findById(idMatiere)
                .orElseThrow(() -> new RuntimeException("Matière non trouvée avec ID: " + idMatiere));
        
        Candidat candidat = candidatRepository.findById(idCandidat)
                .orElseThrow(() -> new RuntimeException("Candidat non trouvé avec ID: " + idCandidat));
        
        Resolution resolution = resolutionRepository.findById(idResolution)
                .orElseThrow(() -> new RuntimeException("Résolution non trouvée avec ID: " + idResolution));
        
        // Vérifier si une note finale existe déjà pour ce couple (matière, candidat)
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
     * Sauvegarde ou met à jour une note finale (version avec objet Resolution)
     */
    @Transactional
    public NoteFinale sauvegarderNoteFinale(Integer idMatiere, Integer idCandidat, 
                                           BigDecimal note, Resolution resolution) {
        return sauvegarderNoteFinale(idMatiere, idCandidat, note, resolution.getId());
    }
    
    /**
     * Récupère une note finale par matière et candidat
     */
    public NoteFinale getNoteFinale(Integer idMatiere, Integer idCandidat) {
        return noteFinaleRepository
                .findByMatiereIdAndCandidatId(idMatiere, idCandidat)
                .orElse(null);
    }
    
    /**
     * Récupère toutes les notes finales pour une matière
     */
    public List<NoteFinale> getNotesFinalesParMatiere(Integer idMatiere) {
        return noteFinaleRepository.findByMatiereId(idMatiere);
    }
    
    /**
     * Récupère toutes les notes finales pour un candidat
     */
    public List<NoteFinale> getNotesFinalesParCandidat(Integer idCandidat) {
        return noteFinaleRepository.findByCandidatId(idCandidat);
    }
    
    /**
     * Récupère toutes les notes finales
     */
    public List<NoteFinale> getAllNotesFinales() {
        return noteFinaleRepository.findAll();
    }
    
    /**
     * Supprime une note finale
     */
    @Transactional
    public void deleteNoteFinale(Integer id) {
        noteFinaleRepository.deleteById(id);
    }
    
    /**
     * Supprime une note finale par matière et candidat
     */
    @Transactional
    public void deleteNoteFinale(Integer idMatiere, Integer idCandidat) {
        NoteFinale noteFinale = getNoteFinale(idMatiere, idCandidat);
        if (noteFinale != null) {
            noteFinaleRepository.delete(noteFinale);
        }
    }
    
    /**
     * Vérifie si une note finale existe
     */
    public boolean existeNoteFinale(Integer idMatiere, Integer idCandidat) {
        return noteFinaleRepository.findByMatiereIdAndCandidatId(idMatiere, idCandidat).isPresent();
    }
    
    /**
     * Calcule la moyenne des notes finales d'un candidat (toutes matières confondues)
     */
    public BigDecimal calculerMoyenneGeneraleCandidat(Integer idCandidat) {
        List<NoteFinale> notes = getNotesFinalesParCandidat(idCandidat);
        
        if (notes.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal somme = BigDecimal.ZERO;
        int totalCoeff = 0;
        
        for (NoteFinale note : notes) {
            BigDecimal noteValue = note.getValeurNoteFinale();
            int coeff = note.getMatiere().getCoefficient();
            
            somme = somme.add(noteValue.multiply(BigDecimal.valueOf(coeff)));
            totalCoeff += coeff;
        }
        
        if (totalCoeff == 0) {
            return BigDecimal.ZERO;
        }
        
        return somme.divide(BigDecimal.valueOf(totalCoeff), 2, java.math.RoundingMode.HALF_UP);
    }
    
    /**
     * Récupère les statistiques des notes finales pour une matière
     */
    public java.util.Map<String, Object> getStatistiquesMatiere(Integer idMatiere) {
        List<NoteFinale> notes = getNotesFinalesParMatiere(idMatiere);
        
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        
        if (notes.isEmpty()) {
            stats.put("count", 0);
            stats.put("moyenne", BigDecimal.ZERO);
            stats.put("min", null);
            stats.put("max", null);
            return stats;
        }
        
        BigDecimal somme = BigDecimal.ZERO;
        BigDecimal min = notes.get(0).getValeurNoteFinale();
        BigDecimal max = notes.get(0).getValeurNoteFinale();
        
        for (NoteFinale note : notes) {
            BigDecimal valeur = note.getValeurNoteFinale();
            somme = somme.add(valeur);
            
            if (valeur.compareTo(min) < 0) {
                min = valeur;
            }
            if (valeur.compareTo(max) > 0) {
                max = valeur;
            }
        }
        
        stats.put("count", notes.size());
        stats.put("moyenne", somme.divide(BigDecimal.valueOf(notes.size()), 2, java.math.RoundingMode.HALF_UP));
        stats.put("min", min);
        stats.put("max", max);
        
        return stats;
    }
}