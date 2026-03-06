package com.restservice.notecorrection.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "operateur")
public class Operateur {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_operateur")
    private Integer id;
    
    @Column(name = "symbole", length = 10)
    private String symbole;
    
    public Operateur() {}
    
    public Operateur(String symbole) {
        this.symbole = symbole;
    }
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getSymbole() { return symbole; }
    public void setSymbole(String symbole) { this.symbole = symbole; }
}