package com.example.empleados.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.empleados.dto.DepartamentoRequest;
import com.example.empleados.dto.DepartamentoResponse;
import com.example.empleados.dto.DepartamentoUpdateRequest;

class ActualizarDepartamentoIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldUpdateDepartamentoNombre() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DepartamentoRequest createRequest = new DepartamentoRequest();
        createRequest.setClave("FIN");
        createRequest.setNombre("Finanzas");

        testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos", HttpMethod.POST,
                        new HttpEntity<>(createRequest, headers), DepartamentoResponse.class);

        DepartamentoUpdateRequest updateRequest = new DepartamentoUpdateRequest();
        updateRequest.setNombre("Control Financiero");

        ResponseEntity<DepartamentoResponse> response = testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos/FIN", HttpMethod.PUT,
                        new HttpEntity<>(updateRequest, headers), DepartamentoResponse.class);

        assertEquals("Control Financiero", response.getBody().getNombre());
        assertEquals("FIN", response.getBody().getClave());
    }
}
