package com.example.empleados.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.empleados.dto.DepartamentoRequest;
import com.example.empleados.dto.DepartamentoUpdateRequest;

class ActualizarDepartamentoSinTokenIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldReturnUnauthorizedWhenUpdatingDepartamentoWithoutCredentials() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DepartamentoRequest createRequest = new DepartamentoRequest();
        createRequest.setClave("COM");
        createRequest.setNombre("Compras");

        testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos", HttpMethod.POST,
                        new HttpEntity<>(createRequest, headers), Object.class);

        DepartamentoUpdateRequest updateRequest = new DepartamentoUpdateRequest();
        updateRequest.setNombre("Compras Globales");

        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/v2/departamentos/COM", HttpMethod.PUT,
                new HttpEntity<>(updateRequest, headers), String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
