package sprint.forage.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "typedevis")
public class TypeDevis {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtypedevis")
    private Long idTypeDevis;
    
    @Column(name = "libelle", nullable = false, length = 100)
    private String libelle;
    
    @OneToMany(mappedBy = "typeDevis", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Devis> devis;
    
    // Constructeurs
    public TypeDevis() {}
    
    public TypeDevis(String libelle) {
        this.libelle = libelle;
    }
    
    // Getters et Setters
    public Long getIdTypeDevis() {
        return idTypeDevis;
    }
    
    public void setIdTypeDevis(Long idTypeDevis) {
        this.idTypeDevis = idTypeDevis;
    }
    
    public String getLibelle() {
        return libelle;
    }
    
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
    
    public List<Devis> getDevis() {
        return devis;
    }
    
    public void setDevis(List<Devis> devis) {
        this.devis = devis;
    }
}