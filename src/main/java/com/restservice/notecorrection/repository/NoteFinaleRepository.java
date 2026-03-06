package com.restservice.notecorrection.repository;

import com.restservice.notecorrection.entity.NoteFinale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteFinaleRepository extends JpaRepository<NoteFinale, Integer> {
    Optional<NoteFinale> findByMatiereIdAndCandidatId(Integer matiereId, Integer candidatId);
    List<NoteFinale> findByMatiereId(Integer matiereId);
    List<NoteFinale> findByCandidatId(Integer candidatId);
}