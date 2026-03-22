package sprint.forage.repository;

import sprint.forage.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    
    // Recherche par libellé
    Optional<Status> findByLibelle(String libelle);
    
    // Vérifier si un libellé existe
    boolean existsByLibelle(String libelle);
    
    // Méthode correcte avec @Query explicite pour récupérer le statut avec ses demandesStatus
    @Query("SELECT s FROM Status s LEFT JOIN FETCH s.demandeStatuses WHERE s.idStatus = :id")
    Optional<Status> findByIdWithDemandeStatuses(@Param("id") Long id);
    
    // Alternative: Si vous voulez juste compter les utilisations
    @Query("SELECT COUNT(ds) FROM DemandeStatus ds WHERE ds.status.idStatus = :statusId")
    long countUtilisationsByStatusId(@Param("statusId") Long statusId);
}