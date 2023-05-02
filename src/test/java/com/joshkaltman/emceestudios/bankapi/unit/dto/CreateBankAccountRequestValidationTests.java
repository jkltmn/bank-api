package com.joshkaltman.emceestudios.bankapi.unit.dto;


import com.joshkaltman.emceestudios.bankapi.dto.CreateBankAccountRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateBankAccountRequestValidationTests {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    private CreateBankAccountRequest buildValidBankAccountRequest() {
        CreateBankAccountRequest req = new CreateBankAccountRequest();
        req.setFirstName("Mickey");
        req.setLastName("Mouse");
        req.setPin("1923");
        req.setCurrency("USD");
        return req;
    };

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    public void noValidationViolationsForCorrectProperties() {
        var req = buildValidBankAccountRequest();

        Set<ConstraintViolation<CreateBankAccountRequest>> violations = validator.validate(req);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void validatesEmptyFirstName() {
        var req = buildValidBankAccountRequest();
        req.setFirstName("");

        Set<ConstraintViolation<CreateBankAccountRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validatesNullFirstName() {
        var req = buildValidBankAccountRequest();
        req.setFirstName(null);

        Set<ConstraintViolation<CreateBankAccountRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validatesFirstNameWithInvalidChars() {
        var req = buildValidBankAccountRequest();
        req.setFirstName("M1ck@y");

        Set<ConstraintViolation<CreateBankAccountRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validatesEmptyLastName() {
        var req = buildValidBankAccountRequest();
        req.setLastName("");

        Set<ConstraintViolation<CreateBankAccountRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validatesNullLastName() {
        var req = buildValidBankAccountRequest();
        req.setLastName(null);

        Set<ConstraintViolation<CreateBankAccountRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validatesLastNameWithInvalidChars() {
        var req = buildValidBankAccountRequest();
        req.setLastName("M0us3!");

        Set<ConstraintViolation<CreateBankAccountRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validatesEmptyPin() {
        var req = buildValidBankAccountRequest();
        req.setPin("");

        Set<ConstraintViolation<CreateBankAccountRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validatesNullPin() {
        var req = buildValidBankAccountRequest();
        req.setPin(null);

        Set<ConstraintViolation<CreateBankAccountRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validatesPinWithInvalidChars() {
        var req = buildValidBankAccountRequest();
        req.setPin("24A2");

        Set<ConstraintViolation<CreateBankAccountRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validatesPinWithTooFewChars() {
        var req = buildValidBankAccountRequest();
        req.setPin("133");

        Set<ConstraintViolation<CreateBankAccountRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validatesPinWithTooManyChars() {
        var req = buildValidBankAccountRequest();
        req.setPin("13344");

        Set<ConstraintViolation<CreateBankAccountRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }
}
