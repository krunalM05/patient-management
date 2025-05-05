package com.pm.patientservice.mapper;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.model.Patient;

import java.time.LocalDate;

public class PatientMapper {

    public static PatientResponseDTO toDTO(Patient patient){
        PatientResponseDTO patientResponseDTO = new PatientResponseDTO();
        patientResponseDTO.setId(String.valueOf(patient.getId()));
        patientResponseDTO.setFirstName(patient.getFirstName());
        patientResponseDTO.setLastName(patient.getLastName());
        patientResponseDTO.setEmail(patient.getEmail());
        patientResponseDTO.setAddress(patient.getAddress());
        patientResponseDTO.setDateOfBirth(String.valueOf(patient.getDateOfBirth()));
        patientResponseDTO.setGender(patient.getGender());
        return patientResponseDTO;
    }

    public static Patient toModel(PatientRequestDTO patientRequestDTO){
        Patient patient = new Patient();
        patient.setFirstName(patientRequestDTO.getFirstName());
        patient.setLastName(patientRequestDTO.getLastName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getRegistrationDate()));
        patient.setGender(patientRequestDTO.getGender());
        patient.setRegistrationDate(LocalDate.parse(patientRequestDTO.getRegistrationDate()));
        return patient;
    }

}
