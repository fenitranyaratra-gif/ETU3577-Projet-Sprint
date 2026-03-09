package com.restservice.notecorrection.repository;

import com.restservice.notecorrection.entity.Candidat;
import com.restservice.notecorrection.entity.Matiere;
import com.restservice.notecorrection.entity.Parametre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParametreRepository extends JpaRepository<Parametre, Integer> {
    // Ancienne méthode (ne renvoie qu'un seul résultat)
    // Parametre findByMatiereId(Integer matiereId);
    
    // Nouvelle méthode pour avoir TOUS les paramètres d'une matière
    List<Parametre> findByMatiereId(Integer matiereId);
    
    boolean existsByMatiereId(Integer matiereId);
    
    @Query("SELECT m FROM Matiere m WHERE m.id = :id")
    Matiere findMatiereById(@Param("id") Integer id);
    
    @Query("SELECT c FROM Candidat c WHERE c.id = :id")
    Candidat findCandidatById(@Param("id") Integer id);
}