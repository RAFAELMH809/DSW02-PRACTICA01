package com.example.empleados.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.empleados.dto.DepartamentoRequest;
import com.example.empleados.dto.DepartamentoResponse;

class ObtenerDepartamentoIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldGetDepartamentoByClave() {
        DepartamentoRequest request = new DepartamentoRequest();
        request.setClave("TI");
        request.setNombre("Tecnologia");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos", HttpMethod.POST,
                        new HttpEntity<>(request, headers), DepartamentoResponse.class);

        ResponseEntity<DepartamentoResponse> response = testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos/TI", HttpMethod.GET,
                        HttpEntity.EMPTY, DepartamentoResponse.class);

        assertNotNull(response.getBody());
        assertEquals("TI", response.getBody().getClave());
    }
}
