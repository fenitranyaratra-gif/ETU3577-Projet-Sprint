package sprint.forage.repository;

import sprint.forage.entity.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DemandeRepository extends JpaRepository<Demande, Long> {
    
    List<Demande> findByClientIdClient(Long clientId);
    
    List<Demande> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<Demande> findByDistrict(String district);
    
    @Query("SELECT d FROM Demande d LEFT JOIN FETCH d.devis WHERE d.idDemande = :id")
    Optional<Demande> findByIdWithDevis(@Param("id") Long id);
    
    @Query("SELECT d FROM Demande d LEFT JOIN FETCH d.demandeStatuses ds LEFT JOIN FETCH ds.status WHERE d.idDemande = :id")
    Optional<Demande> findByIdWithStatus(@Param("id") Long id);
    
    @Query("SELECT d FROM Demande d WHERE d.client.nom LIKE %:nomClient%")
    List<Demande> findByClientNomContaining(@Param("nomClient") String nomClient);
    
    @Query("SELECT COUNT(d) FROM Demande d WHERE d.date = :date")
    long countByDate(@Param("date") LocalDate date);
}