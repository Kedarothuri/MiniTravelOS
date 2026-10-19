package com.example.miniarydartravelos.dto.response;

import com.example.miniarydartravelos.enumtype.SupplierType;

public class SupplierResponseDTO {

    private Long id;
    private String name;
    private SupplierType type;
    private String contactPerson;
    private String email;
    private String phone;
    private String city;
    private boolean active;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public SupplierType getType() { return type; }
    public void setType(SupplierType type) { this.type = type; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
