package sprint.forage.controller;

import sprint.forage.entity.Client;
import sprint.forage.entity.Demande;
import sprint.forage.entity.Status;
import sprint.forage.entity.DemandeStatus;
import sprint.forage.service.ClientService;
import sprint.forage.service.DemandeService;
import sprint.forage.service.StatusService;
import sprint.forage.service.DemandeStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/demandes")
public class DemandeController {
    private static final Logger logger = LoggerFactory.getLogger(DemandeController.class);

    @Autowired
    private DemandeService demandeService;
    
    @Autowired
    private ClientService clientService;
    
    @Autowired
    private StatusService statusService;
    
    @Autowired
    private DemandeStatusService demandeStatusService;

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
        
        // Log pour déboguer
        logger.info("=== AJOUT DEMANDE ===");
        logger.info("clientId: {}", clientId);
        logger.info("lieu: {}", lieu);
        logger.info("adresse: {}", adresse);
        logger.info("district: {}", district);
        
        // Validation du client
        if (clientId == null) {
            logger.error("clientId est null");
            ModelAndView modelAndView = new ModelAndView("demandes/ajouter");
            modelAndView.addObject("clients", clientService.getAllClients());
            modelAndView.addObject("titre", "Ajouter une Demande");
            modelAndView.addObject("errorMessage", "Veuillez sélectionner un client !");
            return modelAndView;
        }
        
        // Validation du lieu
        if (lieu == null || lieu.trim().isEmpty()) {
            logger.error("lieu est null ou vide");
            ModelAndView modelAndView = new ModelAndView("demandes/ajouter");
            modelAndView.addObject("clients", clientService.getAllClients());
            modelAndView.addObject("titre", "Ajouter une Demande");
            modelAndView.addObject("errorMessage", "Le lieu est obligatoire !");
            return modelAndView;
        }
        
        try {
            Demande demande = new Demande();
            demande.setLieu(lieu);
            demande.setAdresse(adresse);
            demande.setDistrict(district);
            
            demandeService.createDemande(demande, clientId);
            
            redirectAttributes.addFlashAttribute("successMessage", "Demande ajoutée avec succès !");
            return new ModelAndView("redirect:/demandes/liste");
            
        } catch (Exception e) {
            logger.error("Erreur lors de la création: ", e);
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
            redirectAttributes.addFlashAttribute("errorMessage", "Demande non trouvée !");
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
    
    @GetMapping("/details/{id}")
    public ModelAndView showDetails(@PathVariable Long id) {
        Demande demande = demandeService.getDemandeByIdWithStatus(id).orElse(null);
        
        if (demande == null) {
            return new ModelAndView("redirect:/demandes/liste");
        }
        
        List<DemandeStatus> historiqueStatus = demandeStatusService.getDemandeStatusByDemandeId(id);
        DemandeStatus statutActuel = demandeStatusService.getLatestStatusByDemandeId(id).orElse(null);
        
        ModelAndView modelAndView = new ModelAndView("demandes/details");
        modelAndView.addObject("demande", demande);
        modelAndView.addObject("statutActuel", statutActuel);
        modelAndView.addObject("historiqueStatus", historiqueStatus);
        modelAndView.addObject("titre", "Détails de la Demande");
        
        return modelAndView;
    }
    
    @GetMapping("/statuts")
public ModelAndView listeStatutsDemandes() {
    List<Demande> demandes = demandeService.getAllDemandes();
    List<Status> statusList = statusService.getAllStatus(); // Ajoutez cette ligne
    
    ModelAndView modelAndView = new ModelAndView("demandes/statuts");
    modelAndView.addObject("demandes", demandes);
    modelAndView.addObject("statusList", statusList); // Ajoutez cette ligne
    modelAndView.addObject("titre", "Gestion des statuts des demandes");
    modelAndView.addObject("sousTitre", "Suivi et modification des statuts");
    
    for (Demande demande : demandes) {
        DemandeStatus statutActuel = demandeStatusService.getLatestStatusByDemandeId(demande.getIdDemande()).orElse(null);
        modelAndView.addObject("statut_" + demande.getIdDemande(), statutActuel);
    }
    
    return modelAndView;
}
    
    @GetMapping("/historique-statuts/{id}")
    public ModelAndView historiqueStatuts(@PathVariable Long id) {
        Demande demande = demandeService.getDemandeById(id).orElse(null);
        
        if (demande == null) {
            return new ModelAndView("redirect:/demandes/statuts");
        }
        
        List<DemandeStatus> historiqueStatus = demandeStatusService.getDemandeStatusByDemandeId(id);
        
        ModelAndView modelAndView = new ModelAndView("demandes/historique-statuts");
        modelAndView.addObject("demande", demande);
        modelAndView.addObject("historiqueStatus", historiqueStatus);
        modelAndView.addObject("titre", "Historique des statuts - Demande #" + id);
        
        return modelAndView;
    }
    
    @GetMapping("/changer-statut/{id}")
    public ModelAndView showChangeStatusForm(@PathVariable Long id) {
        Demande demande = demandeService.getDemandeById(id).orElse(null);
        List<Status> statusList = statusService.getAllStatus();
        
        if (demande == null) {
            return new ModelAndView("redirect:/demandes/statuts");
        }
        
        ModelAndView modelAndView = new ModelAndView("demandes/changer-statut");
        modelAndView.addObject("demande", demande);
        modelAndView.addObject("statusList", statusList);
        modelAndView.addObject("titre", "Changer le statut - Demande #" + id);
        
        return modelAndView;
    }
    
    @PostMapping("/changer-statut/{id}")
    public ModelAndView changeStatus(@PathVariable Long id,
                                    @RequestParam Long statusId,
                                    RedirectAttributes redirectAttributes) {
        
        try {
            demandeStatusService.changeDemandeStatus(id, statusId);
            redirectAttributes.addFlashAttribute("successMessage", "Statut modifié avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors du changement de statut : " + e.getMessage());
        }
        
        return new ModelAndView("redirect:/demandes/statuts");
    }
    
    @PostMapping("/ajouter-statut")
    public ModelAndView ajouterStatut(@RequestParam Long demandeId,
                                     @RequestParam Long statusId,
                                     RedirectAttributes redirectAttributes) {
        
        try {
            demandeStatusService.changeDemandeStatus(demandeId, statusId);
            redirectAttributes.addFlashAttribute("successMessage", "Statut ajouté avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur : " + e.getMessage());
        }
        
        return new ModelAndView("redirect:/demandes/statuts");
    }
}