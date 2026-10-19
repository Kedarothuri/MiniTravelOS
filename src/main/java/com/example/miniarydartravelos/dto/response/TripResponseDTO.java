package com.example.miniarydartravelos.dto.response;

import com.example.miniarydartravelos.enumtype.TripStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TripResponseDTO {

    private Long id;
    private Long clientId;
    private String clientName;
    private Long quotationId;
    private String tripName;
    private LocalDate startDate;
    private LocalDate endDate;
    private TripStatus status;
    private BigDecimal totalCosts;
    private BigDecimal paidCosts;
    private BigDecimal outstandingCosts;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public Long getQuotationId() { return quotationId; }
    public void setQuotationId(Long quotationId) { this.quotationId = quotationId; }

    public String getTripName() { return tripName; }
    public void setTripName(String tripName) { this.tripName = tripName; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public TripStatus getStatus() { return status; }
    public void setStatus(TripStatus status) { this.status = status; }

    public BigDecimal getTotalCosts() { return totalCosts; }
    public void setTotalCosts(BigDecimal totalCosts) { this.totalCosts = totalCosts; }

    public BigDecimal getPaidCosts() { return paidCosts; }
    public void setPaidCosts(BigDecimal paidCosts) { this.paidCosts = paidCosts; }

    public BigDecimal getOutstandingCosts() { return outstandingCosts; }
    public void setOutstandingCosts(BigDecimal outstandingCosts) { this.outstandingCosts = outstandingCosts; }
}
