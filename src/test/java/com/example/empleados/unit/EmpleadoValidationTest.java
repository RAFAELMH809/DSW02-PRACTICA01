package com.example.empleados.unit;

import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.example.empleados.dto.EmpleadoRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class EmpleadoValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setup() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        validatorFactory.close();
    }

    @Test
    void shouldFailWhenAnyFieldExceeds100Chars() {
        EmpleadoRequest request = new EmpleadoRequest();
        request.setNombre("a".repeat(101));
        request.setDireccion("b".repeat(101));
        request.setTelefono("c".repeat(101));

        Set<ConstraintViolation<EmpleadoRequest>> violations = validator.validate(request);

        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nombre")));
        Assertions.assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("direccion")));
        Assertions.assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("telefono")));
    }
}
