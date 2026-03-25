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
    
    public Demande createDemande(Demande demande, Long clientId) {
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new RuntimeException("Tsita: " + clientId));
        
        demande.setClient(client);
        demande.setDate(LocalDate.now());
        
        if (demande.getDemandeStatuses() == null) {
            demande.setDemandeStatuses(new ArrayList<>());
        }
        
        Demande enregistreDemande = demandeRepository.save(demande);
        
        Status initialStatutVoalohan = statusRepository.findByLibelle("Crée")
            .orElseThrow(() -> new RuntimeException("Inseerer any @ BAse"));
        
        DemandeStatus demandeStatus = new DemandeStatus();
        demandeStatus.setDemande(enregistreDemande);
        demandeStatus.setStatus(initialStatutVoalohan);
        demandeStatus.setDate(LocalDate.now());
        
        DemandeStatus enregistreDemandeStatus = demandeStatusRepository.save(demandeStatus);
        enregistreDemande.addDemandeStatus(enregistreDemandeStatus);
        
        return enregistreDemande;
    }
    
    public List<Demande> getAllDemandes() {
        return demandeRepository.findAll();
    }
    
    public Optional<Demande> getDemandeById(Long id) {
        return demandeRepository.findById(id);
    }
    

    public Demande updateDemande(Long id, Demande demandeDetails) {
        Demande demande = demandeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("TSy hita " + id));
        
        demande.setLieu(demandeDetails.getLieu());
        demande.setAdresse(demandeDetails.getAdresse());
        demande.setDistrict(demandeDetails.getDistrict());
        if (demandeDetails.getClient() != null) {
            demande.setClient(demandeDetails.getClient());
        }
        
        return demandeRepository.save(demande);
    }
    
    public void deleteDemande(Long id) {
        Demande demande = demandeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("tsy hita l'id: " + id));
        
        if (demande.getDevis() != null && !demande.getDevis().isEmpty()) {
            throw new RuntimeException("Impossible ");
        }
        
        demandeRepository.delete(demande);
    }
}