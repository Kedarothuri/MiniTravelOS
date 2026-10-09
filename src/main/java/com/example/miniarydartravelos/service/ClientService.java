package com.example.miniarydartravelos.service;

import com.example.miniarydartravelos.dto.request.ClientRequestDTO;
import com.example.miniarydartravelos.dto.request.TravelerRequestDTO;
import com.example.miniarydartravelos.dto.response.ClientResponseDTO;
import com.example.miniarydartravelos.dto.response.TravelerResponseDTO;

import java.util.List;

public interface ClientService {
    ClientResponseDTO createClient(ClientRequestDTO request);
    List<ClientResponseDTO> getAllClients();
    ClientResponseDTO getClientById(Long id);
    ClientResponseDTO updateClient(Long id, ClientRequestDTO request);
    void deleteClient(Long id);

    TravelerResponseDTO addTraveler(Long clientId, TravelerRequestDTO request);
    List<TravelerResponseDTO> getTravelersByClientId(Long clientId);
}