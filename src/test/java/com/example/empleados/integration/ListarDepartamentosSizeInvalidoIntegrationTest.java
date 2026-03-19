package com.example.empleados.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;

class ListarDepartamentosSizeInvalidoIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldRejectWhenSizeIsGreaterThan100() {
        ResponseEntity<JsonNode> response = testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos?page=0&size=101", HttpMethod.GET, HttpEntity.EMPTY, JsonNode.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
