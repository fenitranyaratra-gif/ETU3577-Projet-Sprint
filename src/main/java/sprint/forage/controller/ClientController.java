package sprint.forage.controller;

import sprint.forage.entity.Client;
import sprint.forage.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/liste")
    public ModelAndView listeClients() {
        List<Client> clients = clientService.getAllClients();
        
        ModelAndView modelAndView = new ModelAndView("clients/liste");
        modelAndView.addObject("clients", clients);
        modelAndView.addObject("titre", "Liste des Clients");
        modelAndView.addObject("sousTitre", "Gestion des clients");
        
        return modelAndView;
    }
    
    @GetMapping("/ajouter")
    public ModelAndView showAddForm() {
        ModelAndView modelAndView = new ModelAndView("clients/ajouter");
        modelAndView.addObject("client", new Client());
        modelAndView.addObject("titre", "Ajouter un Client");
        return modelAndView;
    }
    
    @PostMapping("/ajouter")
    public ModelAndView addClient(@ModelAttribute Client client,
                                 RedirectAttributes redirectAttributes) {
        
        if (client.getContact() == null || client.getContact().trim().isEmpty()) {
            ModelAndView modelAndView = new ModelAndView("clients/ajouter");
            modelAndView.addObject("client", client);
            modelAndView.addObject("titre", "Ajouter un Client");
            modelAndView.addObject("errorMessage", "Le contact est obligatoire !");
            return modelAndView;
        }
        
        if (clientService.existsByContact(client.getContact())) {
            ModelAndView modelAndView = new ModelAndView("clients/ajouter");
            modelAndView.addObject("client", client);
            modelAndView.addObject("titre", "Ajouter un Client");
            modelAndView.addObject("errorMessage", "Un client avec ce contact existe déjà !");
            return modelAndView;
        }
        
        if (client.getNom() == null || client.getNom().trim().isEmpty()) {
            ModelAndView modelAndView = new ModelAndView("clients/ajouter");
            modelAndView.addObject("client", client);
            modelAndView.addObject("titre", "Ajouter un Client");
            modelAndView.addObject("errorMessage", "Le nom est obligatoire !");
            return modelAndView;
        }
        
        try {
            clientService.createClient(client);
            redirectAttributes.addFlashAttribute("successMessage", "Client ajouté avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'ajout du client : " + e.getMessage());
        }
        
        return new ModelAndView("redirect:/clients/liste");
    }
    
    @GetMapping("/modifier/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        Client client = clientService.getClientById(id).orElse(null);
        
        if (client == null) {
            return new ModelAndView("redirect:/clients/liste");
        }
        
        ModelAndView modelAndView = new ModelAndView("clients/modifier");
        modelAndView.addObject("client", client);
        modelAndView.addObject("titre", "Modifier le Client");
        return modelAndView;
    }
    
    @PostMapping("/modifier/{id}")
    public ModelAndView updateClient(@PathVariable Long id,
                                    @ModelAttribute Client client,
                                    RedirectAttributes redirectAttributes) {
        
        Client existingClient = clientService.getClientById(id).orElse(null);
        
        if (existingClient == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Client non trouvé !");
            return new ModelAndView("redirect:/clients/liste");
        }
        
        if (client.getNom() == null || client.getNom().trim().isEmpty()) {
            ModelAndView modelAndView = new ModelAndView("clients/modifier");
            modelAndView.addObject("client", existingClient);
            modelAndView.addObject("titre", "Modifier le Client");
            modelAndView.addObject("errorMessage", "Le nom est obligatoire !");
            return modelAndView;
        }
        
        if (client.getContact() == null || client.getContact().trim().isEmpty()) {
            ModelAndView modelAndView = new ModelAndView("clients/modifier");
            modelAndView.addObject("client", existingClient);
            modelAndView.addObject("titre", "Modifier le Client");
            modelAndView.addObject("errorMessage", "Le contact est obligatoire !");
            return modelAndView;
        }
        
        
        existingClient.setNom(client.getNom());
        existingClient.setContact(client.getContact());
        
        try {
            clientService.updateClient(id, existingClient);
            redirectAttributes.addFlashAttribute("successMessage", "Client modifié avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la modification : " + e.getMessage());
        }
        
        return new ModelAndView("redirect:/clients/liste");
    }
    
    @GetMapping("/supprimer/{id}")
    public ModelAndView deleteClient(@PathVariable Long id,
                                    RedirectAttributes redirectAttributes) {
        
        Client client = clientService.getClientById(id).orElse(null);
        
        if (client == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Client non trouvé !");
        } else {
            try {
                clientService.deleteClient(id);
                redirectAttributes.addFlashAttribute("successMessage", "Client supprimé avec succès !");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Impossible de supprimer le client : " + e.getMessage());
            }
        }
        
        return new ModelAndView("redirect:/clients/liste");
    }
    
    @GetMapping("/details/{id}")
    public ModelAndView showDetails(@PathVariable Long id) {
        Client client = clientService.getClientByIdWithDemandes(id).orElse(null);
        
        if (client == null) {
            return new ModelAndView("redirect:/clients/liste");
        }
        
        ModelAndView modelAndView = new ModelAndView("clients/details");
        modelAndView.addObject("client", client);
        modelAndView.addObject("titre", "Détails du Client");
        modelAndView.addObject("demandes", client.getDemandes());
        
        return modelAndView;
    }
}