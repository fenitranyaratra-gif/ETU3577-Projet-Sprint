package com.restservice.notecorrection.repository;

import com.restservice.notecorrection.entity.Resolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ResolutionRepository extends JpaRepository<Resolution, Integer> {
    Optional<Resolution> findByLibelleNoteContainingIgnoreCase(String libelle);
}