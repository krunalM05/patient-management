package com.pm.patientservice.exception;

import com.pm.patientservice.model.Patient;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException(String providedEmailAlreadyExist) {
        super(providedEmailAlreadyExist);
    }
}
