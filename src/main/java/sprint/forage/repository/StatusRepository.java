package sprint.forage.repository;

import sprint.forage.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    
    Optional<Status> findByLibelle(String libelle);
    
    boolean existsByLibelle(String libelle);
    
    @Query("SELECT s FROM Status s LEFT JOIN FETCH s.demandeStatuses WHERE s.idStatus = :id")
    Optional<Status> findByIdWithDemandeStatuses(@Param("id") Long id);
    
    @Query("SELECT COUNT(ds) FROM DemandeStatus ds WHERE ds.status.idStatus = :statusId")
    long countUtilisationsByStatusId(@Param("statusId") Long statusId);
}