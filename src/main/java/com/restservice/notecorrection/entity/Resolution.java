package com.restservice.notecorrection.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "resolution")
public class Resolution {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resolution")
    private Integer id;
    
    @Column(name = "libellenote", length = 100)
    private String libelleNote;
    
    public Resolution() {}
    
    public Resolution(String libelleNote) {
        this.libelleNote = libelleNote;
    }
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getLibelleNote() { return libelleNote; }
    public void setLibelleNote(String libelleNote) { this.libelleNote = libelleNote; }
}