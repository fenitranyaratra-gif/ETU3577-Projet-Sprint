package sprint.forage.repository;

import sprint.forage.entity.DetailsDevis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DetailsDevisRepository extends JpaRepository<DetailsDevis, Long> {
    
    List<DetailsDevis> findByDevisIdDevis(Long devisId);
    
    @Query("SELECT dd FROM DetailsDevis dd WHERE dd.devis.idDevis = :devisId")
    List<DetailsDevis> findAllByDevisId(@Param("devisId") Long devisId);
    
    @Query("SELECT SUM(dd.montant) FROM DetailsDevis dd WHERE dd.devis.idDevis = :devisId")
    BigDecimal sumMontantByDevisId(@Param("devisId") Long devisId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM DetailsDevis dd WHERE dd.devis.idDevis = :devisId")
    void deleteByDevisId(@Param("devisId") Long devisId);
    
    boolean existsByDevisIdDevis(Long devisId);
}