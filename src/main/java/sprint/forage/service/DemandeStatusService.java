package sprint.forage.service;

import sprint.forage.entity.Demande;
import sprint.forage.entity.DemandeStatus;
import sprint.forage.entity.Status;
import sprint.forage.repository.DemandeStatusRepository;
import sprint.forage.repository.DemandeRepository;
import sprint.forage.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DemandeStatusService {
    
    @Autowired
    private DemandeStatusRepository demandeStatusRepository;
    
    @Autowired
    private DemandeRepository demandeRepository;
    
    @Autowired
    private StatusRepository statusRepository;
    
    public DemandeStatus createDemandeStatus(DemandeStatus demandeStatus, Long demandeId, Long statusId) {
        Demande demande = demandeRepository.findById(demandeId)
            .orElseThrow(() -> new RuntimeException("Demande non trouvée avec l'id: " + demandeId));
        
        Status status = statusRepository.findById(statusId)
            .orElseThrow(() -> new RuntimeException("Statut non trouvé avec l'id: " + statusId));
        
        demandeStatus.setDemande(demande);
        demandeStatus.setStatus(status);
        demandeStatus.setDate(LocalDate.now());
        
        return demandeStatusRepository.save(demandeStatus);
    }
    
    public List<DemandeStatus> getAllDemandeStatus() {
        return demandeStatusRepository.findAll();
    }
    
    public Optional<DemandeStatus> getDemandeStatusById(Long id) {
        return demandeStatusRepository.findById(id);
    }
    
    public List<DemandeStatus> getDemandeStatusByDemandeId(Long demandeId) {
        return demandeStatusRepository.findByDemandeIdOrderByDateDesc(demandeId);
    }
    
    public Optional<DemandeStatus> getLatestStatusByDemandeId(Long demandeId) {
        return demandeStatusRepository.findLatestStatusByDemandeId(demandeId);
    }
    
    public List<DemandeStatus> getDemandeStatusByStatusId(Long statusId) {
        return demandeStatusRepository.findByStatusIdStatus(statusId);
    }
    
    public List<DemandeStatus> getDemandeStatusByDateRange(LocalDate startDate, LocalDate endDate) {
        return demandeStatusRepository.findByDateBetween(startDate, endDate);
    }
    
    public DemandeStatus updateDemandeStatus(Long id, DemandeStatus demandeStatusDetails) {
        throw new UnsupportedOperationException("La mise à jour d'un statut n'est pas autorisée. Utilisez plutôt la création d'un nouveau statut.");
    }
    
    public void deleteDemandeStatus(Long id) {
        DemandeStatus demandeStatus = demandeStatusRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Statut de demande non trouvé avec l'id: " + id));
        
        demandeStatusRepository.delete(demandeStatus);
    }
    
    public DemandeStatus changeDemandeStatus(Long demandeId, Long newStatusId) {
        Demande demande = demandeRepository.findById(demandeId)
            .orElseThrow(() -> new RuntimeException("Demande non trouvée avec l'id: " + demandeId));
        
        Status newStatus = statusRepository.findById(newStatusId)
            .orElseThrow(() -> new RuntimeException("Statut non trouvé avec l'id: " + newStatusId));
        
        DemandeStatus demandeStatus = new DemandeStatus(demande, newStatus);
        demandeStatus.setDate(LocalDate.now());
        
        return demandeStatusRepository.save(demandeStatus);
    }
    
    public long countByStatusAndDate(Long statusId, LocalDate date) {
        return demandeStatusRepository.countByStatusAndDate(statusId, date);
    }
}