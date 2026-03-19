package com.example.empleados.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.empleados.dto.DepartamentoRequest;
import com.example.empleados.dto.DepartamentoResponse;

class CrearDepartamentoIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldCreateDepartamento() {
        DepartamentoRequest request = new DepartamentoRequest();
        request.setClave("rh");
        request.setNombre("Recursos Humanos");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<DepartamentoResponse> response = testRestTemplate
                .withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos", HttpMethod.POST,
                        new HttpEntity<>(request, headers), DepartamentoResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("RH", response.getBody().getClave());
        assertEquals(0, response.getBody().getEmployeeCount());
    }
}
