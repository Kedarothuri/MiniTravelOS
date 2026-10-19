package com.example.miniarydartravelos.service.impl;

import com.example.miniarydartravelos.dto.request.ClientRequestDTO;
import com.example.miniarydartravelos.dto.request.TravelerRequestDTO;
import com.example.miniarydartravelos.dto.response.ClientResponseDTO;
import com.example.miniarydartravelos.dto.response.TravelerResponseDTO;
import com.example.miniarydartravelos.entity.Client;
import com.example.miniarydartravelos.entity.Traveler;
import com.example.miniarydartravelos.enumtype.RequestStatus;
import com.example.miniarydartravelos.exception.BusinessRuleException;
import com.example.miniarydartravelos.exception.ResourceNotFoundException;
import com.example.miniarydartravelos.repository.ClientRepository;
import com.example.miniarydartravelos.repository.TravelRequestRepository;
import com.example.miniarydartravelos.repository.TravelerRepository;
import com.example.miniarydartravelos.service.ClientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final TravelerRepository travelerRepository;
    private final TravelRequestRepository travelRequestRepository;

    public ClientServiceImpl(ClientRepository clientRepository, TravelerRepository travelerRepository,
                             TravelRequestRepository travelRequestRepository) {
        this.clientRepository = clientRepository;
        this.travelerRepository = travelerRepository;
        this.travelRequestRepository = travelRequestRepository;
    }

    @Override
    public ClientResponseDTO createClient(ClientRequestDTO request) {
        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new BusinessRuleException("Client with email " + request.getEmail() + " already exists.");
        }

        Client client = new Client();
        client.setFullName(request.getFullName());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        client.setCountry(request.getCountry());
        client.setNotes(request.getNotes());

        Client saved = clientRepository.save(client);
        return mapToClientResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientResponseDTO> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(this::mapToClientResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponseDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        return mapToClientResponse(client);
    }

    @Override
    public ClientResponseDTO updateClient(Long id, ClientRequestDTO request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        client.setFullName(request.getFullName());
        client.setPhone(request.getPhone());
        client.setCountry(request.getCountry());
        client.setNotes(request.getNotes());

        Client updated = clientRepository.save(client);
        return mapToClientResponse(updated);
    }

    @Override
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        boolean hasActiveRequests = travelRequestRepository.existsByClientIdAndStatusIn(
                id, List.of(RequestStatus.NEW, RequestStatus.IN_PROGRESS, RequestStatus.QUOTED, RequestStatus.ACCEPTED)
        );

        if (hasActiveRequests) {
            throw new BusinessRuleException("Cannot delete client with active travel requests.");
        }

        clientRepository.delete(client);
    }

    @Override
    public TravelerResponseDTO addTraveler(Long clientId, TravelerRequestDTO request) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));

        Traveler traveler = new Traveler();
        traveler.setClient(client);
        traveler.setName(request.getName());
        traveler.setDateOfBirth(request.getDateOfBirth());
        traveler.setNationality(request.getNationality());
        traveler.setPassportNumber(request.getPassportNumber());
        traveler.setSpecialRequirements(request.getSpecialRequirements());

        Traveler saved = travelerRepository.save(traveler);
        return mapToTravelerResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TravelerResponseDTO> getTravelersByClientId(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new ResourceNotFoundException("Client not found with id: " + clientId);
        }
        return travelerRepository.findByClientId(clientId)
                .stream()
                .map(this::mapToTravelerResponse)
                .collect(Collectors.toList());
    }

    // Helper Mappers
    private ClientResponseDTO mapToClientResponse(Client client) {
        ClientResponseDTO dto = new ClientResponseDTO();
        dto.setId(client.getId());
        dto.setFullName(client.getFullName());
        dto.setEmail(client.getEmail());
        dto.setPhone(client.getPhone());
        dto.setCountry(client.getCountry());
        dto.setNotes(client.getNotes());
        dto.setCreatedAt(client.getCreatedAt());
        dto.setUpdatedAt(client.getUpdatedAt());
        return dto;
    }

    private TravelerResponseDTO mapToTravelerResponse(Traveler traveler) {
        TravelerResponseDTO dto = new TravelerResponseDTO();
        dto.setId(traveler.getId());
        dto.setClientId(traveler.getClient().getId());
        dto.setName(traveler.getName());
        dto.setDateOfBirth(traveler.getDateOfBirth());
        dto.setNationality(traveler.getNationality());
        dto.setPassportNumber(traveler.getPassportNumber());
        dto.setSpecialRequirements(traveler.getSpecialRequirements());
        return dto;
    }
}
