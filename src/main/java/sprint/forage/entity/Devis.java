package sprint.forage.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "devis")
public class Devis {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddevis")
    private Long idDevis;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idtypedevis", nullable = false)
    private TypeDevis typeDevis;
    
    @Column(name = "date", nullable = false)
    private LocalDate date;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iddemande", nullable = false)
    private Demande demande;
    
    @OneToMany(mappedBy = "devis", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetailsDevis> detailsDevis;
    
    // Constructeurs
    public Devis() {
        this.date = LocalDate.now();
    }
    
    public Devis(TypeDevis typeDevis, Demande demande) {
        this.typeDevis = typeDevis;
        this.demande = demande;
        this.date = LocalDate.now();
    }
    
    // Getters et Setters
    public Long getIdDevis() {
        return idDevis;
    }
    
    public void setIdDevis(Long idDevis) {
        this.idDevis = idDevis;
    }
    
    public TypeDevis getTypeDevis() {
        return typeDevis;
    }
    
    public void setTypeDevis(TypeDevis typeDevis) {
        this.typeDevis = typeDevis;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public Demande getDemande() {
        return demande;
    }
    
    public void setDemande(Demande demande) {
        this.demande = demande;
    }
    
    public List<DetailsDevis> getDetailsDevis() {
        return detailsDevis;
    }
    
    public void setDetailsDevis(List<DetailsDevis> detailsDevis) {
        this.detailsDevis = detailsDevis;
    }
    public BigDecimal getMontantTotalDdevis() {
    BigDecimal result = BigDecimal.ZERO;
    for (DetailsDevis d : detailsDevis) {
        if (d.getMontant() != null) {
            result = result.add(d.getMontant());
        }
    }
    return result;
}
}