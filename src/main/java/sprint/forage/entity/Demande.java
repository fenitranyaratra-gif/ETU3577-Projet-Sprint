package sprint.forage.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "demande")
public class Demande {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddemande")
    private Long idDemande;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idclient", nullable = false)
    private Client client;
    
    @Column(name = "lieu", length = 255)
    private String lieu;
    
    @Column(name = "adresse", columnDefinition = "TEXT")
    private String adresse;
    
    @Column(name = "district", length = 100)
    private String district;
    
    @Column(name = "date", nullable = false)
    private LocalDate date;
    
    @OneToMany(mappedBy = "demande", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Devis> devis = new ArrayList<>();  // Initialisation
    
    @OneToMany(mappedBy = "demande", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DemandeStatus> demandeStatuses = new ArrayList<>();  // Initialisation IMPORTANT
    
    // Constructeurs
    public Demande() {
        this.date = LocalDate.now();
        this.devis = new ArrayList<>();
        this.demandeStatuses = new ArrayList<>();
    }
    
    public Demande(Client client, String lieu, String adresse, String district) {
        this.client = client;
        this.lieu = lieu;
        this.adresse = adresse;
        this.district = district;
        this.date = LocalDate.now();
        this.devis = new ArrayList<>();
        this.demandeStatuses = new ArrayList<>();
    }
    
    // Getters et Setters
    public Long getIdDemande() {
        return idDemande;
    }
    
    public void setIdDemande(Long idDemande) {
        this.idDemande = idDemande;
    }
    
    public Client getClient() {
        return client;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
    
    public String getLieu() {
        return lieu;
    }
    
    public void setLieu(String lieu) {
        this.lieu = lieu;
    }
    
    public String getAdresse() {
        return adresse;
    }
    
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    
    public String getDistrict() {
        return district;
    }
    
    public void setDistrict(String district) {
        this.district = district;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public List<Devis> getDevis() {
        return devis;
    }
    
    public void setDevis(List<Devis> devis) {
        this.devis = devis;
    }
    
    public List<DemandeStatus> getDemandeStatuses() {
        return demandeStatuses;
    }
    
    public void setDemandeStatuses(List<DemandeStatus> demandeStatuses) {
        this.demandeStatuses = demandeStatuses;
    }
}