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
import com.example.empleados.dto.ErrorResponse;

class CrearDepartamentoClaveDuplicadaIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldFailWhenClaveAlreadyExistsIgnoringCase() {
        DepartamentoRequest first = new DepartamentoRequest();
        first.setClave("RH");
        first.setNombre("Recursos Humanos");

        DepartamentoRequest second = new DepartamentoRequest();
        second.setClave("rh");
        second.setNombre("Otra Area");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos", HttpMethod.POST,
                        new HttpEntity<>(first, headers), Object.class);

        ResponseEntity<ErrorResponse> response = testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos", HttpMethod.POST,
                        new HttpEntity<>(second, headers), ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
