package com.example.empleados.integration;

import com.example.empleados.dto.ErrorResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ObtenerEmpleadoNoEncontradoIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldReturnBadRequestForInvalidPattern() {
        ResponseEntity<ErrorResponse> response = testRestTemplate.withBasicAuth("admin", "admin123")
            .exchange("/api/v2/empleados/INVALID", HttpMethod.GET, null, ErrorResponse.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturnNotFoundForMissingClave() {
        ResponseEntity<ErrorResponse> response = testRestTemplate.withBasicAuth("admin", "admin123")
            .exchange("/api/v2/empleados/EMP-9999", HttpMethod.GET, null, ErrorResponse.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
