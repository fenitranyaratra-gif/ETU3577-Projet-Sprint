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
    
    public Client createClient(Client client) {
        if (clientRepository.existsByContact(client.getContact())) {
            throw new RuntimeException("Tsy mety");
        }
        return clientRepository.save(client);
    }
    
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
    
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }
    
    public Optional<Client> getClientByNom(String nom) {
        return clientRepository.findByNom(nom);
    }
    
    public Client updateClient(Long id, Client clientDetails) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tsy hita " + id));
        
        client.setNom(clientDetails.getNom());
        client.setContact(clientDetails.getContact());
        
        return clientRepository.save(client);
    }
    
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tsy hita " + id));
        
        if (client.getDemandes() != null && !client.getDemandes().isEmpty()) {
            throw new RuntimeException("Supprimer");
        }
        
        clientRepository.delete(client);
    }
    

}