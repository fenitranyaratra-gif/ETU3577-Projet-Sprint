package sprint.forage.repository;

import sprint.forage.entity.Devis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DevisRepository extends JpaRepository<Devis, Long> {
    
    List<Devis> findByDemandeIdDemande(Long demandeId);
    
    List<Devis> findByTypeDevisIdTypeDevis(Long typeDevisId);
    
    List<Devis> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT d FROM Devis d LEFT JOIN FETCH d.detailsDevis WHERE d.idDevis = :id")
    Optional<Devis> findByIdWithDetails(@Param("id") Long id);
    
    @Query("SELECT d FROM Devis d LEFT JOIN FETCH d.demande WHERE d.idDevis = :id")
    Optional<Devis> findByIdWithDemande(@Param("id") Long id);
    
    @Query("SELECT d FROM Devis d WHERE d.demande.client.idClient = :clientId")
    List<Devis> findByClientId(@Param("clientId") Long clientId);
    
    @Query("SELECT AVG(SUM(dd.montant)) FROM Devis d JOIN d.detailsDevis dd GROUP BY d")
    Double getAverageDevisAmount();
}