package sprint.forage.service;

import sprint.forage.entity.Status;
import sprint.forage.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StatusService {
    
    @Autowired
    private StatusRepository statusRepository;
    
    // Create
    public Status createStatus(Status status) {
        if (statusRepository.existsByLibelle(status.getLibelle())) {
            throw new RuntimeException("Un statut avec ce libellé existe déjà");
        }
        return statusRepository.save(status);
    }
    
    // Read - All
    public List<Status> getAllStatus() {
        return statusRepository.findAll();
    }
    
    // Read - By ID
    public Optional<Status> getStatusById(Long id) {
        return statusRepository.findById(id);
    }
    
    // Read - By ID with demande statuses
    public Optional<Status> getStatusByIdWithDemandeStatuses(Long id) {
        return statusRepository.findByIdWithDemandeStatuses(id);
    }
    
    // Read - By libelle
    public Optional<Status> getStatusByLibelle(String libelle) {
        return statusRepository.findByLibelle(libelle);
    }
    // Update
    public Status updateStatus(Long id, Status statusDetails) {
        Status status = statusRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Statut non trouvé avec l'id: " + id));
        
        status.setLibelle(statusDetails.getLibelle());
        
        return statusRepository.save(status);
    }
    
    // Delete
    public void deleteStatus(Long id) {
        Status status = statusRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Statut non trouvé avec l'id: " + id));
        
        // Vérifier si le statut est utilisé
        if (status.getDemandeStatuses() != null && !status.getDemandeStatuses().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer un statut qui est utilisé");
        }
        
        statusRepository.delete(status);
    }
}