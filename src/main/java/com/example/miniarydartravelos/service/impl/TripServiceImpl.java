package com.example.miniarydartravelos.service.impl;

import com.example.miniarydartravelos.dto.request.TripCostRequestDTO;
import com.example.miniarydartravelos.dto.response.TripCostResponseDTO;
import com.example.miniarydartravelos.dto.response.TripResponseDTO;
import com.example.miniarydartravelos.entity.*;
import com.example.miniarydartravelos.enumtype.PaymentStatus;
import com.example.miniarydartravelos.enumtype.QuotationStatus;
import com.example.miniarydartravelos.enumtype.TripStatus;
import com.example.miniarydartravelos.exception.BusinessRuleException;
import com.example.miniarydartravelos.exception.ResourceNotFoundException;
import com.example.miniarydartravelos.repository.*;
import com.example.miniarydartravelos.service.TripService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final QuotationRepository quotationRepository;
    private final SupplierRepository supplierRepository;
    private final TripCostRepository tripCostRepository;

    public TripServiceImpl(TripRepository tripRepository, QuotationRepository quotationRepository,
                           SupplierRepository supplierRepository, TripCostRepository tripCostRepository) {
        this.tripRepository = tripRepository;
        this.quotationRepository = quotationRepository;
        this.supplierRepository = supplierRepository;
        this.tripCostRepository = tripCostRepository;
    }

    @Override
    public TripResponseDTO createTripFromQuotation(Long quotationId) {
        Quotation quotation = quotationRepository.findById(quotationId)
                .orElseThrow(() -> new ResourceNotFoundException("Quotation not found with id: " + quotationId));

        if (quotation.getStatus() != QuotationStatus.ACCEPTED) {
            throw new BusinessRuleException("A trip can only be created from an ACCEPTED quotation.");
        }

        if (tripRepository.existsByQuotationId(quotationId)) {
            throw new BusinessRuleException("A trip has already been created for this quotation.");
        }

        TravelRequest req = quotation.getRequest();
        Trip trip = new Trip();
        trip.setClient(req.getClient());
        trip.setQuotation(quotation);
        trip.setTripName("Trip to " + req.getDestination());
        trip.setStartDate(req.getStartDate());
        trip.setEndDate(req.getEndDate());
        trip.setStatus(TripStatus.PLANNED);

        Trip saved = tripRepository.save(trip);
        return mapToTripResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TripResponseDTO getTripById(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + id));
        return mapToTripResponse(trip);
    }

    @Override
    public TripResponseDTO updateTripStatus(Long id, TripStatus status) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + id));

        // Completion Rule: Cannot complete if unpaid costs exist[cite: 1]
        if (status == TripStatus.COMPLETED) {
            boolean hasUnpaid = tripCostRepository.existsByTripIdAndPaymentStatusIn(
                    id, List.of(PaymentStatus.UNPAID, PaymentStatus.PARTIALLY_PAID)
            );
            if (hasUnpaid) {
                throw new BusinessRuleException("Cannot complete trip while unpaid or partially paid costs exist.");
            }
        }

        trip.setStatus(status);
        Trip updated = tripRepository.save(trip);
        return mapToTripResponse(updated);
    }

    @Override
    public TripCostResponseDTO addCostToTrip(Long tripId, TripCostRequestDTO request) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + tripId));

        if (trip.getStatus() == TripStatus.CANCELLED) {
            throw new BusinessRuleException("Cannot add costs to a CANCELLED trip.");
        }

        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + request.getSupplierId()));

        TripCost cost = new TripCost();
        cost.setTrip(trip);
        cost.setSupplier(supplier);
        cost.setDescription(request.getDescription());
        cost.setAmount(request.getAmount());
        cost.setCurrency(request.getCurrency());
        cost.setDueDate(request.getDueDate());
        cost.setPaymentStatus(PaymentStatus.UNPAID);

        TripCost saved = tripCostRepository.save(cost);
        return mapToCostResponse(saved);
    }

    @Override
    public TripCostResponseDTO updateCostPaymentStatus(Long costId, PaymentStatus status) {
        TripCost cost = tripCostRepository.findById(costId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip cost not found with id: " + costId));

        if (cost.getPaymentStatus() == PaymentStatus.PAID) {
            throw new BusinessRuleException("Paid costs cannot be edited.");
        }

        cost.setPaymentStatus(status);
        TripCost updated = tripCostRepository.save(cost);
        return mapToCostResponse(updated);
    }

    @Override
    public void deleteTripCost(Long costId) {
        TripCost cost = tripCostRepository.findById(costId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip cost not found with id: " + costId));

        if (cost.getPaymentStatus() == PaymentStatus.PAID) {
            throw new BusinessRuleException("Paid costs cannot be deleted.");
        }

        tripCostRepository.delete(cost);
    }

    private TripResponseDTO mapToTripResponse(Trip trip) {
        TripResponseDTO dto = new TripResponseDTO();
        dto.setId(trip.getId());
        dto.setClientId(trip.getClient().getId());
        dto.setClientName(trip.getClient().getFullName());
        dto.setQuotationId(trip.getQuotation().getId());
        dto.setTripName(trip.getTripName());
        dto.setStartDate(trip.getStartDate());
        dto.setEndDate(trip.getEndDate());
        dto.setStatus(trip.getStatus());

        List<TripCost> costs = tripCostRepository.findByTripId(trip.getId());
        BigDecimal total = costs.stream().map(TripCost::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal paid = costs.stream().filter(c -> c.getPaymentStatus() == PaymentStatus.PAID)
                .map(TripCost::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        dto.setTotalCosts(total);
        dto.setPaidCosts(paid);
        dto.setOutstandingCosts(total.subtract(paid));
        return dto;
    }

    private TripCostResponseDTO mapToCostResponse(TripCost cost) {
        TripCostResponseDTO dto = new TripCostResponseDTO();
        dto.setId(cost.getId());
        dto.setTripId(cost.getTrip().getId());
        dto.setSupplierId(cost.getSupplier().getId());
        dto.setSupplierName(cost.getSupplier().getName());
        dto.setDescription(cost.getDescription());
        dto.setAmount(cost.getAmount());
        dto.setCurrency(cost.getCurrency());
        dto.setDueDate(cost.getDueDate());
        dto.setPaymentStatus(cost.getPaymentStatus());
        return dto;
    }
}
