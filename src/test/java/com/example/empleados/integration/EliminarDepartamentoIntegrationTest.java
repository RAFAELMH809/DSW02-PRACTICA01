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

class EliminarDepartamentoIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldDeleteDepartamentoWithoutEmployees() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DepartamentoRequest createRequest = new DepartamentoRequest();
        createRequest.setClave("LEGAL");
        createRequest.setNombre("Legal");

        testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos", HttpMethod.POST,
                        new HttpEntity<>(createRequest, headers), Object.class);

        ResponseEntity<Void> response = testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos/LEGAL", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
