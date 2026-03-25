package sprint.forage.repository;

import sprint.forage.entity.DetailsDevis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DetailsDevisRepository extends JpaRepository<DetailsDevis, Long> {
    
}