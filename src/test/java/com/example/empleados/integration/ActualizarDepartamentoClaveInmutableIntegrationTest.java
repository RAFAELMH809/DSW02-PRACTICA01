package com.example.empleados.integration;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.empleados.dto.DepartamentoRequest;
import com.fasterxml.jackson.databind.JsonNode;

class ActualizarDepartamentoClaveInmutableIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldKeepClaveImmutableWhenUpdatingDepartamento() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DepartamentoRequest createRequest = new DepartamentoRequest();
        createRequest.setClave("COM");
        createRequest.setNombre("Compras");

        testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos", HttpMethod.POST,
                        new HttpEntity<>(createRequest, headers), Object.class);

        String updatePayload = """
                {
                  \"nombre\": \"Compras Globales\",
                  \"clave\": \"NUEVA\"
                }
                """;

        ResponseEntity<JsonNode> updateResponse = testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos/COM", HttpMethod.PUT,
                        new HttpEntity<>(updatePayload, headers), JsonNode.class);

        if (updateResponse.getStatusCode() == HttpStatus.OK) {
            JsonNode body = Objects.requireNonNull(updateResponse.getBody(), "Response body must not be null");
            assertEquals("COM", body.get("clave").asText());
            assertEquals("Compras Globales", body.get("nombre").asText());
        } else {
            assertEquals(HttpStatus.BAD_REQUEST, updateResponse.getStatusCode());
            JsonNode body = Objects.requireNonNull(updateResponse.getBody(), "Response body must not be null");
            assertTrue(body.path("message").asText().toLowerCase().contains("clave"));
        }
    }
}
