package com.pm.patientservice.dto;

import com.pm.patientservice.dto.validators.CreatePatientValidatorGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientRequestDTO {
    @NotBlank(message = "First Name is Required")
    private String firstName;

    @NotBlank(message = "Last Name is Required")
    private String lastName;

    @NotBlank(message = "Email is Required")
    @Email(message = "Please Provide Valid Email")
    private String email;

    @NotBlank(message = "Address is Required")
    private String address;

    @NotBlank(message = "Date of Birth is Required")
    private String dateOfBirth;

    @NotBlank(message = "Registration Date is Required", groups = CreatePatientValidatorGroup.class)
    private String registrationDate;

    @NotBlank(message = "Gender is Required")
    private String gender;

}
