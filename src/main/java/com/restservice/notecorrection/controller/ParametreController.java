package com.restservice.notecorrection.controller;

import com.restservice.notecorrection.entity.Parametre;
import com.restservice.notecorrection.service.ParametreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/parametres")
public class ParametreController {

    @Autowired
    private ParametreService parametreService;

    @GetMapping("/liste")
    public ModelAndView listeParametres() {
        List<Parametre> parametres = parametreService.getAllParametres();
        
        ModelAndView modelAndView = new ModelAndView("parametres/liste");
        modelAndView.addObject("parametres", parametres);
        modelAndView.addObject("titre", "Liste des Paramètres");
        modelAndView.addObject("sousTitre", "Gestion des paramètres de correction");
        
        return modelAndView;
    }
    
    @GetMapping("/ajouter")
    public ModelAndView showAddForm() {
        ModelAndView modelAndView = new ModelAndView("parametres/ajouter");
        modelAndView.addObject("parametre", new Parametre());
        modelAndView.addObject("matieres", parametreService.getAllMatieres());
        modelAndView.addObject("operateurs", parametreService.getAllOperateurs());
        modelAndView.addObject("resolutions", parametreService.getAllResolutions());
        modelAndView.addObject("titre", "Ajouter un Paramètre");
        return modelAndView;
    }
    
    @PostMapping("/ajouter")
    public ModelAndView addParametre(@RequestParam("matiere.id") Integer matiereId,
                                    @RequestParam("ecartMax") BigDecimal ecartMax,
                                    @RequestParam("operateur.id") Integer operateurId,
                                    @RequestParam("resolution.id") Integer resolutionId,
                                    RedirectAttributes redirectAttributes) {
        
        if (parametreService.parametreExistsForMatiere(matiereId)) {
            ModelAndView modelAndView = new ModelAndView("parametres/ajouter");
            modelAndView.addObject("matieres", parametreService.getAllMatieres());
            modelAndView.addObject("operateurs", parametreService.getAllOperateurs());
            modelAndView.addObject("resolutions", parametreService.getAllResolutions());
            modelAndView.addObject("errorMessage", 
                "Un paramètre existe déjà pour cette matière !");
            modelAndView.addObject("titre", "Ajouter un Paramètre");
            return modelAndView;
        }
        
        if (ecartMax == null || ecartMax.compareTo(BigDecimal.ZERO) <= 0) {
            ModelAndView modelAndView = new ModelAndView("parametres/ajouter");
            modelAndView.addObject("matieres", parametreService.getAllMatieres());
            modelAndView.addObject("operateurs", parametreService.getAllOperateurs());
            modelAndView.addObject("resolutions", parametreService.getAllResolutions());
            modelAndView.addObject("errorMessage", 
                "L'écart maximum doit être positif !");
            modelAndView.addObject("titre", "Ajouter un Paramètre");
            return modelAndView;
        }
        
        Parametre parametre = new Parametre();
        parametre.setMatiere(parametreService.getMatiereById(matiereId));
        parametre.setEcartMax(ecartMax);
        parametre.setOperateur(parametreService.getOperateurById(operateurId));
        parametre.setResolution(parametreService.getResolutionById(resolutionId));
        
        parametreService.saveParametre(parametre);
        
        redirectAttributes.addFlashAttribute("successMessage", 
            "Paramètre ajouté avec succès !");
        
        return new ModelAndView("redirect:/parametres/liste");
    }
    
    @GetMapping("/modifier/{id}")
    public ModelAndView showEditForm(@PathVariable Integer id) {
        Parametre parametre = parametreService.getParametreById(id);
        
        if (parametre == null) {
            return new ModelAndView("redirect:/parametres/liste");
        }
        
        ModelAndView modelAndView = new ModelAndView("parametres/modifier");
        modelAndView.addObject("parametre", parametre);
        modelAndView.addObject("matieres", parametreService.getAllMatieres());
        modelAndView.addObject("operateurs", parametreService.getAllOperateurs());
        modelAndView.addObject("resolutions", parametreService.getAllResolutions());
        modelAndView.addObject("titre", "Modifier le Paramètre");
        return modelAndView;
    }
    
    @PostMapping("/modifier/{id}")
    public ModelAndView updateParametre(@PathVariable Integer id,
                                       @RequestParam("matiere.id") Integer matiereId,
                                       @RequestParam("ecartMax") BigDecimal ecartMax,
                                       @RequestParam("operateur.id") Integer operateurId,
                                       @RequestParam("resolution.id") Integer resolutionId,
                                       RedirectAttributes redirectAttributes) {
        
        Parametre existingParametre = parametreService.getParametreById(id);
        
        if (existingParametre == null) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Paramètre non trouvé !");
            return new ModelAndView("redirect:/parametres/liste");
        }
        
        if (ecartMax == null || ecartMax.compareTo(BigDecimal.ZERO) <= 0) {
            ModelAndView modelAndView = new ModelAndView("parametres/modifier");
            modelAndView.addObject("parametre", existingParametre);
            modelAndView.addObject("matieres", parametreService.getAllMatieres());
            modelAndView.addObject("operateurs", parametreService.getAllOperateurs());
            modelAndView.addObject("resolutions", parametreService.getAllResolutions());
            modelAndView.addObject("errorMessage", 
                "L'écart maximum doit être positif !");
            modelAndView.addObject("titre", "Modifier le Paramètre");
            return modelAndView;
        }
        
        existingParametre.setMatiere(parametreService.getMatiereById(matiereId));
        existingParametre.setEcartMax(ecartMax);
        existingParametre.setOperateur(parametreService.getOperateurById(operateurId));
        existingParametre.setResolution(parametreService.getResolutionById(resolutionId));
        
        parametreService.saveParametre(existingParametre);
        
        redirectAttributes.addFlashAttribute("successMessage", 
            "Paramètre modifié avec succès !");
        
        return new ModelAndView("redirect:/parametres/liste");
    }
    
    @GetMapping("/supprimer/{id}")
    public ModelAndView deleteParametre(@PathVariable Integer id,
                                       RedirectAttributes redirectAttributes) {
        
        Parametre parametre = parametreService.getParametreById(id);
        
        if (parametre == null) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Paramètre non trouvé !");
        } else {
            parametreService.deleteParametre(id);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Paramètre supprimé avec succès !");
        }
        
        return new ModelAndView("redirect:/parametres/liste");
    }
}