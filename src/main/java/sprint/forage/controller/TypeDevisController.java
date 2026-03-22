package sprint.forage.controller;

import sprint.forage.entity.TypeDevis;
import sprint.forage.service.TypeDevisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/types-devis")
public class TypeDevisController {

    @Autowired
    private TypeDevisService typeDevisService;

    @GetMapping("/liste")
    public ModelAndView listeTypeDevis() {
        List<TypeDevis> typeDevis = typeDevisService.getAllTypeDevis();
        
        ModelAndView modelAndView = new ModelAndView("types-devis/liste");
        modelAndView.addObject("typesDevis", typeDevis);
        modelAndView.addObject("titre", "Liste des Types de Devis");
        modelAndView.addObject("sousTitre", "Gestion des types de devis");
        
        return modelAndView;
    }
    
    @GetMapping("/ajouter")
    public ModelAndView showAddForm() {
        ModelAndView modelAndView = new ModelAndView("types-devis/ajouter");
        modelAndView.addObject("typeDevis", new TypeDevis());
        modelAndView.addObject("titre", "Ajouter un Type de Devis");
        return modelAndView;
    }
    
    @PostMapping("/ajouter")
    public ModelAndView addTypeDevis(@ModelAttribute TypeDevis typeDevis,
                                    RedirectAttributes redirectAttributes) {
        
        if (typeDevis.getLibelle() == null || typeDevis.getLibelle().trim().isEmpty()) {
            ModelAndView modelAndView = new ModelAndView("types-devis/ajouter");
            modelAndView.addObject("typeDevis", typeDevis);
            modelAndView.addObject("titre", "Ajouter un Type de Devis");
            modelAndView.addObject("errorMessage", "Le libellé est obligatoire !");
            return modelAndView;
        }
        
        try {
            typeDevisService.createTypeDevis(typeDevis);
            redirectAttributes.addFlashAttribute("successMessage", "Type de devis ajouté avec succès !");
        } catch (Exception e) {
            ModelAndView modelAndView = new ModelAndView("types-devis/ajouter");
            modelAndView.addObject("typeDevis", typeDevis);
            modelAndView.addObject("titre", "Ajouter un Type de Devis");
            modelAndView.addObject("errorMessage", "Erreur : " + e.getMessage());
            return modelAndView;
        }
        
        return new ModelAndView("redirect:/types-devis/liste");
    }
    
    @GetMapping("/modifier/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        TypeDevis typeDevis = typeDevisService.getTypeDevisById(id).orElse(null);
        
        if (typeDevis == null) {
            return new ModelAndView("redirect:/types-devis/liste");
        }
        
        ModelAndView modelAndView = new ModelAndView("types-devis/modifier");
        modelAndView.addObject("typeDevis", typeDevis);
        modelAndView.addObject("titre", "Modifier le Type de Devis");
        return modelAndView;
    }
    
    @PostMapping("/modifier/{id}")
    public ModelAndView updateTypeDevis(@PathVariable Long id,
                                       @ModelAttribute TypeDevis typeDevis,
                                       RedirectAttributes redirectAttributes) {
        
        TypeDevis existingTypeDevis = typeDevisService.getTypeDevisById(id).orElse(null);
        
        if (existingTypeDevis == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Type de devis non trouvé !");
            return new ModelAndView("redirect:/types-devis/liste");
        }
        
        if (typeDevis.getLibelle() == null || typeDevis.getLibelle().trim().isEmpty()) {
            ModelAndView modelAndView = new ModelAndView("types-devis/modifier");
            modelAndView.addObject("typeDevis", existingTypeDevis);
            modelAndView.addObject("titre", "Modifier le Type de Devis");
            modelAndView.addObject("errorMessage", "Le libellé est obligatoire !");
            return modelAndView;
        }
        
        existingTypeDevis.setLibelle(typeDevis.getLibelle());
        
        try {
            typeDevisService.updateTypeDevis(id, existingTypeDevis);
            redirectAttributes.addFlashAttribute("successMessage", "Type de devis modifié avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la modification : " + e.getMessage());
        }
        
        return new ModelAndView("redirect:/types-devis/liste");
    }
    
    @GetMapping("/supprimer/{id}")
    public ModelAndView deleteTypeDevis(@PathVariable Long id,
                                       RedirectAttributes redirectAttributes) {
        
        TypeDevis typeDevis = typeDevisService.getTypeDevisById(id).orElse(null);
        
        if (typeDevis == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Type de devis non trouvé !");
        } else {
            try {
                typeDevisService.deleteTypeDevis(id);
                redirectAttributes.addFlashAttribute("successMessage", "Type de devis supprimé avec succès !");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Impossible de supprimer : " + e.getMessage());
            }
        }
        
        return new ModelAndView("redirect:/types-devis/liste");
    }
}