package com.example.miniarydartravelos.controller;

import com.example.miniarydartravelos.dto.request.TravelRequestDTO;
import com.example.miniarydartravelos.dto.response.TravelRequestResponseDTO;
import com.example.miniarydartravelos.enumtype.RequestStatus;
import com.example.miniarydartravelos.service.TravelRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class TravelRequestController {

    private final TravelRequestService travelRequestService;

    public TravelRequestController(TravelRequestService travelRequestService) {
        this.travelRequestService = travelRequestService;
    }

    @PostMapping
    public ResponseEntity<TravelRequestResponseDTO> createRequest(@Valid @RequestBody TravelRequestDTO request) {
        return new ResponseEntity<>(travelRequestService.createRequest(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TravelRequestResponseDTO>> getAllRequests() {
        return ResponseEntity.ok(travelRequestService.getAllRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TravelRequestResponseDTO> getRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(travelRequestService.getRequestById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TravelRequestResponseDTO> updateStatus(@PathVariable Long id, @RequestParam RequestStatus status) {
        return ResponseEntity.ok(travelRequestService.updateStatus(id, status));
    }
}
