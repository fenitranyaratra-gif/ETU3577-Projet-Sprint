package sprint.forage.controller;

import sprint.forage.entity.Client;
import sprint.forage.entity.Demande;
import sprint.forage.service.ClientService;
import sprint.forage.service.DemandeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/demandes")
public class DemandeController {

    @Autowired
    private DemandeService demandeService;
    
    @Autowired
    private ClientService clientService;
    

    @GetMapping("/liste")
    public ModelAndView listeDemandes() {
        List<Demande> demandes = demandeService.getAllDemandes();
        
        ModelAndView modelAndView = new ModelAndView("demandes/liste");
        modelAndView.addObject("demandes", demandes);
        modelAndView.addObject("titre", "Liste des Demandes");
        modelAndView.addObject("sousTitre", "Gestion des demandes");
        
        return modelAndView;
    }
    
    @GetMapping("/ajouter")
    public ModelAndView showAddForm() {
        List<Client> clients = clientService.getAllClients();
        
        ModelAndView modelAndView = new ModelAndView("demandes/ajouter");
        modelAndView.addObject("demande", new Demande());
        modelAndView.addObject("clients", clients);
        modelAndView.addObject("titre", "Ajouter une Demande");
        return modelAndView;
    }
    
    @PostMapping("/ajouter")
    public ModelAndView addDemande(@RequestParam(value = "clientId", required = false) Long clientId,
                                  @RequestParam(value = "lieu", required = false) String lieu,
                                  @RequestParam(value = "adresse", required = false) String adresse,
                                  @RequestParam(value = "district", required = false) String district,
                                  RedirectAttributes redirectAttributes) {
        
        try {
            Demande demande = new Demande();
            demande.setLieu(lieu);
            demande.setAdresse(adresse);
            demande.setDistrict(district);
            
            demandeService.createDemande(demande, clientId);
            
            redirectAttributes.addFlashAttribute("successMessage", "Demande ajoutée avec succès !");
            return new ModelAndView("redirect:/demandes/liste");
            
        } catch (Exception e) {
            ModelAndView modelAndView = new ModelAndView("demandes/ajouter");
            modelAndView.addObject("clients", clientService.getAllClients());
            modelAndView.addObject("titre", "Ajouter une Demande");
            modelAndView.addObject("errorMessage", "Erreur : " + e.getMessage());
            return modelAndView;
        }
    }
    
    @GetMapping("/modifier/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        Demande demande = demandeService.getDemandeById(id).orElse(null);
        List<Client> clients = clientService.getAllClients();
        
        if (demande == null) {
            return new ModelAndView("redirect:/demandes/liste");
        }
        
        ModelAndView modelAndView = new ModelAndView("demandes/modifier");
        modelAndView.addObject("demande", demande);
        modelAndView.addObject("clients", clients);
        modelAndView.addObject("titre", "Modifier la Demande");
        return modelAndView;
    }
    
    @PostMapping("/modifier/{id}")
    public ModelAndView updateDemande(@PathVariable Long id,
                                     @RequestParam String lieu,
                                     @RequestParam(required = false) String adresse,
                                     @RequestParam(required = false) String district,
                                     @RequestParam Long clientId,
                                     RedirectAttributes redirectAttributes) {
        
        Demande existingDemande = demandeService.getDemandeById(id).orElse(null);
        
        if (existingDemande == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "DemandeTsiys !");
            return new ModelAndView("redirect:/demandes/liste");
        }
        
        if (lieu == null || lieu.trim().isEmpty()) {
            ModelAndView modelAndView = new ModelAndView("demandes/modifier");
            modelAndView.addObject("demande", existingDemande);
            modelAndView.addObject("clients", clientService.getAllClients());
            modelAndView.addObject("titre", "Modifier la Demande");
            modelAndView.addObject("errorMessage", "Le lieu est obligatoire !");
            return modelAndView;
        }
        
        Client client = clientService.getClientById(clientId).orElse(null);
        if (client == null) {
            ModelAndView modelAndView = new ModelAndView("demandes/modifier");
            modelAndView.addObject("demande", existingDemande);
            modelAndView.addObject("clients", clientService.getAllClients());
            modelAndView.addObject("titre", "Modifier la Demande");
            modelAndView.addObject("errorMessage", "Client non trouvé !");
            return modelAndView;
        }
        
        existingDemande.setLieu(lieu);
        existingDemande.setAdresse(adresse);
        existingDemande.setDistrict(district);
        existingDemande.setClient(client);
        
        try {
            demandeService.updateDemande(id, existingDemande);
            redirectAttributes.addFlashAttribute("successMessage", "Demande modifiée avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la modification : " + e.getMessage());
        }
        
        return new ModelAndView("redirect:/demandes/liste");
    }
    
    @GetMapping("/supprimer/{id}")
    public ModelAndView deleteDemande(@PathVariable Long id,
                                     RedirectAttributes redirectAttributes) {
        
        Demande demande = demandeService.getDemandeById(id).orElse(null);
        
        if (demande == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Demande non trouvée !");
        } else {
            try {
                demandeService.deleteDemande(id);
                redirectAttributes.addFlashAttribute("successMessage", "Demande supprimée avec succès !");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Impossible de supprimer : " + e.getMessage());
            }
        }
        
        return new ModelAndView("redirect:/demandes/liste");
    }
    
    
}