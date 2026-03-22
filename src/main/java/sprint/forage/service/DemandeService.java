package sprint.forage.service;

import sprint.forage.entity.Client;
import sprint.forage.entity.Demande;
import sprint.forage.entity.Status;
import sprint.forage.entity.DemandeStatus;
import sprint.forage.repository.DemandeRepository;
import sprint.forage.repository.ClientRepository;
import sprint.forage.repository.StatusRepository;
import sprint.forage.repository.DemandeStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DemandeService {
    
    @Autowired
    private DemandeRepository demandeRepository;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private StatusRepository statusRepository;
    
    @Autowired
    private DemandeStatusRepository demandeStatusRepository;
    
    // Create
    public Demande createDemande(Demande demande, Long clientId) {
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id: " + clientId));
        
        demande.setClient(client);
        demande.setDate(LocalDate.now());
        
        // S'assurer que les listes sont initialisées
        if (demande.getDemandeStatuses() == null) {
            demande.setDemandeStatuses(new ArrayList<>());
        }
        
        // Sauvegarder la demande d'abord
        Demande savedDemande = demandeRepository.save(demande);
        
        // Ajouter le statut initial "En attente"
        Status initialStatus = statusRepository.findByLibelle("En attente")
            .orElseThrow(() -> new RuntimeException("Statut 'En attente' non trouvé. Veuillez l'insérer dans la base de données."));
        
        // Créer et sauvegarder le DemandeStatus
        DemandeStatus demandeStatus = new DemandeStatus();
        demandeStatus.setDemande(savedDemande);
        demandeStatus.setStatus(initialStatus);
        demandeStatus.setDate(LocalDate.now());
        
        // Sauvegarder le DemandeStatus
        DemandeStatus savedDemandeStatus = demandeStatusRepository.save(demandeStatus);
        
        // Ajouter à la liste de la demande
        savedDemande.getDemandeStatuses().add(savedDemandeStatus);
        
        return savedDemande;
    }
    
    // Read - All
    public List<Demande> getAllDemandes() {
        return demandeRepository.findAll();
    }
    
    // Read - By ID
    public Optional<Demande> getDemandeById(Long id) {
        return demandeRepository.findById(id);
    }
    
    // Read - By ID with status
    public Optional<Demande> getDemandeByIdWithStatus(Long id) {
        return demandeRepository.findByIdWithStatus(id);
    }
    
    // Update
    public Demande updateDemande(Long id, Demande demandeDetails) {
        Demande demande = demandeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Demande non trouvée avec l'id: " + id));
        
        demande.setLieu(demandeDetails.getLieu());
        demande.setAdresse(demandeDetails.getAdresse());
        demande.setDistrict(demandeDetails.getDistrict());
        if (demandeDetails.getClient() != null) {
            demande.setClient(demandeDetails.getClient());
        }
        
        return demandeRepository.save(demande);
    }
    
    // Delete
    public void deleteDemande(Long id) {
        Demande demande = demandeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Demande non trouvée avec l'id: " + id));
        
        // Vérifier si la demande a des devis
        if (demande.getDevis() != null && !demande.getDevis().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer une demande qui a des devis");
        }
        
        demandeRepository.delete(demande);
    }
}