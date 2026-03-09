package com.restservice.notecorrection.repository;

import com.restservice.notecorrection.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {
    boolean existsByMatiereIdAndCandidatIdAndCorrecteurId(Integer matiereId, Integer candidatId, Integer correcteurId);
    
    List<Note> findByMatiereId(Integer matiereId);
    
    List<Note> findByCandidatId(Integer candidatId);
    
    @Query("SELECT n FROM Note n WHERE n.matiere.id = :matiereId AND n.candidat.id = :candidatId")
    List<Note> findNotesByMatiereAndCandidat(@Param("matiereId") Integer matiereId, @Param("candidatId") Integer candidatId);
}