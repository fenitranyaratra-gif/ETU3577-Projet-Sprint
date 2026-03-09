package com.restservice.notecorrection.repository;

import com.restservice.notecorrection.entity.Operateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperateurRepository extends JpaRepository<Operateur, Integer> {
}