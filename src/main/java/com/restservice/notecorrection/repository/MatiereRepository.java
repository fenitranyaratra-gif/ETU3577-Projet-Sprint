package com.restservice.notecorrection.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restservice.notecorrection.entity.Matiere;

@Repository
public interface MatiereRepository extends JpaRepository<Matiere, Integer> {
}