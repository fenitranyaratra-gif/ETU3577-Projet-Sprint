package com.restservice.notecorrection.controller;

import com.restservice.notecorrection.entity.Candidat;
import com.restservice.notecorrection.service.CandidatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/candidats")
public class CandidatController {

    @Autowired
    private CandidatService candidatService;

    @GetMapping("/liste")
    public ModelAndView listeCandidats() {
        List<Candidat> candidats = candidatService.getAllCandidatsOrderByNom();
        
        ModelAndView modelAndView = new ModelAndView("candidats/liste");
        modelAndView.addObject("candidats", candidats);
        modelAndView.addObject("titre", "Liste des Candidats");
        modelAndView.addObject("sousTitre", "Gestion des candidats");
        
        return modelAndView;
    }
    
    @GetMapping("/ajouter")
    public ModelAndView showAddForm() {
        ModelAndView modelAndView = new ModelAndView("candidats/ajouter");
        modelAndView.addObject("candidat", new Candidat());
        modelAndView.addObject("titre", "Ajouter un Candidat");
        return modelAndView;
    }
    
    @PostMapping("/ajouter")
    public ModelAndView addCandidat(@ModelAttribute Candidat candidat,
                                   RedirectAttributes redirectAttributes) {
        
        if (candidatService.candidatExists(candidat.getNom(), candidat.getPrenom())) {
            ModelAndView modelAndView = new ModelAndView("candidats/ajouter");
            modelAndView.addObject("candidat", candidat);
            modelAndView.addObject("titre", "Ajouter un Candidat");
            modelAndView.addObject("errorMessage", 
                "Ce candidat existe déjà !");
            return modelAndView;
        }
        
        if (candidat.getNom() == null || candidat.getNom().trim().isEmpty()) {
            ModelAndView modelAndView = new ModelAndView("candidats/ajouter");
            modelAndView.addObject("candidat", candidat);
            modelAndView.addObject("titre", "Ajouter un Candidat");
            modelAndView.addObject("errorMessage", 
                "Le nom est obligatoire !");
            return modelAndView;
        }
        
        candidatService.saveCandidat(candidat);
        
        redirectAttributes.addFlashAttribute("successMessage", 
            "Candidat ajouté avec succès !");
        
        return new ModelAndView("redirect:/candidats/liste");
    }
    
    @GetMapping("/modifier/{id}")
    public ModelAndView showEditForm(@PathVariable Integer id) {
        Candidat candidat = candidatService.getCandidatById(id);
        
        if (candidat == null) {
            return new ModelAndView("redirect:/candidats/liste");
        }
        
        ModelAndView modelAndView = new ModelAndView("candidats/modifier");
        modelAndView.addObject("candidat", candidat);
        modelAndView.addObject("titre", "Modifier le Candidat");
        return modelAndView;
    }
    
    @PostMapping("/modifier/{id}")
    public ModelAndView updateCandidat(@PathVariable Integer id,
                                      @ModelAttribute Candidat candidat,
                                      RedirectAttributes redirectAttributes) {
        
        Candidat existingCandidat = candidatService.getCandidatById(id);
        
        if (existingCandidat == null) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Candidat non trouvé !");
            return new ModelAndView("redirect:/candidats/liste");
        }
        
        existingCandidat.setNom(candidat.getNom());
        existingCandidat.setPrenom(candidat.getPrenom());
        
        if (existingCandidat.getNom() == null || existingCandidat.getNom().trim().isEmpty()) {
            ModelAndView modelAndView = new ModelAndView("candidats/modifier");
            modelAndView.addObject("candidat", existingCandidat);
            modelAndView.addObject("titre", "Modifier le Candidat");
            modelAndView.addObject("errorMessage", 
                "Le nom est obligatoire !");
            return modelAndView;
        }
        
        candidatService.saveCandidat(existingCandidat);
        
        redirectAttributes.addFlashAttribute("successMessage", 
            "Candidat modifié avec succès !");
        
        return new ModelAndView("redirect:/candidats/liste");
    }
    
    @GetMapping("/supprimer/{id}")
    public ModelAndView deleteCandidat(@PathVariable Integer id,
                                      RedirectAttributes redirectAttributes) {
        
        Candidat candidat = candidatService.getCandidatById(id);
        
        if (candidat == null) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Candidat non trouvé !");
        } else {
            candidatService.deleteCandidat(id);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Candidat supprimé avec succès !");
        }
        
        return new ModelAndView("redirect:/candidats/liste");
    }
}