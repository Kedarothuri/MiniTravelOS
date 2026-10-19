package com.example.miniarydartravelos.service;

import com.example.miniarydartravelos.dto.request.TravelRequestDTO;
import com.example.miniarydartravelos.dto.response.TravelRequestResponseDTO;
import com.example.miniarydartravelos.enumtype.RequestStatus;

import java.util.List;

public interface TravelRequestService {
    TravelRequestResponseDTO createRequest(TravelRequestDTO request);
    List<TravelRequestResponseDTO> getAllRequests();
    TravelRequestResponseDTO getRequestById(Long id);
    TravelRequestResponseDTO updateStatus(Long id, RequestStatus status);
}
