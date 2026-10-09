package com.example.miniarydartravelos.repository;

import com.example.miniarydartravelos.entity.Traveler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelerRepository extends JpaRepository<Traveler, Long> {
    List<Traveler> findByClientId(Long clientId);
}