package com.restservice.notecorrection.service;

import com.restservice.notecorrection.entity.*;
import com.restservice.notecorrection.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private MatiereRepository matiereRepository;

    @Autowired
    private CandidatRepository candidatRepository;

    @Autowired
    private CorrecteurRepository correcteurRepository;

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Note getNoteById(Integer id) {
        return noteRepository.findById(id).orElse(null);
    }

    public Note saveNote(Note note) {
        return noteRepository.save(note);
    }

    public void deleteNote(Integer id) {
        noteRepository.deleteById(id);
    }

    public boolean noteExists(Integer matiereId, Integer candidatId, Integer correcteurId) {
        return noteRepository.existsByMatiereIdAndCandidatIdAndCorrecteurId(matiereId, candidatId, correcteurId);
    }

    public List<Matiere> getAllMatieres() {
        return matiereRepository.findAll();
    }

    public List<Candidat> getAllCandidats() {
        return candidatRepository.findAll();
    }

    public List<Correcteur> getAllCorrecteurs() {
        return correcteurRepository.findAll();
    }

    public Matiere getMatiereById(Integer id) {
        return matiereRepository.findById(id).orElse(null);
    }

    public Candidat getCandidatById(Integer id) {
        return candidatRepository.findById(id).orElse(null);
    }

    public Correcteur getCorrecteurById(Integer id) {
        return correcteurRepository.findById(id).orElse(null);
    }

    public List<Note> getNotesByMatiereAndCandidat(Integer matiereId, Integer candidatId) {
        return noteRepository.findNotesByMatiereAndCandidat(matiereId, candidatId);
    }

    public boolean validateNoteValue(BigDecimal valeur) {
        return valeur != null && valeur.compareTo(BigDecimal.ZERO) >= 0 && valeur.compareTo(new BigDecimal("20")) <= 0;
    }
}