package com.restservice.notecorrection.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "parametre")
public class Parametre {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_param")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "id_matiere")
    private Matiere matiere;
    
    @Column(name = "ecart_max", precision = 5, scale = 2)
    private BigDecimal ecartMax;
    
    @ManyToOne
    @JoinColumn(name = "id_operateur")
    private Operateur operateur;
    
    @ManyToOne
    @JoinColumn(name = "id_resolution")
    private Resolution resolution;
    
    public Parametre() {}
    
    public Parametre(Matiere matiere, BigDecimal ecartMax, Operateur operateur, Resolution resolution) {
        this.matiere = matiere;
        this.ecartMax = ecartMax;
        this.operateur = operateur;
        this.resolution = resolution;
    }
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Matiere getMatiere() { return matiere; }
    public void setMatiere(Matiere matiere) { this.matiere = matiere; }
    
    public BigDecimal getEcartMax() { return ecartMax; }
    public void setEcartMax(BigDecimal ecartMax) { this.ecartMax = ecartMax; }
    
    public Operateur getOperateur() { return operateur; }
    public void setOperateur(Operateur operateur) { this.operateur = operateur; }
    
    public Resolution getResolution() { return resolution; }
    public void setResolution(Resolution resolution) { this.resolution = resolution; }
}