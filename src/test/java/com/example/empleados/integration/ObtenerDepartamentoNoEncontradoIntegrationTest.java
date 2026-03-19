package com.example.empleados.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.empleados.dto.ErrorResponse;

class ObtenerDepartamentoNoEncontradoIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldReturn404WhenDepartamentoNotFound() {
        ResponseEntity<ErrorResponse> response = testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos/NO_EXISTE", HttpMethod.GET, HttpEntity.EMPTY, ErrorResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
