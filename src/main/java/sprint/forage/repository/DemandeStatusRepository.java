package sprint.forage.repository;

import sprint.forage.entity.DemandeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DemandeStatusRepository extends JpaRepository<DemandeStatus, Long> {
    
    List<DemandeStatus> findByDemandeIdDemande(Long demandeId);
    
    List<DemandeStatus> findByStatusIdStatus(Long statusId);
    
    @Query("SELECT ds FROM DemandeStatus ds WHERE ds.demande.idDemande = :demandeId ORDER BY ds.date DESC")
    List<DemandeStatus> findByDemandeIdOrderByDateDesc(@Param("demandeId") Long demandeId);
    
    // Correction: Trier par date puis par ID pour avoir un résultat unique
    @Query("SELECT ds FROM DemandeStatus ds WHERE ds.demande.idDemande = :demandeId ORDER BY ds.date DESC, ds.idDemandeStatus DESC LIMIT 1")
    Optional<DemandeStatus> findLatestStatusByDemandeId(@Param("demandeId") Long demandeId);
    
    @Query("SELECT ds FROM DemandeStatus ds WHERE ds.date BETWEEN :startDate AND :endDate")
    List<DemandeStatus> findByDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT COUNT(ds) FROM DemandeStatus ds WHERE ds.status.idStatus = :statusId AND ds.date = :date")
    long countByStatusAndDate(@Param("statusId") Long statusId, @Param("date") LocalDate date);
}