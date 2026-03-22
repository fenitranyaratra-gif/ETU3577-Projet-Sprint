package sprint.forage.controller;

import sprint.forage.entity.Devis;
import sprint.forage.entity.Demande;
import sprint.forage.entity.TypeDevis;
import sprint.forage.service.DevisService;
import sprint.forage.service.DemandeService;
import sprint.forage.service.TypeDevisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/devis")
public class DevisController {

    private static final Logger logger = LoggerFactory.getLogger(DevisController.class);

    @Autowired
    private DevisService devisService;
    
    @Autowired
    private DemandeService demandeService;
    
    @Autowired
    private TypeDevisService typeDevisService;

    @GetMapping("/liste")
    public ModelAndView listeDevis() {
        logger.info("=== AFFICHAGE LISTE DES DEVIS ===");
        
        try {
            List<Devis> devis = devisService.getAllDevis();
            logger.info("Nombre de devis trouvés: {}", devis != null ? devis.size() : 0);
            
            ModelAndView modelAndView = new ModelAndView("devis/liste");
            modelAndView.addObject("devis", devis);
            modelAndView.addObject("titre", "Liste des Devis");
            modelAndView.addObject("sousTitre", "Gestion des devis");
            
            return modelAndView;
            
        } catch (Exception e) {
            logger.error("Erreur lors de l'affichage de la liste des devis: ", e);
            ModelAndView modelAndView = new ModelAndView("devis/liste");
            modelAndView.addObject("errorMessage", "Erreur lors du chargement des devis: " + e.getMessage());
            return modelAndView;
        }
    }
    
    @GetMapping("/ajouter")
    public ModelAndView showAddForm() {
        logger.info("=== AFFICHAGE FORMULAIRE AJOUT DEVIS ===");
        
        try {
            List<Demande> demandes = demandeService.getAllDemandes();
            logger.info("Nombre de demandes disponibles: {}", demandes != null ? demandes.size() : 0);
            
            List<TypeDevis> typesDevis = typeDevisService.getAllTypeDevis();
            logger.info("Nombre de types de devis disponibles: {}", typesDevis != null ? typesDevis.size() : 0);
            
            ModelAndView modelAndView = new ModelAndView("devis/ajouter");
            modelAndView.addObject("devis", new Devis());
            modelAndView.addObject("demandes", demandes);
            modelAndView.addObject("typesDevis", typesDevis);
            modelAndView.addObject("titre", "Ajouter un Devis");
            
            return modelAndView;
            
        } catch (Exception e) {
            logger.error("Erreur lors de l'affichage du formulaire d'ajout de devis: ", e);
            ModelAndView modelAndView = new ModelAndView("devis/ajouter");
            modelAndView.addObject("errorMessage", "Erreur: " + e.getMessage());
            return modelAndView;
        }
    }
    
    @PostMapping("/ajouter")
    public ModelAndView addDevis(@ModelAttribute Devis devis,
                                @RequestParam Long demandeId,
                                @RequestParam Long typeDevisId,
                                RedirectAttributes redirectAttributes) {
        
        logger.info("=== AJOUT DEVIS ===");
        logger.info("DemandeId reçu: {}", demandeId);
        logger.info("TypeDevisId reçu: {}", typeDevisId);
        logger.info("Devis reçu: {}", devis);
        
        // Validation
        if (demandeId == null) {
            logger.warn("DemandeId est null");
            ModelAndView modelAndView = new ModelAndView("devis/ajouter");
            modelAndView.addObject("devis", devis);
            modelAndView.addObject("demandes", demandeService.getAllDemandes());
            modelAndView.addObject("typesDevis", typeDevisService.getAllTypeDevis());
            modelAndView.addObject("titre", "Ajouter un Devis");
            modelAndView.addObject("errorMessage", "Veuillez sélectionner une demande !");
            return modelAndView;
        }
        
        if (typeDevisId == null) {
            logger.warn("TypeDevisId est null");
            ModelAndView modelAndView = new ModelAndView("devis/ajouter");
            modelAndView.addObject("devis", devis);
            modelAndView.addObject("demandes", demandeService.getAllDemandes());
            modelAndView.addObject("typesDevis", typeDevisService.getAllTypeDevis());
            modelAndView.addObject("titre", "Ajouter un Devis");
            modelAndView.addObject("errorMessage", "Veuillez sélectionner un type de devis !");
            return modelAndView;
        }
        
        try {
            logger.info("Tentative de création du devis...");
            Devis createdDevis = devisService.createDevis(devis, demandeId, typeDevisId);
            logger.info("Devis créé avec succès! ID: {}", createdDevis.getIdDevis());
            
            redirectAttributes.addFlashAttribute("successMessage", "Devis ajouté avec succès !");
            return new ModelAndView("redirect:/devis/liste");
            
        } catch (Exception e) {
            logger.error("Erreur lors de la création du devis: ", e);
            ModelAndView modelAndView = new ModelAndView("devis/ajouter");
            modelAndView.addObject("devis", devis);
            modelAndView.addObject("demandes", demandeService.getAllDemandes());
            modelAndView.addObject("typesDevis", typeDevisService.getAllTypeDevis());
            modelAndView.addObject("titre", "Ajouter un Devis");
            modelAndView.addObject("errorMessage", "Erreur : " + e.getMessage());
            return modelAndView;
        }
    }
    
    @GetMapping("/modifier/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        logger.info("=== AFFICHAGE FORMULAIRE MODIFICATION DEVIS ID: {} ===", id);
        
        try {
            Devis devis = devisService.getDevisById(id).orElse(null);
            
            if (devis == null) {
                logger.warn("Devis avec ID {} non trouvé", id);
                return new ModelAndView("redirect:/devis/liste");
            }
            
            logger.info("Devis trouvé - Demande ID: {}, TypeDevis ID: {}", 
                devis.getDemande().getIdDemande(), 
                devis.getTypeDevis().getIdTypeDevis());
            
            List<Demande> demandes = demandeService.getAllDemandes();
            List<TypeDevis> typesDevis = typeDevisService.getAllTypeDevis();
            
            ModelAndView modelAndView = new ModelAndView("devis/modifier");
            modelAndView.addObject("devis", devis);
            modelAndView.addObject("demandes", demandes);
            modelAndView.addObject("typesDevis", typesDevis);
            modelAndView.addObject("titre", "Modifier le Devis");
            
            return modelAndView;
            
        } catch (Exception e) {
            logger.error("Erreur lors de l'affichage du formulaire de modification: ", e);
            return new ModelAndView("redirect:/devis/liste");
        }
    }
    
    @PostMapping("/modifier/{id}")
    public ModelAndView updateDevis(@PathVariable Long id,
                                   @ModelAttribute Devis devis,
                                   @RequestParam Long demandeId,
                                   @RequestParam Long typeDevisId,
                                   RedirectAttributes redirectAttributes) {
        
        logger.info("=== MODIFICATION DEVIS ID: {} ===", id);
        logger.info("Nouvelle demandeId: {}", demandeId);
        logger.info("Nouveau typeDevisId: {}", typeDevisId);
        
        try {
            Devis existingDevis = devisService.getDevisById(id).orElse(null);
            
            if (existingDevis == null) {
                logger.warn("Devis avec ID {} non trouvé pour modification", id);
                redirectAttributes.addFlashAttribute("errorMessage", "Devis non trouvé !");
                return new ModelAndView("redirect:/devis/liste");
            }
            
            Demande demande = demandeService.getDemandeById(demandeId).orElse(null);
            TypeDevis typeDevis = typeDevisService.getTypeDevisById(typeDevisId).orElse(null);
            
            if (demande == null) {
                logger.warn("Demande avec ID {} non trouvée", demandeId);
                ModelAndView modelAndView = new ModelAndView("devis/modifier");
                modelAndView.addObject("devis", existingDevis);
                modelAndView.addObject("demandes", demandeService.getAllDemandes());
                modelAndView.addObject("typesDevis", typeDevisService.getAllTypeDevis());
                modelAndView.addObject("titre", "Modifier le Devis");
                modelAndView.addObject("errorMessage", "Demande non trouvée !");
                return modelAndView;
            }
            
            if (typeDevis == null) {
                logger.warn("TypeDevis avec ID {} non trouvé", typeDevisId);
                ModelAndView modelAndView = new ModelAndView("devis/modifier");
                modelAndView.addObject("devis", existingDevis);
                modelAndView.addObject("demandes", demandeService.getAllDemandes());
                modelAndView.addObject("typesDevis", typeDevisService.getAllTypeDevis());
                modelAndView.addObject("titre", "Modifier le Devis");
                modelAndView.addObject("errorMessage", "Type de devis non trouvé !");
                return modelAndView;
            }
            
            existingDevis.setDemande(demande);
            existingDevis.setTypeDevis(typeDevis);
            
            logger.info("Tentative de mise à jour du devis...");
            devisService.updateDevis(id, existingDevis);
            logger.info("Devis mis à jour avec succès");
            
            redirectAttributes.addFlashAttribute("successMessage", "Devis modifié avec succès !");
            
        } catch (Exception e) {
            logger.error("Erreur lors de la modification du devis: ", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la modification : " + e.getMessage());
        }
        
        return new ModelAndView("redirect:/devis/liste");
    }
    
    @GetMapping("/supprimer/{id}")
    public ModelAndView deleteDevis(@PathVariable Long id,
                                   RedirectAttributes redirectAttributes) {
        
        logger.info("=== SUPPRESSION DEVIS ID: {} ===", id);
        
        try {
            Devis devis = devisService.getDevisById(id).orElse(null);
            
            if (devis == null) {
                logger.warn("Devis avec ID {} non trouvé pour suppression", id);
                redirectAttributes.addFlashAttribute("errorMessage", "Devis non trouvé !");
            } else {
                logger.info("Suppression du devis...");
                devisService.deleteDevis(id);
                logger.info("Devis supprimé avec succès");
                redirectAttributes.addFlashAttribute("successMessage", "Devis supprimé avec succès !");
            }
            
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression du devis: ", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Impossible de supprimer : " + e.getMessage());
        }
        
        return new ModelAndView("redirect:/devis/liste");
    }
    
    @GetMapping("/details/{id}")
    public ModelAndView showDetails(@PathVariable Long id) {
        logger.info("=== AFFICHAGE DETAILS DEVIS ID: {} ===", id);
        
        try {
            Devis devis = devisService.getDevisByIdWithDetails(id).orElse(null);
            
            if (devis == null) {
                logger.warn("Devis avec ID {} non trouvé pour affichage des détails", id);
                return new ModelAndView("redirect:/devis/liste");
            }
            
            logger.info("Devis trouvé - Nombre de détails: {}", 
                devis.getDetailsDevis() != null ? devis.getDetailsDevis().size() : 0);
            
            ModelAndView modelAndView = new ModelAndView("devis/details");
            modelAndView.addObject("devis", devis);
            modelAndView.addObject("titre", "Détails du Devis");
            
            BigDecimal montantTotal = devisService.getMontantTotalDevis(id);
            logger.info("Montant total du devis: {}", montantTotal);
            modelAndView.addObject("montantTotal", montantTotal);
            
            return modelAndView;
            
        } catch (Exception e) {
            logger.error("Erreur lors de l'affichage des détails du devis: ", e);
            return new ModelAndView("redirect:/devis/liste");
        }
    }
}