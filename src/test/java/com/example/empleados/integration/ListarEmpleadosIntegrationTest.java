package com.example.empleados.integration;

import com.example.empleados.dto.EmpleadoRequest;
import com.example.empleados.dto.EmpleadoResponse;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

class ListarEmpleadosIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldListEmpleados() {
        EmpleadoRequest request = new EmpleadoRequest();
        request.setNombre("Maria");
        request.setDireccion("Calle B");
        request.setTelefono("555-222");
        request.setDepartamentoClave("SIN_DEPTO");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        testRestTemplate.withBasicAuth("admin", "admin123")
            .exchange("/api/v2/empleados", HttpMethod.POST, new HttpEntity<>(request, headers), EmpleadoResponse.class);

        ResponseEntity<JsonNode> response = testRestTemplate.withBasicAuth("admin", "admin123")
            .exchange("/api/v2/empleados?page=0&size=10", HttpMethod.GET, null, JsonNode.class);

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        JsonNode body = response.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertTrue(body.has("content"));
        Assertions.assertFalse(body.get("content").isEmpty());
    }
}



