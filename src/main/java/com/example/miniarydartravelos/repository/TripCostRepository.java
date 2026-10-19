package com.example.miniarydartravelos.repository;

import com.example.miniarydartravelos.entity.TripCost;
import com.example.miniarydartravelos.enumtype.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripCostRepository extends JpaRepository<TripCost, Long> {
    List<TripCost> findByTripId(Long tripId);
    boolean existsBySupplierId(Long supplierId);
    boolean existsByTripIdAndPaymentStatusIn(Long tripId, List<PaymentStatus> statuses);
}
