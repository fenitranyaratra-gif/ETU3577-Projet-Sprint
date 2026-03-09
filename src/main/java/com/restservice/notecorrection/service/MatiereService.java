package com.restservice.notecorrection.service;

import com.restservice.notecorrection.entity.Matiere;
import com.restservice.notecorrection.repository.MatiereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatiereService {

    @Autowired
    private MatiereRepository matiereRepository;

    public List<Matiere> getAllMatieres() {
        return matiereRepository.findAll();
    }

    public Matiere getMatiereById(Integer id) {
        return matiereRepository.findById(id).orElse(null);
    }

    public Matiere saveMatiere(Matiere matiere) {
        return matiereRepository.save(matiere);
    }

    public void deleteMatiere(Integer id) {
        matiereRepository.deleteById(id);
    }
}