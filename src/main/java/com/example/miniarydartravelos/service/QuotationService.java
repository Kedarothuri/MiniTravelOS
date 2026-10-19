package com.example.miniarydartravelos.service;

import com.example.miniarydartravelos.dto.request.QuotationRequestDTO;
import com.example.miniarydartravelos.dto.response.QuotationResponseDTO;

import java.util.List;

public interface QuotationService {
    QuotationResponseDTO createQuotation(QuotationRequestDTO request);
    List<QuotationResponseDTO> getQuotationsByRequestId(Long requestId);
    QuotationResponseDTO acceptQuotation(Long id);
    QuotationResponseDTO rejectQuotation(Long id);
}