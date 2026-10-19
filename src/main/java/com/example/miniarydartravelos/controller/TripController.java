package com.example.miniarydartravelos.controller;

import com.example.miniarydartravelos.dto.request.TripCostRequestDTO;
import com.example.miniarydartravelos.dto.response.TripCostResponseDTO;
import com.example.miniarydartravelos.dto.response.TripResponseDTO;
import com.example.miniarydartravelos.enumtype.PaymentStatus;
import com.example.miniarydartravelos.enumtype.TripStatus;
import com.example.miniarydartravelos.service.TripService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping("/from-quotation/{quotationId}")
    public ResponseEntity<TripResponseDTO> createTripFromQuotation(@PathVariable Long quotationId) {
        return new ResponseEntity<>(tripService.createTripFromQuotation(quotationId), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripResponseDTO> getTripById(@PathVariable Long id) {
        return ResponseEntity.ok(tripService.getTripById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TripResponseDTO> updateTripStatus(@PathVariable Long id, @RequestParam TripStatus status) {
        return ResponseEntity.ok(tripService.updateTripStatus(id, status));
    }

    // Costs
    @PostMapping("/{tripId}/costs")
    public ResponseEntity<TripCostResponseDTO> addCostToTrip(@PathVariable Long tripId, @Valid @RequestBody TripCostRequestDTO request) {
        return new ResponseEntity<>(tripService.addCostToTrip(tripId, request), HttpStatus.CREATED);
    }

    @PatchMapping("/costs/{costId}/payment-status")
    public ResponseEntity<TripCostResponseDTO> updateCostPaymentStatus(@PathVariable Long costId, @RequestParam PaymentStatus status) {
        return ResponseEntity.ok(tripService.updateCostPaymentStatus(costId, status));
    }

    @DeleteMapping("/costs/{costId}")
    public ResponseEntity<Void> deleteTripCost(@PathVariable Long costId) {
        tripService.deleteTripCost(costId);
        return ResponseEntity.noContent().build();
    }
}
