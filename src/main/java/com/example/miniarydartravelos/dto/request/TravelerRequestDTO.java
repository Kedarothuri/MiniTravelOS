package com.example.miniarydartravelos.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class TravelerRequestDTO {

    @NotBlank(message = "Traveler name is required")
    private String name;

    private LocalDate dateOfBirth;
    private String nationality;
    private String passportNumber;
    private String specialRequirements;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public String getPassportNumber() { return passportNumber; }
    public void setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }

    public String getSpecialRequirements() { return specialRequirements; }
    public void setSpecialRequirements(String specialRequirements) { this.specialRequirements = specialRequirements; }
}