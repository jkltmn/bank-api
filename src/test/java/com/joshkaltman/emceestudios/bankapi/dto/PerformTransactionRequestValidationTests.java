package com.joshkaltman.emceestudios.bankapi.dto;

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

public class PerformTransactionRequestValidationTests {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    private PerformTransactionRequest buildValidPerformTransactionRequest() {
        PerformTransactionRequest request = new PerformTransactionRequest();
        request.setPin("2495");
        request.setAmount("100.00");

        return request;
    }

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
        var req = buildValidPerformTransactionRequest();

        Set<ConstraintViolation<PerformTransactionRequest>> violations = validator.validate(req);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void permitsNegativeAmount() {
        var req = buildValidPerformTransactionRequest();
        req.setAmount("-80.00");

        Set<ConstraintViolation<PerformTransactionRequest>> violations = validator.validate(req);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void validatesNullAmount() {
        var req = buildValidPerformTransactionRequest();
        req.setAmount(null);

        Set<ConstraintViolation<PerformTransactionRequest>> violations = validator.validate(req);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void validatesEmptyAmount() {
        var req = buildValidPerformTransactionRequest();
        req.setAmount("");

        Set<ConstraintViolation<PerformTransactionRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validatesAmountWithInvalidChars() {
        var req = buildValidPerformTransactionRequest();
        Set<ConstraintViolation<PerformTransactionRequest>> violations;

        req.setAmount("+10.00");
        violations = validator.validate(req);
        assertFalse(violations.isEmpty());

        req.setAmount("$10.00");
        violations = validator.validate(req);
        assertFalse(violations.isEmpty());

        req.setAmount("A0.00");
        violations = validator.validate(req);
        assertFalse(violations.isEmpty());

        req.setAmount("A0-00");
        violations = validator.validate(req);
        assertFalse(violations.isEmpty());

        req.setAmount("10.0");
        violations = validator.validate(req);
        assertFalse(violations.isEmpty());

        req.setAmount("10.000");
        violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }


    @Test
    public void validatesEmptyPin() {
        var req = buildValidPerformTransactionRequest();
        req.setPin("");

        Set<ConstraintViolation<PerformTransactionRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validatesNullPin() {
        var req = buildValidPerformTransactionRequest();
        req.setPin(null);

        Set<ConstraintViolation<PerformTransactionRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validatesPinWithInvalidChars() {
        var req = buildValidPerformTransactionRequest();
        req.setPin("24A2");

        Set<ConstraintViolation<PerformTransactionRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validatesPinWithTooFewChars() {
        var req = buildValidPerformTransactionRequest();
        req.setPin("133");

        Set<ConstraintViolation<PerformTransactionRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void validatesPinWithTooManyChars() {
        var req = buildValidPerformTransactionRequest();
        req.setPin("13344");

        Set<ConstraintViolation<PerformTransactionRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty());
    }
}
