package com.restservice.notecorrection.service;

import com.restservice.notecorrection.entity.Candidat;
import com.restservice.notecorrection.repository.CandidatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidatService {

    @Autowired
    private CandidatRepository candidatRepository;

    public List<Candidat> getAllCandidats() {
        return candidatRepository.findAll();
    }

    public List<Candidat> getAllCandidatsOrderByNom() {
        return candidatRepository.findAllByOrderByNomAsc();
    }

    public Candidat getCandidatById(Integer id) {
        return candidatRepository.findById(id).orElse(null);
    }

    public Candidat saveCandidat(Candidat candidat) {
        return candidatRepository.save(candidat);
    }

    public void deleteCandidat(Integer id) {
        candidatRepository.deleteById(id);
    }

    public boolean candidatExists(String nom, String prenom) {
        return candidatRepository.existsByNomAndPrenom(nom, prenom);
    }
}