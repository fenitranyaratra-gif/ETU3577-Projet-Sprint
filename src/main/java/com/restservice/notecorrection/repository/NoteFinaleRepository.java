package com.restservice.notecorrection.repository;

import com.restservice.notecorrection.entity.NoteFinale;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteFinaleRepository extends JpaRepository<NoteFinale, Integer> {
    Optional<NoteFinale> findByMatiereIdAndCandidatId(Integer matiereId, Integer candidatId);
    List<NoteFinale> findByMatiereId(Integer matiereId);
    List<NoteFinale> findByCandidatId(Integer candidatId);
    // SUPPRESSION EXPLICITE par matière et candidat (la meilleure solution)
    @Transactional
    @Modifying
    @Query("DELETE FROM NoteFinale nf WHERE nf.matiere.id = :matiereId AND nf.candidat.id = :candidatId")
    int deleteByMatiereIdAndCandidatId(@Param("matiereId") Integer matiereId, 
                                        @Param("candidatId") Integer candidatId);
}