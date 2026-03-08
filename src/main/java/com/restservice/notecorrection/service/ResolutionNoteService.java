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

    @Transactional
    public BigDecimal resoudreNote(Integer idMatiere, Integer idCandidat) {
        
        List<Note> notes = noteRepository.findNotesByMatiereAndCandidat(idMatiere, idCandidat);
        
        if (notes.isEmpty()) {
            throw new RuntimeException("Aucune note trouvée pour le candidat " + idCandidat + " en matière " + idMatiere);
        }

        
        BigDecimal noteFinale;
        Resolution resolutionUtilisee;
        
        if (notes.size() == 1) {
            noteFinale = notes.get(0).getValeurNote();
            Resolution defaultRes = new Resolution();
            defaultRes.setLibelleNote("Note unique");
            resolutionUtilisee = defaultRes;
        } else {
            List<Parametre> parametres = parametreRepository.findByMatiereId(idMatiere);            
            if (parametres.isEmpty()) {
                throw new RuntimeException("Aucun paramètre trouvé pour la matière ID: " + idMatiere);
            }
            BigDecimal sommeDifferences = calculerSommeDifferences(notes);
            Parametre parametreCorrespondant = null;
            for (Parametre p : parametres) {
                boolean condition = verifierCondition(sommeDifferences, p.getOperateur(), p.getEcartMax());
                if (condition) {
                    parametreCorrespondant = p;
                    break;
                }
            }
            if (parametreCorrespondant != null) {
                noteFinale = appliquerResolution(notes, parametreCorrespondant.getResolution());
                resolutionUtilisee = parametreCorrespondant.getResolution();
            } else {
                noteFinale = calculerMoyenne(notes);
                resolutionUtilisee = resolutionRepository.findByLibelleNoteContainingIgnoreCase("moyenne")
                        .orElseThrow(() -> new RuntimeException("Résolution 'Moyenne' non trouvée"));
            }
        }
        
        Optional<NoteFinale> ancienneNote = noteFinaleRepository.findByMatiereIdAndCandidatId(idMatiere, idCandidat);
        if (ancienneNote.isPresent()) {
            noteFinaleRepository.delete(ancienneNote.get());
            noteFinaleRepository.flush();
        } else {
        }
        
        NoteFinale nouvelleNote = sauvegarderNoteFinale(idMatiere, idCandidat, noteFinale, resolutionUtilisee);
        return noteFinale;
    }
    

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

    public List<Parametre> getParametresByMatiere(Integer idMatiere) {
        return parametreRepository.findByMatiereId(idMatiere);
    }
    

    public Parametre trouverParametrePourSomme(Integer idMatiere, BigDecimal sommeDifferences) {
        List<Parametre> parametres = parametreRepository.findByMatiereId(idMatiere);
        for (Parametre parametre : parametres) {
            if (verifierCondition(sommeDifferences, parametre.getOperateur(), parametre.getEcartMax())) {
                return parametre;
            }
        }
        return null;
    }
    

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