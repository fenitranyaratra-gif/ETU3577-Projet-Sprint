package com.restservice.notecorrection.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "correcteur")
public class Correcteur {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_correcteur")
    private Integer id;
    
    @Column(name = "nom", length = 100)
    private String nom;
    
    @Column(name = "prenom", length = 100)
    private String prenom;
    
    public Correcteur() {}
    
    public Correcteur(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
}