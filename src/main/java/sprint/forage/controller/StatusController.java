package sprint.forage.controller;

import sprint.forage.entity.Status;
import sprint.forage.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/status")
public class StatusController {

    @Autowired
    private StatusService statusService;

    @GetMapping("/liste")
    public ModelAndView listeStatus() {
        List<Status> statusList = statusService.getAllStatus();
        
        ModelAndView modelAndView = new ModelAndView("status/liste");
        modelAndView.addObject("statusList", statusList);
        modelAndView.addObject("titre", "Liste des Statuts");
        modelAndView.addObject("sousTitre", "Gestion des statuts");
        
        return modelAndView;
    }
    
    @GetMapping("/ajouter")
    public ModelAndView showAddForm() {
        ModelAndView modelAndView = new ModelAndView("status/ajouter");
        modelAndView.addObject("status", new Status());
        modelAndView.addObject("titre", "Ajouter un Statut");
        return modelAndView;
    }
    
    @PostMapping("/ajouter")
    public ModelAndView addStatus(@ModelAttribute Status status,
                                 RedirectAttributes redirectAttributes) {
        
        if (status.getLibelle() == null || status.getLibelle().trim().isEmpty()) {
            ModelAndView modelAndView = new ModelAndView("status/ajouter");
            modelAndView.addObject("status", status);
            modelAndView.addObject("titre", "Ajouter un Statut");
            modelAndView.addObject("errorMessage", "Le libellé est obligatoire !");
            return modelAndView;
        }
        
        try {
            statusService.createStatus(status);
            redirectAttributes.addFlashAttribute("successMessage", "Statut ajouté avec succès !");
        } catch (Exception e) {
            ModelAndView modelAndView = new ModelAndView("status/ajouter");
            modelAndView.addObject("status", status);
            modelAndView.addObject("titre", "Ajouter un Statut");
            modelAndView.addObject("errorMessage", "Erreur : " + e.getMessage());
            return modelAndView;
        }
        
        return new ModelAndView("redirect:/status/liste");
    }
    
    @GetMapping("/modifier/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        Status status = statusService.getStatusById(id).orElse(null);
        
        if (status == null) {
            return new ModelAndView("redirect:/status/liste");
        }
        
        ModelAndView modelAndView = new ModelAndView("status/modifier");
        modelAndView.addObject("status", status);
        modelAndView.addObject("titre", "Modifier le Statut");
        return modelAndView;
    }
    
    @PostMapping("/modifier/{id}")
    public ModelAndView updateStatus(@PathVariable Long id,
                                    @ModelAttribute Status status,
                                    RedirectAttributes redirectAttributes) {
        
        Status existingStatus = statusService.getStatusById(id).orElse(null);
        
        if (existingStatus == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Statut non trouvé !");
            return new ModelAndView("redirect:/status/liste");
        }
        
        if (status.getLibelle() == null || status.getLibelle().trim().isEmpty()) {
            ModelAndView modelAndView = new ModelAndView("status/modifier");
            modelAndView.addObject("status", existingStatus);
            modelAndView.addObject("titre", "Modifier le Statut");
            modelAndView.addObject("errorMessage", "Le libellé est obligatoire !");
            return modelAndView;
        }
        
        existingStatus.setLibelle(status.getLibelle());
        
        try {
            statusService.updateStatus(id, existingStatus);
            redirectAttributes.addFlashAttribute("successMessage", "Statut modifié avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la modification : " + e.getMessage());
        }
        
        return new ModelAndView("redirect:/status/liste");
    }
    
    @GetMapping("/supprimer/{id}")
    public ModelAndView deleteStatus(@PathVariable Long id,
                                    RedirectAttributes redirectAttributes) {
        
        Status status = statusService.getStatusById(id).orElse(null);
        
        if (status == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Statut non trouvé !");
        } else {
            try {
                statusService.deleteStatus(id);
                redirectAttributes.addFlashAttribute("successMessage", "Statut supprimé avec succès !");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Impossible de supprimer : " + e.getMessage());
            }
        }
        
        return new ModelAndView("redirect:/status/liste");
    }
}