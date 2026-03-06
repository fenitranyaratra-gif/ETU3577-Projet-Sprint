package com.restservice.notecorrection.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "note_finale", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_matiere", "id_candidat"})
})
public class NoteFinale {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_note_finale")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "id_matiere", nullable = false)
    private Matiere matiere;
    
    @ManyToOne
    @JoinColumn(name = "id_candidat", nullable = false)
    private Candidat candidat;
    
    @Column(name = "valeur_note_finale", precision = 5, scale = 2)
    private BigDecimal valeurNoteFinale;
    
    @ManyToOne
    @JoinColumn(name = "id_resolution_utilisee")
    private Resolution resolutionUtilisee;
    
    @Column(name = "date_calcul")
    private LocalDateTime dateCalcul;
    
    public NoteFinale() {}
    
    public NoteFinale(Matiere matiere, Candidat candidat, BigDecimal valeurNoteFinale, Resolution resolutionUtilisee) {
        this.matiere = matiere;
        this.candidat = candidat;
        this.valeurNoteFinale = valeurNoteFinale;
        this.resolutionUtilisee = resolutionUtilisee;
        this.dateCalcul = LocalDateTime.now();
    }
    
    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Matiere getMatiere() { return matiere; }
    public void setMatiere(Matiere matiere) { this.matiere = matiere; }
    
    public Candidat getCandidat() { return candidat; }
    public void setCandidat(Candidat candidat) { this.candidat = candidat; }
    
    public BigDecimal getValeurNoteFinale() { return valeurNoteFinale; }
    public void setValeurNoteFinale(BigDecimal valeurNoteFinale) { this.valeurNoteFinale = valeurNoteFinale; }
    
    public Resolution getResolutionUtilisee() { return resolutionUtilisee; }
    public void setResolutionUtilisee(Resolution resolutionUtilisee) { this.resolutionUtilisee = resolutionUtilisee; }
    
    public LocalDateTime getDateCalcul() { return dateCalcul; }
    public void setDateCalcul(LocalDateTime dateCalcul) { this.dateCalcul = dateCalcul; }
}