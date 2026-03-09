package com.restservice.notecorrection.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restservice.notecorrection.entity.Candidat;

@Repository
public interface CandidatRepository extends JpaRepository<Candidat, Integer> {
    boolean existsByNomAndPrenom(String nom, String prenom);

    List<Candidat> findAllByOrderByNomAsc();
}