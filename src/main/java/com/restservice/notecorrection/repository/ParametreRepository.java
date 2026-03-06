package com.restservice.notecorrection.repository;

import com.restservice.notecorrection.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametreRepository extends JpaRepository<Parametre, Integer> {
    Parametre findByMatiereId(Integer matiereId);
    boolean existsByMatiereId(Integer matiereId);
    
    @Query("SELECT m FROM Matiere m WHERE m.id = :id")
    Matiere findMatiereById(@Param("id") Integer id);
    
    @Query("SELECT c FROM Candidat c WHERE c.id = :id")
    Candidat findCandidatById(@Param("id") Integer id);
}