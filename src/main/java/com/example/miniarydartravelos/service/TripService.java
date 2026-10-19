package com.example.miniarydartravelos.service;

import com.example.miniarydartravelos.dto.request.TripCostRequestDTO;
import com.example.miniarydartravelos.dto.response.TripCostResponseDTO;
import com.example.miniarydartravelos.dto.response.TripResponseDTO;
import com.example.miniarydartravelos.enumtype.PaymentStatus;
import com.example.miniarydartravelos.enumtype.TripStatus;

import java.util.List;

public interface TripService {
    TripResponseDTO createTripFromQuotation(Long quotationId);
    TripResponseDTO getTripById(Long id);
    TripResponseDTO updateTripStatus(Long id, TripStatus status);

    TripCostResponseDTO addCostToTrip(Long tripId, TripCostRequestDTO request);
    TripCostResponseDTO updateCostPaymentStatus(Long costId, PaymentStatus status);
    void deleteTripCost(Long costId);
}
