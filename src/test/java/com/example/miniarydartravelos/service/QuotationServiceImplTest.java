package com.example.miniarydartravelos.service;

import com.example.miniarydartravelos.entity.Quotation;
import com.example.miniarydartravelos.entity.TravelRequest;
import com.example.miniarydartravelos.enumtype.QuotationStatus;
import com.example.miniarydartravelos.exception.BusinessRuleException;
import com.example.miniarydartravelos.repository.QuotationRepository;
import com.example.miniarydartravelos.repository.TravelRequestRepository;
import com.example.miniarydartravelos.service.impl.QuotationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuotationServiceImplTest {

    @Mock
    private QuotationRepository quotationRepository;

    @Mock
    private TravelRequestRepository travelRequestRepository;

    @InjectMocks
    private QuotationServiceImpl quotationService;

    private Quotation validQuotation;
    private TravelRequest travelRequest;

    @BeforeEach
    void setUp() {
        travelRequest = new TravelRequest();
        travelRequest.setId(100L);

        validQuotation = new Quotation();
        validQuotation.setId(1L);
        validQuotation.setRequest(travelRequest);
        validQuotation.setValidityDate(LocalDate.now().plusDays(10));
        validQuotation.setStatus(QuotationStatus.DRAFT);
    }

    @Test
    @DisplayName("Should throw exception when attempting to accept an expired quotation")
    void acceptQuotation_Expired_ThrowsException() {
        validQuotation.setValidityDate(LocalDate.now().minusDays(1)); // Expired date
        when(quotationRepository.findById(1L)).thenReturn(Optional.of(validQuotation));

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            quotationService.acceptQuotation(1L);
        });

        assertEquals("Cannot accept an expired quotation.", exception.getMessage());
        verify(quotationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception if another quotation is already accepted for the request")
    void acceptQuotation_AlreadyAccepted_ThrowsException() {
        when(quotationRepository.findById(1L)).thenReturn(Optional.of(validQuotation));
        when(quotationRepository.existsByRequestIdAndStatus(100L, QuotationStatus.ACCEPTED)).thenReturn(true);

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            quotationService.acceptQuotation(1L);
        });

        assertEquals("A quotation has already been accepted for this request.", exception.getMessage());
        verify(quotationRepository, never()).save(any());
    }
}