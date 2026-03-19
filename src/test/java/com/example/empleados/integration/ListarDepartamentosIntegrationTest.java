package com.example.empleados.integration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.empleados.dto.DepartamentoRequest;
import com.fasterxml.jackson.databind.JsonNode;

class ListarDepartamentosIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldListDepartamentosWithPagination() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DepartamentoRequest request1 = new DepartamentoRequest();
        request1.setClave("RH");
        request1.setNombre("Recursos Humanos");

        DepartamentoRequest request2 = new DepartamentoRequest();
        request2.setClave("TI");
        request2.setNombre("Tecnologia");

        testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos", HttpMethod.POST, new HttpEntity<>(request1, headers), Object.class);
        testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos", HttpMethod.POST, new HttpEntity<>(request2, headers), Object.class);

        ResponseEntity<JsonNode> response = testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos?page=0&size=10", HttpMethod.GET, HttpEntity.EMPTY, JsonNode.class);

        JsonNode content = response.getBody().get("content");
        assertFalse(content.isEmpty());
    }
}
