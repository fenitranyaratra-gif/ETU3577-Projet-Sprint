package com.restservice.notecorrection.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "note", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_matiere", "id_candidat", "id_correcteur"})
})
public class Note {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_note")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "id_matiere")
    private Matiere matiere;
    
    @ManyToOne
    @JoinColumn(name = "id_candidat")
    private Candidat candidat;
    
    @ManyToOne
    @JoinColumn(name = "id_correcteur")
    private Correcteur correcteur;
    
    @Column(name = "valeur_note", precision = 5, scale = 2)
    private BigDecimal valeurNote;
    
    public Note() {}
    
    public Note(Matiere matiere, Candidat candidat, Correcteur correcteur, BigDecimal valeurNote) {
        this.matiere = matiere;
        this.candidat = candidat;
        this.correcteur = correcteur;
        this.valeurNote = valeurNote;
    }
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Matiere getMatiere() { return matiere; }
    public void setMatiere(Matiere matiere) { this.matiere = matiere; }
    
    public Candidat getCandidat() { return candidat; }
    public void setCandidat(Candidat candidat) { this.candidat = candidat; }
    
    public Correcteur getCorrecteur() { return correcteur; }
    public void setCorrecteur(Correcteur correcteur) { this.correcteur = correcteur; }
    
    public BigDecimal getValeurNote() { return valeurNote; }
    public void setValeurNote(BigDecimal valeurNote) { this.valeurNote = valeurNote; }
}