package com.example.miniarydartravelos.controller;

import com.example.miniarydartravelos.dto.request.ClientRequestDTO;
import com.example.miniarydartravelos.dto.request.TravelerRequestDTO;
import com.example.miniarydartravelos.dto.response.ClientResponseDTO;
import com.example.miniarydartravelos.dto.response.TravelerResponseDTO;
import com.example.miniarydartravelos.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@Valid @RequestBody ClientRequestDTO request) {
        ClientResponseDTO created = clientService.createClient(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ClientResponseDTO>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> updateClient(@PathVariable Long id, @Valid @RequestBody ClientRequestDTO request) {
        return ResponseEntity.ok(clientService.updateClient(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    // Traveler Sub-Endpoints
    @PostMapping("/{clientId}/travelers")
    public ResponseEntity<TravelerResponseDTO> addTraveler(
            @PathVariable Long clientId,
            @Valid @RequestBody TravelerRequestDTO request) {
        TravelerResponseDTO created = clientService.addTraveler(clientId, request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{clientId}/travelers")
    public ResponseEntity<List<TravelerResponseDTO>> getTravelersByClientId(@PathVariable Long clientId) {
        return ResponseEntity.ok(clientService.getTravelersByClientId(clientId));
    }
}
