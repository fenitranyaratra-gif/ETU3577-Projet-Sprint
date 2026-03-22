package sprint.forage.repository;

import sprint.forage.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
    Optional<Client> findByNom(String nom);
    
    List<Client> findByNomContainingIgnoreCase(String nom);
    
    @Query("SELECT c FROM Client c WHERE c.contact = :contact")
    Optional<Client> findByContact(@Param("contact") String contact);
    
    @Query("SELECT c FROM Client c LEFT JOIN FETCH c.demandes WHERE c.idClient = :id")
    Optional<Client> findByIdWithDemandes(@Param("id") Long id);
    
    boolean existsByContact(String contact);
}