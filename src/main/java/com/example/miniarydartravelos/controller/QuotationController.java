package com.example.miniarydartravelos.controller;

import com.example.miniarydartravelos.dto.request.QuotationRequestDTO;
import com.example.miniarydartravelos.dto.response.QuotationResponseDTO;
import com.example.miniarydartravelos.service.QuotationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotations")
public class QuotationController {

    private final QuotationService quotationService;

    public QuotationController(QuotationService quotationService) {
        this.quotationService = quotationService;
    }

    @PostMapping
    public ResponseEntity<QuotationResponseDTO> createQuotation(@Valid @RequestBody QuotationRequestDTO request) {
        return new ResponseEntity<>(quotationService.createQuotation(request), HttpStatus.CREATED);
    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<List<QuotationResponseDTO>> getQuotationsByRequestId(@PathVariable Long requestId) {
        return ResponseEntity.ok(quotationService.getQuotationsByRequestId(requestId));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<QuotationResponseDTO> acceptQuotation(@PathVariable Long id) {
        return ResponseEntity.ok(quotationService.acceptQuotation(id));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<QuotationResponseDTO> rejectQuotation(@PathVariable Long id) {
        return ResponseEntity.ok(quotationService.rejectQuotation(id));
    }
}
