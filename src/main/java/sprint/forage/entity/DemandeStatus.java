package sprint.forage.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "demandestatus")
public class DemandeStatus {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddemandestatus")
    private Long idDemandeStatus;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iddemande", nullable = false)
    private Demande demande;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idstatus", nullable = false)
    private Status status;
    
    @Column(name = "date", nullable = false)
    private LocalDate date;
    
    // Constructeurs
    public DemandeStatus() {
        this.date = LocalDate.now();
    }
    
    public DemandeStatus(Demande demande, Status status) {
        this.demande = demande;
        this.status = status;
        this.date = LocalDate.now();
    }
    
    // Getters et Setters
    public Long getIdDemandeStatus() {
        return idDemandeStatus;
    }
    
    public void setIdDemandeStatus(Long idDemandeStatus) {
        this.idDemandeStatus = idDemandeStatus;
    }
    
    public Demande getDemande() {
        return demande;
    }
    
    public void setDemande(Demande demande) {
        this.demande = demande;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
}