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

class CrearDepartamentoSinTokenIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldReturnUnauthorizedWhenCreatingDepartamentoWithoutCredentials() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DepartamentoRequest request = new DepartamentoRequest();
        request.setClave("LOG");
        request.setNombre("Logistica");

        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/v2/departamentos", HttpMethod.POST, new HttpEntity<>(request, headers), String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
