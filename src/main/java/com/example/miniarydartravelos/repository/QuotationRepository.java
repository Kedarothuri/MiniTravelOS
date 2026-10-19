package com.example.miniarydartravelos.repository;

import com.example.miniarydartravelos.entity.Quotation;
import com.example.miniarydartravelos.enumtype.QuotationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation, Long> {
    List<Quotation> findByRequestId(Long requestId);
    boolean existsByRequestIdAndStatus(Long requestId, QuotationStatus status);
}