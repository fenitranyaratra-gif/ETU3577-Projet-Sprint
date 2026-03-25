package sprint.forage.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "client")
public class Client {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idclient")
    private Long idClient;
    
    @Column(name = "nom", nullable = false, length = 255)
    private String nom;
    
    @Column(name = "contact", nullable = false, length = 100)
    private String contact;
    
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Demande> demandes;
    
    // Constructeurs
    public Client() {}
    
    public Client(String nom, String contact) {
        this.nom = nom;
        this.contact = contact;
    }
    
    // Getters et Setters
    public Long getIdClient() {
        return idClient;
    }
    
    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getContact() {
        return contact;
    }
    
    public void setContact(String contact) {
        this.contact = contact;
    }
    
    public List<Demande> getDemandes() {
        return demandes;
    }
    
    public void setDemandes(List<Demande> demandes) {
        this.demandes = demandes;
    }
}