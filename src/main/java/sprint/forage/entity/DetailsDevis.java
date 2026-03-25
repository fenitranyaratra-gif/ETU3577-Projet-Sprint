package sprint.forage.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detailsdevis")
public class DetailsDevis {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddetailsdevis")
    private Long idDetailsDevis;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iddevis", nullable = false)
    private Devis devis;
    
    @Column(name = "libelle", nullable = false, length = 255)
    private String libelle;
    
    @Column(name = "montant", nullable = false, precision = 15, scale = 2)
    private BigDecimal montant;
    
    // Constructeurs
    public DetailsDevis() {}
    
    public DetailsDevis(Devis devis, String libelle, BigDecimal montant) {
        this.devis = devis;
        this.libelle = libelle;
        this.montant = montant;
    }
    
    // Getters et Setters
    public Long getIdDetailsDevis() {
        return idDetailsDevis;
    }
    
    public void setIdDetailsDevis(Long idDetailsDevis) {
        this.idDetailsDevis = idDetailsDevis;
    }
    
    public Devis getDevis() {
        return devis;
    }
    
    public void setDevis(Devis devis) {
        this.devis = devis;
    }
    
    public String getLibelle() {
        return libelle;
    }
    
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
    
    public BigDecimal getMontant() {
        return montant;
    }
    
    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }
}