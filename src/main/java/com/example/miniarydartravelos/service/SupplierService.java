package com.example.miniarydartravelos.service;

import com.example.miniarydartravelos.dto.request.SupplierRequestDTO;
import com.example.miniarydartravelos.dto.response.SupplierResponseDTO;

import java.util.List;

public interface SupplierService {
    SupplierResponseDTO createSupplier(SupplierRequestDTO request);
    List<SupplierResponseDTO> getAllSuppliers();
    SupplierResponseDTO getSupplierById(Long id);
    SupplierResponseDTO updateSupplier(Long id, SupplierRequestDTO request);
    void deleteSupplier(Long id);
}
