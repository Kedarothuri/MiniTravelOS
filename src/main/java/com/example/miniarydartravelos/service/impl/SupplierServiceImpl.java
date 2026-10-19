package com.example.miniarydartravelos.service.impl;

import com.example.miniarydartravelos.dto.request.SupplierRequestDTO;
import com.example.miniarydartravelos.dto.response.SupplierResponseDTO;
import com.example.miniarydartravelos.entity.Supplier;
import com.example.miniarydartravelos.exception.BusinessRuleException;
import com.example.miniarydartravelos.exception.ResourceNotFoundException;
import com.example.miniarydartravelos.repository.SupplierRepository;
import com.example.miniarydartravelos.repository.TripCostRepository;
import com.example.miniarydartravelos.service.SupplierService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final TripCostRepository tripCostRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository, TripCostRepository tripCostRepository) {
        this.supplierRepository = supplierRepository;
        this.tripCostRepository = tripCostRepository;
    }

    @Override
    public SupplierResponseDTO createSupplier(SupplierRequestDTO request) {
        Supplier supplier = new Supplier();
        mapToEntity(request, supplier);
        Supplier saved = supplierRepository.save(supplier);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponseDTO> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponseDTO getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
        return mapToResponse(supplier);
    }

    @Override
    public SupplierResponseDTO updateSupplier(Long id, SupplierRequestDTO request) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
        mapToEntity(request, supplier);
        Supplier updated = supplierRepository.save(supplier);
        return mapToResponse(updated);
    }

    @Override
    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));

        boolean isReferenced = tripCostRepository.existsBySupplierId(id);
        if (isReferenced) {
            throw new BusinessRuleException("Cannot delete supplier referenced by an existing trip cost.");
        }

        supplierRepository.delete(supplier);
    }

    private void mapToEntity(SupplierRequestDTO request, Supplier supplier) {
        supplier.setName(request.getName());
        supplier.setType(request.getType());
        supplier.setContactPerson(request.getContactPerson());
        supplier.setEmail(request.getEmail());
        supplier.setPhone(request.getPhone());
        supplier.setCity(request.getCity());
        if (request.getActive() != null) {
            supplier.setActive(request.getActive());
        }
    }

    private SupplierResponseDTO mapToResponse(Supplier supplier) {
        SupplierResponseDTO dto = new SupplierResponseDTO();
        dto.setId(supplier.getId());
        dto.setName(supplier.getName());
        dto.setType(supplier.getType());
        dto.setContactPerson(supplier.getContactPerson());
        dto.setEmail(supplier.getEmail());
        dto.setPhone(supplier.getPhone());
        dto.setCity(supplier.getCity());
        dto.setActive(supplier.isActive());
        return dto;
    }
}
