package sprint.forage.service;

import sprint.forage.entity.Client;
import sprint.forage.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClientService {
    
    @Autowired
    private ClientRepository clientRepository;
    
    // Create
    public Client createClient(Client client) {
        if (clientRepository.existsByContact(client.getContact())) {
            throw new RuntimeException("Un client avec ce contact existe déjà");
        }
        return clientRepository.save(client);
    }
    
    // Read - All
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
    
    // Read - By ID
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }
    
    // Read - By ID with demandes
    public Optional<Client> getClientByIdWithDemandes(Long id) {
        return clientRepository.findByIdWithDemandes(id);
    }
    
    // Read - By nom
    public Optional<Client> getClientByNom(String nom) {
        return clientRepository.findByNom(nom);
    }
    
    // Read - Search by nom
    public List<Client> searchClientsByNom(String nom) {
        return clientRepository.findByNomContainingIgnoreCase(nom);
    }
    
    // Update
    public Client updateClient(Long id, Client clientDetails) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id: " + id));
        
        client.setNom(clientDetails.getNom());
        client.setContact(clientDetails.getContact());
        
        return clientRepository.save(client);
    }
    
    // Delete
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id: " + id));
        
        // Vérifier si le client a des demandes
        if (client.getDemandes() != null && !client.getDemandes().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer un client qui a des demandes");
        }
        
        clientRepository.delete(client);
    }
    
    // Exists by contact
    public boolean existsByContact(String contact) {
        return clientRepository.existsByContact(contact);
    }
}