package com.pm.patientservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientResponseDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String dateOfBirth;
    private String gender;
}
