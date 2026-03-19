package com.example.empleados.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.empleados.dto.DepartamentoRequest;
import com.example.empleados.dto.ErrorResponse;

class CrearDepartamentoPayloadInvalidoIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldReturnClearValidationMessageForInvalidPayload() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DepartamentoRequest request = new DepartamentoRequest();
        request.setClave("RH");
        request.setNombre(" ");

        ResponseEntity<ErrorResponse> response = testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos", HttpMethod.POST,
                        new HttpEntity<>(request, headers), ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("VALIDATION_ERROR", body.getCode());
        assertTrue(body.getMessage().toLowerCase().contains("nombre"));
    }
}
