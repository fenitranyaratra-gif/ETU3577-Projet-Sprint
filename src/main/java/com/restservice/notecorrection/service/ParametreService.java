package com.restservice.notecorrection.service;

import com.restservice.notecorrection.entity.*;
import com.restservice.notecorrection.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ParametreService {

    @Autowired
    private ParametreRepository parametreRepository;

    @Autowired
    private MatiereRepository matiereRepository;

    @Autowired
    private OperateurRepository operateurRepository;

    @Autowired
    private ResolutionRepository resolutionRepository;

    public List<Parametre> getAllParametres() {
        return parametreRepository.findAll();
    }

    public Parametre getParametreById(Integer id) {
        return parametreRepository.findById(id).orElse(null);
    }

    public Parametre saveParametre(Parametre parametre) {
        return parametreRepository.save(parametre);
    }

    public void deleteParametre(Integer id) {
        parametreRepository.deleteById(id);
    }

    public boolean parametreExistsForMatiere(Integer matiereId) {
        return parametreRepository.existsByMatiereId(matiereId);
    }

    public List<Matiere> getAllMatieres() {
        return matiereRepository.findAll();
    }

    public List<Operateur> getAllOperateurs() {
        return operateurRepository.findAll();
    }

    public List<Resolution> getAllResolutions() {
        return resolutionRepository.findAll();
    }

    public Matiere getMatiereById(Integer id) {
        return matiereRepository.findById(id).orElse(null);
    }

    public Operateur getOperateurById(Integer id) {
        return operateurRepository.findById(id).orElse(null);
    }

    public Resolution getResolutionById(Integer id) {
        return resolutionRepository.findById(id).orElse(null);
    }
}