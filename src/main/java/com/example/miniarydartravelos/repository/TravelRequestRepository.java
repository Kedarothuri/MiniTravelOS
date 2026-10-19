package com.example.miniarydartravelos.repository;

import com.example.miniarydartravelos.entity.TravelRequest;
import com.example.miniarydartravelos.enumtype.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelRequestRepository extends JpaRepository<TravelRequest, Long> {
    List<TravelRequest> findByClientId(Long clientId);
    boolean existsByClientIdAndStatusIn(Long clientId, List<RequestStatus> statuses);
}
