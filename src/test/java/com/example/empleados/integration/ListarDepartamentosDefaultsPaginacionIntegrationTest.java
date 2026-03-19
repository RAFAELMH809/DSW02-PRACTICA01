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
import com.fasterxml.jackson.databind.JsonNode;

class ListarDepartamentosDefaultsPaginacionIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldApplyDefaultPaginationWhenParamsAreMissing() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DepartamentoRequest request = new DepartamentoRequest();
        request.setClave("QA");
        request.setNombre("Calidad");

        testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos", HttpMethod.POST, new HttpEntity<>(request, headers), Object.class);

        ResponseEntity<JsonNode> response = testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos", HttpMethod.GET, HttpEntity.EMPTY, JsonNode.class);

        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().get("number").asInt());
        assertEquals(10, response.getBody().get("size").asInt());
    }
}
