package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exception.EmailAlreadyExistException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private PatientRepository patientRepository;
    private ModelMapper modelMapper;
    public PatientService(PatientRepository patientRepository, ModelMapper modelMapper) {
        this.patientRepository = patientRepository;
        this.modelMapper = modelMapper;
    }

    public List<PatientResponseDTO> getPatients(){
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientMapper::toDTO).collect(Collectors.toList());
    }

    public Page<PatientResponseDTO> getPatients(Pageable pageable){
        Page<Patient> patients = patientRepository.findAll(pageable);
        return patients.map(PatientMapper::toDTO);
    }

    public PatientResponseDTO getPatientByEmail(String email){
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(()-> new PatientNotFoundException("Patient Not Found with given Email: "+email));
        return modelMapper.map(patient, PatientResponseDTO.class);
    }

    public PatientResponseDTO getPatientById(UUID id){
        Patient patient = patientRepository.findById(id)
                .orElseThrow(()-> new PatientNotFoundException("Patient Not Found with given Id: "+id));

        return PatientMapper.toDTO(patient);
    }

    public PatientResponseDTO addPatient(PatientRequestDTO patientRequestDTO){
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())){
            throw  new EmailAlreadyExistException("Provided Email already exist");
        }
        Patient patient = PatientMapper.toModel(patientRequestDTO);
        patientRepository.save(patient);
        return PatientMapper.toDTO(patient);
    }

    public PatientResponseDTO updatePatient(UUID patientId, PatientRequestDTO patientRequestDTO) {

        Patient patient = patientRepository.findById(patientId).orElseThrow(
                () -> new PatientNotFoundException("Patient not found with ID: " + patientId));

        if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),patientId)){
            throw  new EmailAlreadyExistException("Provided Email already exist");
        }

        patient.setFirstName(patientRequestDTO.getFirstName());
        patient.setLastName(patientRequestDTO.getLastName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        patient.setGender(patientRequestDTO.getGender());
        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toDTO(updatedPatient);
    }

    public void deletePatient(UUID patientId){
        patientRepository.deleteById(patientId);
    }
}
