package com.pm.patientservice.controller;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.dto.validators.CreatePatientValidatorGroup;
import com.pm.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.UUID;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Patient Management", description = "Patient Management REST API")
@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private PatientService patientService;
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(summary = "Get all patients")
    public ResponseEntity<List<PatientResponseDTO>> getPatients(){
        return ResponseEntity.ok().body(patientService.getPatients());
    }

    @Operation(summary = "Get all patients with pagination and sortby")
    @GetMapping(params = {"pageNumber", "size"})
    public ResponseEntity<Page<PatientResponseDTO>> getPatients(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Optional<String> sortby
    ){
        Pageable pageable;

        if (sortby.isPresent()){
            pageable = PageRequest.of(pageNumber, size, Sort.by(sortby.get()).ascending());
        }else {
            pageable = PageRequest.of(pageNumber, size);
        }
        Page<PatientResponseDTO> patients = patientService.getPatients(pageable);
        return ResponseEntity.ok().body(patients);
    }

    @GetMapping("/{patientId}")
    @Operation(summary = "Get patient by Id")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable UUID patientId){
        return ResponseEntity.ok().body(patientService.getPatientById(patientId));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get Patient by Email")
    public ResponseEntity<PatientResponseDTO> getPatientByEmail(@PathVariable String email){
        return ResponseEntity.ok().body(patientService.getPatientByEmail(email));
    }

    @PostMapping
    @Operation(summary = "Create new patient")
    public ResponseEntity<PatientResponseDTO> createPatient(
            @Validated({Default.class, CreatePatientValidatorGroup.class}) @RequestBody PatientRequestDTO patientRequestDTO){
        return new ResponseEntity<>(patientService.addPatient(patientRequestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{patientId}")
    @Operation(summary = "Update patient")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID patientId, @Validated(Default.class) @RequestBody PatientRequestDTO patientRequestDTO){
        return ResponseEntity.ok().body(patientService.updatePatient(patientId, patientRequestDTO));
    }

    @DeleteMapping("/{patientId}")
    @Operation(summary = "Delete patient")
    public ResponseEntity<Object> deletePatient(@PathVariable UUID patientId){
        patientService.deletePatient(patientId);
        return ResponseEntity.noContent().build();
    }
}
