package com.example.empleados.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;

class AccesoDepartamentoSinTokenIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldReturnUnauthorizedWhenNoCredentialsAreProvided() {
        ResponseEntity<JsonNode> response = testRestTemplate.exchange(
                "/api/v2/departamentos?page=0&size=10", HttpMethod.GET, HttpEntity.EMPTY, JsonNode.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
