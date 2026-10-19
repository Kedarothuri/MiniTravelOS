package com.example.miniarydartravelos.service;

import com.example.miniarydartravelos.entity.TripCost;
import com.example.miniarydartravelos.enumtype.PaymentStatus;
import com.example.miniarydartravelos.exception.BusinessRuleException;
import com.example.miniarydartravelos.repository.TripCostRepository;
import com.example.miniarydartravelos.repository.TripRepository;
import com.example.miniarydartravelos.service.impl.TripServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServiceImplTest {

    @Mock
    private TripCostRepository tripCostRepository;

    @InjectMocks
    private TripServiceImpl tripService;

    @Test
    @DisplayName("Should throw exception when trying to edit or delete a PAID trip cost")
    void deleteTripCost_WhenPaid_ThrowsException() {
        TripCost paidCost = new TripCost();
        paidCost.setId(5L);
        paidCost.setPaymentStatus(PaymentStatus.PAID);

        when(tripCostRepository.findById(5L)).thenReturn(Optional.of(paidCost));

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            tripService.deleteTripCost(5L);
        });

        assertEquals("Paid costs cannot be deleted.", exception.getMessage());
        verify(tripCostRepository, never()).delete(any());
    }
}