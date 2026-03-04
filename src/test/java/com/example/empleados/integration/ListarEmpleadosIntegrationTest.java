package com.example.empleados.integration;

import com.example.empleados.dto.EmpleadoRequest;
import com.example.empleados.dto.EmpleadoResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

class ListarEmpleadosIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldListEmpleados() {
        EmpleadoRequest request = new EmpleadoRequest();
        request.setNombre("Maria");
        request.setDireccion("Calle B");
        request.setTelefono("555-222");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/empleados", HttpMethod.POST, new HttpEntity<>(request, headers), EmpleadoResponse.class);

        ResponseEntity<List<EmpleadoResponse>> response = testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/empleados", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        List<EmpleadoResponse> body = response.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertFalse(body.isEmpty());
    }
}
