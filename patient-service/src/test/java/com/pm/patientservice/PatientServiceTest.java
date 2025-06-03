package com.pm.patientservice;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exception.EmailAlreadyExistException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import com.pm.patientservice.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
public class PatientServiceTest {

    private PatientService patientService;
    private ModelMapper modelMapper;
    private PatientRepository patientRepository;
    @BeforeEach
    void setUp() {
        patientRepository = mock(PatientRepository.class);
        modelMapper = mock(ModelMapper.class);
        patientService = new PatientService(patientRepository, modelMapper);
    }

    @Nested
    @DisplayName("Get Patient By Email Tests")
    class getPatientByEmailTests {
        private String email = "test@gmail.com";
        private Patient patient = new Patient();
        private PatientResponseDTO patientResponseDTO = new PatientResponseDTO();

        @BeforeEach
        void setUp() {
            patient.setEmail(email);
            patient.setFirstName("John");
            patient.setGender("Male");
            patient.setLastName("Doe");
            patient.setAddress("New York");
            patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
            patient.setRegistrationDate(LocalDate.of(2021, 1, 1));
            patient.setId(java.util.UUID.randomUUID());
            patientResponseDTO.setEmail(email);
            patientResponseDTO.setFirstName("John");
            patientResponseDTO.setLastName("Doe");
        }
        @Test
        @DisplayName("Get Patient By Correct Email Test")
        void getPatientByEmailTest_WithCorrectEmail() {

            //define
            when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient));
            when(modelMapper.map(any(Patient.class), eq(PatientResponseDTO.class))).thenReturn(patientResponseDTO);

            //act
            PatientResponseDTO result = patientService.getPatientByEmail(email);
            log.info("Patient Response DTO: {}", patientResponseDTO.toString());
            //assert
            assertNotNull(result);
            assertEquals(email, result.getEmail());
            assertEquals("John", result.getFirstName());
            assertEquals("Doe", result.getLastName());
            verify(patientRepository, times(1)).findByEmail(email);
        }

        @Test
        @DisplayName("Get Patient By InCorrect Email Test")
        void getPatientByEmailTest_WithIncorrectEmail() {
            String incorrectEmail = "notFound@example.com";
            when(patientRepository.findByEmail(incorrectEmail)).thenReturn(Optional.empty());
            assertThrows(PatientNotFoundException.class,()->patientService
                    .getPatientByEmail(incorrectEmail));
            verify(patientRepository, times(1)).findByEmail(incorrectEmail);
        }
    }

    @Nested
    @DisplayName("Get Patient By Id Tests")
    class getPatientByIdTests {
        private UUID id = UUID.randomUUID();
        private Patient patient = new Patient();
        private PatientResponseDTO patientResponseDTO = new PatientResponseDTO();

        @BeforeEach
        void setUp() {
            patient.setEmail("email@abc.com");
            patient.setFirstName("John");
            patient.setGender("Male");
            patient.setLastName("Doe");
            patient.setAddress("New York");
            patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
            patient.setRegistrationDate(LocalDate.of(2021, 1, 1));
            patient.setId(id);
            patientResponseDTO.setEmail("email@abc.com");
            patientResponseDTO.setFirstName("John");
            patientResponseDTO.setLastName("Doe");
            patientResponseDTO.setId(String.valueOf(id));
        }

        @Test
        @DisplayName("Get Patient By Correct Id Test")
        void getPatientByIdTest_WithCorrectId() {
            //define
            when(patientRepository.findById(id)).thenReturn(Optional.ofNullable(patient));
            when(modelMapper.map(any(Patient.class), eq(PatientResponseDTO.class))).thenReturn(patientResponseDTO);

            PatientResponseDTO result = patientService.getPatientById(id);

            //assert
            assertNotNull(result);
            assertEquals(id.toString(), result.getId());
            assertEquals("John", result.getFirstName());
            assertEquals("Doe", result.getLastName());
            verify(patientRepository, times(1)).findById(id);
        }

        @Test
        @DisplayName("Get Patient By InCorrect Id Test")
        void getPatientByIdTest_WithIncorrectId() {
            UUID incorrectId = UUID.randomUUID();
            when(patientRepository.findById(incorrectId)).thenReturn(Optional.empty());

            assertThrows(PatientNotFoundException.class,()->patientService
                    .getPatientById(incorrectId));
            verify(patientRepository, times(1)).findById(incorrectId);
        }
    }

    @Nested
    @DisplayName("Add Patient Tests")
    class addPatientTests {
        private PatientRequestDTO patientRequestDTO = new PatientRequestDTO();
        private Patient patient = new Patient();
        private PatientResponseDTO patientResponseDTO = new PatientResponseDTO();
        @BeforeEach
        void setUp() {
            patientRequestDTO.setEmail("johnDoe@gmailcom");
            patientRequestDTO.setFirstName("John");
            patientRequestDTO.setLastName("Doe");
            patientRequestDTO.setGender("Male");
            patientRequestDTO.setAddress("New York");
            patientRequestDTO.setDateOfBirth("1990-01-01");
            patientRequestDTO.setRegistrationDate("2021-01-01");

            UUID id = UUID.randomUUID();
            patient.setEmail("johnDoe@gmailcom");
            patient.setFirstName("John");
            patient.setGender("Male");
            patient.setLastName("Doe");
            patient.setAddress("New York");
            patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
            patient.setRegistrationDate(LocalDate.of(2021, 1, 1));
            patient.setId(id);

            patientResponseDTO.setEmail("johnDoe@gmailcom");
            patientResponseDTO.setFirstName("John");
            patientResponseDTO.setLastName("Doe");
            patientResponseDTO.setId(String.valueOf(patient.getId()));
            patientResponseDTO.setDateOfBirth("1990-01-01");
            patientResponseDTO.setAddress("New York");
        }

        @Test
        @DisplayName("Add Patient With Correct Data")
        void addPatientTest_WithCorrectData(){
            //define
            when(patientRepository.save(any(Patient.class))).thenReturn(patient);
            when(patientRepository.existsByEmail(patientRequestDTO.getEmail())).thenReturn(false);

            //act
            PatientResponseDTO result = patientService.addPatient(patientRequestDTO);

            //assert
            assertNotNull(result);
            //assertEquals(result.getId(), patient.getId());
            assertEquals(patientResponseDTO.getEmail(), result.getEmail());
            verify(patientRepository, times(1)).save(any(Patient.class));
        }

        @Test
        @DisplayName("Add Patient with Existing Email")
        void addPatientTest_WithExistingEmail(){
            when(patientRepository.existsByEmail(patientRequestDTO.getEmail())).thenReturn(true);
            assertThrows(EmailAlreadyExistException.class,()->patientService.addPatient(patientRequestDTO));
            verify(patientRepository, never()).save(any(Patient.class));
            verify(patientRepository, times(1)).existsByEmail(patientRequestDTO.getEmail());
        }
    }

    @Nested
    @DisplayName("Delete Patient Tests")
    class DeletePatientTests {

        @Test
        @DisplayName("Delete Patient By Correct ID Test")
        void deletePatientByIdTest() {
            UUID patientId = UUID.randomUUID();
            when(patientRepository.existsById(patientId)).thenReturn(true);

            patientService.deletePatient(patientId);

            verify(patientRepository, times(1)).deleteById(patientId);
        }

        @Test
        @DisplayName("Delete Patient By Incorrect (Non-existent) ID Test")
        void deletePatientByInvalidIdTest() {
            UUID invalidId = UUID.randomUUID();
            when(patientRepository.existById(invalidId)).thenReturn(false);

            assertThrows(PatientNotFoundException.class, () -> patientService.deletePatient(invalidId));

            verify(patientRepository, never()).deleteById(invalidId);
        }

    }



}
