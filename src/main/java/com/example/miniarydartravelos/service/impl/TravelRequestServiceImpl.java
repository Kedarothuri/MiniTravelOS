package com.example.miniarydartravelos.service.impl;

import com.example.miniarydartravelos.dto.request.TravelRequestDTO;
import com.example.miniarydartravelos.dto.response.TravelRequestResponseDTO;
import com.example.miniarydartravelos.entity.Client;
import com.example.miniarydartravelos.entity.TravelRequest;
import com.example.miniarydartravelos.enumtype.RequestStatus;
import com.example.miniarydartravelos.exception.BusinessRuleException;
import com.example.miniarydartravelos.exception.ResourceNotFoundException;
import com.example.miniarydartravelos.repository.ClientRepository;
import com.example.miniarydartravelos.repository.TravelRequestRepository;
import com.example.miniarydartravelos.service.TravelRequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TravelRequestServiceImpl implements TravelRequestService {

    private final TravelRequestRepository travelRequestRepository;
    private final ClientRepository clientRepository;

    public TravelRequestServiceImpl(TravelRequestRepository travelRequestRepository, ClientRepository clientRepository) {
        this.travelRequestRepository = travelRequestRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public TravelRequestResponseDTO createRequest(TravelRequestDTO request) {
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BusinessRuleException("End date cannot be before start date.");
        }

        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + request.getClientId()));

        TravelRequest travelRequest = new TravelRequest();
        travelRequest.setClient(client);
        travelRequest.setDestination(request.getDestination());
        travelRequest.setStartDate(request.getStartDate());
        travelRequest.setEndDate(request.getEndDate());
        travelRequest.setTravelerCount(request.getTravelerCount());
        travelRequest.setBudget(request.getBudget());
        travelRequest.setCurrency(request.getCurrency());
        travelRequest.setNotes(request.getNotes());
        travelRequest.setAssignedEmployee(request.getAssignedEmployee());
        travelRequest.setStatus(RequestStatus.NEW);

        TravelRequest saved = travelRequestRepository.save(travelRequest);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TravelRequestResponseDTO> getAllRequests() {
        return travelRequestRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TravelRequestResponseDTO getRequestById(Long id) {
        TravelRequest request = travelRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Travel request not found with id: " + id));
        return mapToResponse(request);
    }

    @Override
    public TravelRequestResponseDTO updateStatus(Long id, RequestStatus status) {
        TravelRequest request = travelRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Travel request not found with id: " + id));
        request.setStatus(status);
        TravelRequest updated = travelRequestRepository.save(request);
        return mapToResponse(updated);
    }

    private TravelRequestResponseDTO mapToResponse(TravelRequest req) {
        TravelRequestResponseDTO dto = new TravelRequestResponseDTO();
        dto.setId(req.getId());
        dto.setClientId(req.getClient().getId());
        dto.setClientName(req.getClient().getFullName());
        dto.setDestination(req.getDestination());
        dto.setStartDate(req.getStartDate());
        dto.setEndDate(req.getEndDate());
        dto.setTravelerCount(req.getTravelerCount());
        dto.setBudget(req.getBudget());
        dto.setCurrency(req.getCurrency());
        dto.setNotes(req.getNotes());
        dto.setAssignedEmployee(req.getAssignedEmployee());
        dto.setStatus(req.getStatus());
        return dto;
    }
}
