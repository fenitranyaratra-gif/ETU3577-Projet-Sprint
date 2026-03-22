package sprint.forage.service;

import sprint.forage.entity.TypeDevis;
import sprint.forage.repository.TypeDevisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TypeDevisService {
    
    @Autowired
    private TypeDevisRepository typeDevisRepository;
    
    // Create
    public TypeDevis createTypeDevis(TypeDevis typeDevis) {
        if (typeDevisRepository.existsByLibelle(typeDevis.getLibelle())) {
            throw new RuntimeException("Un type de devis avec ce libellé existe déjà");
        }
        return typeDevisRepository.save(typeDevis);
    }
    
    // Read - All
    public List<TypeDevis> getAllTypeDevis() {
        return typeDevisRepository.findAll();
    }
    
    // Read - By ID
    public Optional<TypeDevis> getTypeDevisById(Long id) {
        return typeDevisRepository.findById(id);
    }
    
    // Read - By ID with devis
    public Optional<TypeDevis> getTypeDevisByIdWithDevis(Long id) {
        return typeDevisRepository.findByIdWithDevis(id);
    }
    
    // Read - By libelle
    public Optional<TypeDevis> getTypeDevisByLibelle(String libelle) {
        return typeDevisRepository.findByLibelle(libelle);
    }
    
    // Read - Search by libelle
    public List<TypeDevis> searchTypeDevisByLibelle(String libelle) {
        return typeDevisRepository.findByLibelleContainingIgnoreCase(libelle);
    }
    
    // Update
    public TypeDevis updateTypeDevis(Long id, TypeDevis typeDevisDetails) {
        TypeDevis typeDevis = typeDevisRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Type de devis non trouvé avec l'id: " + id));
        
        typeDevis.setLibelle(typeDevisDetails.getLibelle());
        
        return typeDevisRepository.save(typeDevis);
    }
    
    // Delete
    public void deleteTypeDevis(Long id) {
        TypeDevis typeDevis = typeDevisRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Type de devis non trouvé avec l'id: " + id));
        
        // Vérifier si le type de devis est utilisé
        if (typeDevis.getDevis() != null && !typeDevis.getDevis().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer un type de devis qui est utilisé");
        }
        
        typeDevisRepository.delete(typeDevis);
    }
}