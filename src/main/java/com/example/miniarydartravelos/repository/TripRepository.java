package com.example.miniarydartravelos.repository;

import com.example.miniarydartravelos.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    boolean existsByQuotationId(Long quotationId);
}