package sprint.forage.service;

import sprint.forage.entity.Devis;
import sprint.forage.entity.Status;
import sprint.forage.entity.Demande;
import sprint.forage.entity.DemandeStatus;
import sprint.forage.entity.TypeDevis;
import sprint.forage.repository.DevisRepository;
import sprint.forage.repository.StatusRepository;
import sprint.forage.repository.DemandeRepository;
import sprint.forage.repository.DemandeStatusRepository;
import sprint.forage.repository.TypeDevisRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@Transactional
public class DevisService {
    
    @Autowired
    private DevisRepository devisRepository;
    private static final Logger logger = LoggerFactory.getLogger(DevisService.class);
    @Autowired
    private DemandeRepository demandeRepository;
    
    @Autowired
    private TypeDevisRepository typeDevisRepository;
        @Autowired
    private StatusRepository statusRepository;
        @Autowired
    private DemandeStatusRepository demandeStatusRepository;
    

    public Devis createDevis(Devis devis, Long demandeId, Long typeDevisId) {
        logger.info("=== CREATION DEVIS ===");
        logger.info("DemandeId: {}", demandeId);
        logger.info("TypeDevisId: {}", typeDevisId);
        
        Demande demande = demandeRepository.findById(demandeId)
            .orElseThrow(() -> new RuntimeException("Demande non trouvée avec l'id: " + demandeId));
        
        TypeDevis typeDevis = typeDevisRepository.findById(typeDevisId)
            .orElseThrow(() -> new RuntimeException("Type de devis non trouvé avec l'id: " + typeDevisId));
        
        devis.setDemande(demande);
        devis.setTypeDevis(typeDevis);
        devis.setDate(LocalDate.now());
        
        Devis savedDevis = devisRepository.save(devis);
        logger.info("Devis sauvegardé avec ID: {}", savedDevis.getIdDevis());
        
        Long devisEnvoyeStatusId = 9L; // "Devis envoyé" 
        
        Status devisEnvoyeStatus = statusRepository.findById(devisEnvoyeStatusId)
            .orElseThrow(() -> new RuntimeException(
                "Statut avec ID " + devisEnvoyeStatusId + " non trouvé. " +
                "Veuillez vérifier que le statut 'Devis envoyé' existe avec cet ID."
            ));
        
        logger.info("Statut 'Devis envoyé' trouvé avec ID: {}", devisEnvoyeStatus.getIdStatus());
        
        DemandeStatus demandeStatus = new DemandeStatus();
        demandeStatus.setDemande(demande);
        demandeStatus.setStatus(devisEnvoyeStatus);
        demandeStatus.setDate(LocalDate.now());
        
        DemandeStatus savedDemandeStatus = demandeStatusRepository.save(demandeStatus);
        logger.info("Nouveau statut ajouté pour la demande {}: {}", demandeId, devisEnvoyeStatus.getLibelle());
        
        if (demande.getDemandeStatuses() == null) {
            demande.setDemandeStatuses(new ArrayList<>());
        }
        demande.getDemandeStatuses().add(savedDemandeStatus);
        
        return savedDevis;
    }
    
    public List<Devis> getAllDevis() {
        return devisRepository.findAll();
    }
    
    public Optional<Devis> getDevisById(Long id) {
        return devisRepository.findById(id);
    }
    
    public Optional<Devis> getDevisByIdWithDetails(Long id) {
        return devisRepository.findByIdWithDetails(id);
    }
    
    public List<Devis> getDevisByDemandeId(Long demandeId) {
        return devisRepository.findByDemandeIdDemande(demandeId);
    }
    
    public List<Devis> getDevisByTypeDevisId(Long typeDevisId) {
        return devisRepository.findByTypeDevisIdTypeDevis(typeDevisId);
    }
    
    public List<Devis> getDevisByDateRange(LocalDate startDate, LocalDate endDate) {
        return devisRepository.findByDateBetween(startDate, endDate);
    }
    
    public List<Devis> getDevisByClientId(Long clientId) {
        return devisRepository.findByClientId(clientId);
    }
    
    public Devis updateDevis(Long id, Devis devisDetails) {
        Devis devis = devisRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Devis non trouvé avec l'id: " + id));
        
        devis.setTypeDevis(devisDetails.getTypeDevis());
        devis.setDate(devisDetails.getDate());
        
        return devisRepository.save(devis);
    }
    
    public void deleteDevis(Long id) {
        Devis devis = devisRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Devis non trouvé avec l'id: " + id));
        
        if (devis.getDetailsDevis() != null && !devis.getDetailsDevis().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer un devis qui a des détails");
        }
        
        devisRepository.delete(devis);
    }
    
    public BigDecimal getMontantTotalDevis(Long devisId) {
        return devisRepository.findByIdWithDetails(devisId)
            .map(devis -> devis.getDetailsDevis().stream()
                .map(dd -> dd.getMontant())
                .reduce(BigDecimal.ZERO, BigDecimal::add))
            .orElse(BigDecimal.ZERO);
    }
    
    public Double getAverageDevisAmount() {
        return devisRepository.getAverageDevisAmount();
    }
}