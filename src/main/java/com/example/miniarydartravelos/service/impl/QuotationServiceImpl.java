package com.example.miniarydartravelos.service.impl;

import com.example.miniarydartravelos.dto.request.QuotationRequestDTO;
import com.example.miniarydartravelos.dto.response.QuotationResponseDTO;
import com.example.miniarydartravelos.entity.Quotation;
import com.example.miniarydartravelos.entity.TravelRequest;
import com.example.miniarydartravelos.enumtype.QuotationStatus;
import com.example.miniarydartravelos.enumtype.RequestStatus;
import com.example.miniarydartravelos.exception.BusinessRuleException;
import com.example.miniarydartravelos.exception.ResourceNotFoundException;
import com.example.miniarydartravelos.repository.QuotationRepository;
import com.example.miniarydartravelos.repository.TravelRequestRepository;
import com.example.miniarydartravelos.service.QuotationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuotationServiceImpl implements QuotationService {

    private final QuotationRepository quotationRepository;
    private final TravelRequestRepository travelRequestRepository;

    public QuotationServiceImpl(QuotationRepository quotationRepository, TravelRequestRepository travelRequestRepository) {
        this.quotationRepository = quotationRepository;
        this.travelRequestRepository = travelRequestRepository;
    }

    @Override
    public QuotationResponseDTO createQuotation(QuotationRequestDTO request) {
        TravelRequest travelRequest = travelRequestRepository.findById(request.getRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Travel request not found with id: " + request.getRequestId()));

        Quotation quotation = new Quotation();
        quotation.setRequest(travelRequest);
        quotation.setTitle(request.getTitle());
        quotation.setDescription(request.getDescription());
        quotation.setCurrency(request.getCurrency());
        quotation.setTotalAmount(request.getTotalAmount());
        quotation.setValidityDate(request.getValidityDate());
        quotation.setNotes(request.getNotes());
        quotation.setStatus(QuotationStatus.DRAFT);

        Quotation saved = quotationRepository.save(quotation);

        // Update travel request status to QUOTED
        travelRequest.setStatus(RequestStatus.QUOTED);
        travelRequestRepository.save(travelRequest);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuotationResponseDTO> getQuotationsByRequestId(Long requestId) {
        return quotationRepository.findByRequestId(requestId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public QuotationResponseDTO acceptQuotation(Long id) {
        Quotation quotation = quotationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quotation not found with id: " + id));

        // Validation 1: Expired quotation rule
        if (quotation.getValidityDate().isBefore(LocalDate.now())) {
            throw new BusinessRuleException("Cannot accept an expired quotation.");
        }

        // Validation 2: Single accepted quotation rule
        boolean existsAccepted = quotationRepository.existsByRequestIdAndStatus(
                quotation.getRequest().getId(), QuotationStatus.ACCEPTED
        );
        if (existsAccepted) {
            throw new BusinessRuleException("A quotation has already been accepted for this request.");
        }

        quotation.setStatus(QuotationStatus.ACCEPTED);
        quotation.getRequest().setStatus(RequestStatus.ACCEPTED);

        Quotation updated = quotationRepository.save(quotation);
        return mapToResponse(updated);
    }

    @Override
    public QuotationResponseDTO rejectQuotation(Long id) {
        Quotation quotation = quotationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quotation not found with id: " + id));
        quotation.setStatus(QuotationStatus.REJECTED);
        Quotation updated = quotationRepository.save(quotation);
        return mapToResponse(updated);
    }

    private QuotationResponseDTO mapToResponse(Quotation q) {
        QuotationResponseDTO dto = new QuotationResponseDTO();
        dto.setId(q.getId());
        dto.setRequestId(q.getRequest().getId());
        dto.setTitle(q.getTitle());
        dto.setDescription(q.getDescription());
        dto.setCurrency(q.getCurrency());
        dto.setTotalAmount(q.getTotalAmount());
        dto.setValidityDate(q.getValidityDate());
        dto.setNotes(q.getNotes());
        dto.setStatus(q.getStatus());
        return dto;
    }
}
