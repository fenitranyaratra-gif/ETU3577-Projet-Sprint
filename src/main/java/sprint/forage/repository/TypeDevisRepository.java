package sprint.forage.repository;

import sprint.forage.entity.TypeDevis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TypeDevisRepository extends JpaRepository<TypeDevis, Long> {
    
    Optional<TypeDevis> findByLibelle(String libelle);
    
    List<TypeDevis> findByLibelleContainingIgnoreCase(String libelle);
    
    @Query("SELECT td FROM TypeDevis td LEFT JOIN FETCH td.devis WHERE td.idTypeDevis = :id")
    Optional<TypeDevis> findByIdWithDevis(@Param("id") Long id);
    
    boolean existsByLibelle(String libelle);
}