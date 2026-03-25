package sprint.forage.controller;

import sprint.forage.entity.DetailsDevis;
import sprint.forage.entity.Devis;
import sprint.forage.service.DetailsDevisService;
import sprint.forage.service.DevisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/details-devis")
public class DetailsDevisController {

    @Autowired
    private DetailsDevisService detailsDevisService;
    
    @Autowired
    private DevisService devisService;

    @GetMapping("/liste/{devisId}")
    public ModelAndView listeDetailsDevis(@PathVariable Long devisId) {
        Devis devis = devisService.getDevisById(devisId).orElse(null);
        
        if (devis == null) {
            return new ModelAndView("redirect:/devis/liste");
        }
        
        List<DetailsDevis> detailsDevis = detailsDevisService.getDetailsDevisByDevisId(devisId);
        BigDecimal montantTotal = detailsDevisService.getTotalMontantByDevisId(devisId);
        
        ModelAndView modelAndView = new ModelAndView("details-devis/liste");
        modelAndView.addObject("detailsDevis", detailsDevis);
        modelAndView.addObject("devis", devis);
        modelAndView.addObject("montantTotal", montantTotal);
        modelAndView.addObject("titre", "Détails du Devis");
        modelAndView.addObject("sousTitre", "Devis N°" + devisId);
        
        return modelAndView;
    }
    
    @GetMapping("/ajouter/{devisId}")
    public ModelAndView showAddForm(@PathVariable Long devisId) {
        Devis devis = devisService.getDevisById(devisId).orElse(null);
        
        if (devis == null) {
            return new ModelAndView("redirect:/devis/liste");
        }
        
        ModelAndView modelAndView = new ModelAndView("details-devis/ajouter");
        modelAndView.addObject("detailsDevis", new DetailsDevis());
        modelAndView.addObject("devis", devis);
        modelAndView.addObject("titre", "Ajouter un Détail au Devis");
        return modelAndView;
    }
    
    @PostMapping("/ajouter/{devisId}")
    public ModelAndView addDetailsDevis(@PathVariable Long devisId,
                                       @ModelAttribute DetailsDevis detailsDevis,
                                       RedirectAttributes redirectAttributes) {
        
        // Validation
        if (detailsDevis.getLibelle() == null || detailsDevis.getLibelle().trim().isEmpty()) {
            ModelAndView modelAndView = new ModelAndView("details-devis/ajouter");
            modelAndView.addObject("detailsDevis", detailsDevis);
            modelAndView.addObject("devis", devisService.getDevisById(devisId).orElse(null));
            modelAndView.addObject("titre", "Ajouter un Détail au Devis");
            modelAndView.addObject("errorMessage", "Le libellé est obligatoire !");
            return modelAndView;
        }
        
        if (detailsDevis.getMontant() == null || detailsDevis.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
            ModelAndView modelAndView = new ModelAndView("details-devis/ajouter");
            modelAndView.addObject("detailsDevis", detailsDevis);
            modelAndView.addObject("devis", devisService.getDevisById(devisId).orElse(null));
            modelAndView.addObject("titre", "Ajouter un Détail au Devis");
            modelAndView.addObject("errorMessage", "Le montant doit être supérieur à 0 !");
            return modelAndView;
        }
        
        try {
            detailsDevisService.createDetailsDevis(detailsDevis, devisId);
            redirectAttributes.addFlashAttribute("successMessage", "Détail ajouté avec succès !");
        } catch (Exception e) {
            ModelAndView modelAndView = new ModelAndView("details-devis/ajouter");
            modelAndView.addObject("detailsDevis", detailsDevis);
            modelAndView.addObject("devis", devisService.getDevisById(devisId).orElse(null));
            modelAndView.addObject("titre", "Ajouter un Détail au Devis");
            modelAndView.addObject("errorMessage", "Erreur : " + e.getMessage());
            return modelAndView;
        }
        
        return new ModelAndView("redirect:/details-devis/liste/" + devisId);
    }
    
    @GetMapping("/modifier/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        DetailsDevis detailsDevis = detailsDevisService.getDetailsDevisById(id).orElse(null);
        
        if (detailsDevis == null) {
            return new ModelAndView("redirect:/devis/liste");
        }
        
        ModelAndView modelAndView = new ModelAndView("details-devis/modifier");
        modelAndView.addObject("detailsDevis", detailsDevis);
        modelAndView.addObject("titre", "Modifier le Détail du Devis");
        return modelAndView;
    }
    
    @PostMapping("/modifier/{id}")
    public ModelAndView updateDetailsDevis(@PathVariable Long id,
                                          @ModelAttribute DetailsDevis detailsDevis,
                                          RedirectAttributes redirectAttributes) {
        
        DetailsDevis existingDetails = detailsDevisService.getDetailsDevisById(id).orElse(null);
        
        if (existingDetails == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Détail non trouvé !");
            return new ModelAndView("redirect:/devis/liste");
        }
        
        // Validation
        if (detailsDevis.getLibelle() == null || detailsDevis.getLibelle().trim().isEmpty()) {
            ModelAndView modelAndView = new ModelAndView("details-devis/modifier");
            modelAndView.addObject("detailsDevis", existingDetails);
            modelAndView.addObject("titre", "Modifier le Détail du Devis");
            modelAndView.addObject("errorMessage", "Le libellé est obligatoire !");
            return modelAndView;
        }
        
        if (detailsDevis.getMontant() == null || detailsDevis.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
            ModelAndView modelAndView = new ModelAndView("details-devis/modifier");
            modelAndView.addObject("detailsDevis", existingDetails);
            modelAndView.addObject("titre", "Modifier le Détail du Devis");
            modelAndView.addObject("errorMessage", "Le montant doit être supérieur à 0 !");
            return modelAndView;
        }
        
        existingDetails.setLibelle(detailsDevis.getLibelle());
        existingDetails.setMontant(detailsDevis.getMontant());
        
        try {
            detailsDevisService.updateDetailsDevis(id, existingDetails);
            redirectAttributes.addFlashAttribute("successMessage", "Détail modifié avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la modification : " + e.getMessage());
        }
        
        Long devisId = existingDetails.getDevis().getIdDevis();
        return new ModelAndView("redirect:/details-devis/liste/" + devisId);
    }
    
    @GetMapping("/supprimer/{id}")
    public ModelAndView deleteDetailsDevis(@PathVariable Long id,
                                          RedirectAttributes redirectAttributes) {
        
        DetailsDevis detailsDevis = detailsDevisService.getDetailsDevisById(id).orElse(null);
        
        if (detailsDevis == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Détail non trouvé !");
            return new ModelAndView("redirect:/devis/liste");
        }
        
        Long devisId = detailsDevis.getDevis().getIdDevis();
        
        try {
            detailsDevisService.deleteDetailsDevis(id);
            redirectAttributes.addFlashAttribute("successMessage", "Détail supprimé avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Impossible de supprimer : " + e.getMessage());
        }
        
        return new ModelAndView("redirect:/details-devis/liste/" + devisId);
    }
}