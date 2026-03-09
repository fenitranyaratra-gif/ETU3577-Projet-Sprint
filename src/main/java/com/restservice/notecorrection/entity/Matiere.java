package com.restservice.notecorrection.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "matiere")
public class Matiere {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_matiere")
    private Integer id;
    
    @Column(name = "nom", length = 100, nullable = false)
    private String nom;
    
    @Column(name = "coeff")
    private Integer coefficient = 1;
    
    public Matiere() {}
    
    public Matiere(String nom, Integer coefficient) {
        this.nom = nom;
        this.coefficient = coefficient;
    }
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public Integer getCoefficient() { return coefficient; }
    public void setCoefficient(Integer coefficient) { this.coefficient = coefficient; }
}