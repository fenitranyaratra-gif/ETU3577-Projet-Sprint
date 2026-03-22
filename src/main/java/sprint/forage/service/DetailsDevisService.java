package sprint.forage.service;

import sprint.forage.entity.DetailsDevis;
import sprint.forage.entity.Devis;
import sprint.forage.repository.DetailsDevisRepository;
import sprint.forage.repository.DevisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DetailsDevisService {
    
    @Autowired
    private DetailsDevisRepository detailsDevisRepository;
    
    @Autowired
    private DevisRepository devisRepository;
    
    // Create
    public DetailsDevis createDetailsDevis(DetailsDevis detailsDevis, Long devisId) {
        Devis devis = devisRepository.findById(devisId)
            .orElseThrow(() -> new RuntimeException("Devis non trouvé avec l'id: " + devisId));
        
        if (detailsDevis.getMontant().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Le montant ne peut pas être négatif");
        }
        
        detailsDevis.setDevis(devis);
        return detailsDevisRepository.save(detailsDevis);
    }
    
    // Create multiple details
    public List<DetailsDevis> createMultipleDetailsDevis(List<DetailsDevis> detailsDevisList, Long devisId) {
        Devis devis = devisRepository.findById(devisId)
            .orElseThrow(() -> new RuntimeException("Devis non trouvé avec l'id: " + devisId));
        
        for (DetailsDevis details : detailsDevisList) {
            if (details.getMontant().compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Le montant ne peut pas être négatif");
            }
            details.setDevis(devis);
        }
        
        return detailsDevisRepository.saveAll(detailsDevisList);
    }
    
    // Read - All
    public List<DetailsDevis> getAllDetailsDevis() {
        return detailsDevisRepository.findAll();
    }
    
    // Read - By ID
    public Optional<DetailsDevis> getDetailsDevisById(Long id) {
        return detailsDevisRepository.findById(id);
    }
    
    // Read - By devis ID
    public List<DetailsDevis> getDetailsDevisByDevisId(Long devisId) {
        return detailsDevisRepository.findByDevisIdDevis(devisId);
    }
    
    // Update
    public DetailsDevis updateDetailsDevis(Long id, DetailsDevis detailsDevisDetails) {
        DetailsDevis detailsDevis = detailsDevisRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Détail devis non trouvé avec l'id: " + id));
        
        if (detailsDevisDetails.getMontant().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Le montant ne peut pas être négatif");
        }
        
        detailsDevis.setLibelle(detailsDevisDetails.getLibelle());
        detailsDevis.setMontant(detailsDevisDetails.getMontant());
        
        return detailsDevisRepository.save(detailsDevis);
    }
    
    // Delete
    public void deleteDetailsDevis(Long id) {
        DetailsDevis detailsDevis = detailsDevisRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Détail devis non trouvé avec l'id: " + id));
        
        detailsDevisRepository.delete(detailsDevis);
    }
    
    // Delete all details by devis ID
    public void deleteDetailsDevisByDevisId(Long devisId) {
        if (detailsDevisRepository.existsByDevisIdDevis(devisId)) {
            detailsDevisRepository.deleteByDevisId(devisId);
        }
    }
    
    // Get total amount by devis ID
    public BigDecimal getTotalMontantByDevisId(Long devisId) {
        return detailsDevisRepository.sumMontantByDevisId(devisId);
    }
}