package sprint.forage.repository;

import sprint.forage.entity.DemandeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandeStatusRepository extends JpaRepository<DemandeStatus, Long> {
    
}