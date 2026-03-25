package sprint.forage.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "status")
public class Status {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idstatus")
    private Long idStatus;
    
    @Column(name = "libelle", nullable = false, length = 100, unique = true)
    private String libelle;
    
    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DemandeStatus> demandeStatuses;
    
    // Constructeurs
    public Status() {}
    
    public Status(String libelle) {
        this.libelle = libelle;
    }
    
    // Getters et Setters
    public Long getIdStatus() {
        return idStatus;
    }
    
    public void setIdStatus(Long idStatus) {
        this.idStatus = idStatus;
    }
    
    public String getLibelle() {
        return libelle;
    }
    
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
    
    public List<DemandeStatus> getDemandeStatuses() {
        return demandeStatuses;
    }
    
    public void setDemandeStatuses(List<DemandeStatus> demandeStatuses) {
        this.demandeStatuses = demandeStatuses;
    }
}