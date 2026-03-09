package com.restservice.notecorrection.repository;

import com.restservice.notecorrection.entity.Correcteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorrecteurRepository extends JpaRepository<Correcteur, Integer> {
}