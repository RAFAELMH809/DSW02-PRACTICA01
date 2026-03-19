package com.example.empleados.integration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.empleados.dto.DepartamentoRequest;
import com.fasterxml.jackson.databind.JsonNode;

class ListarDepartamentosRecorridoPaginasIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldTraverseAllPagesWithoutDuplicatesOrOmissions() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String[] claves = { "ADM", "FIN", "LEG", "MKT", "OPS" };
        for (String clave : claves) {
            DepartamentoRequest request = new DepartamentoRequest();
            request.setClave(clave);
            request.setNombre("Departamento " + clave);

            testRestTemplate.withBasicAuth("admin", "admin123")
                    .exchange("/api/v2/departamentos", HttpMethod.POST,
                            new HttpEntity<>(request, headers), Object.class);
        }

        Set<String> collectedUnique = new HashSet<>();
        List<String> collectedOrdered = new ArrayList<>();

        int totalElements = -1;
        int page = 0;
        int size = 2;

        while (true) {
            ResponseEntity<JsonNode> response = testRestTemplate.withBasicAuth("admin", "admin123")
                    .exchange("/api/v2/departamentos?page=" + page + "&size=" + size,
                            HttpMethod.GET, HttpEntity.EMPTY, JsonNode.class);

            JsonNode body = response.getBody();
                assertNotNull(body);
            JsonNode content = body.get("content");
                assertNotNull(content);

            if (totalElements < 0) {
                totalElements = body.get("totalElements").asInt();
            }

            for (JsonNode item : content) {
                String clave = item.get("clave").asText();
                collectedOrdered.add(clave);
                collectedUnique.add(clave);
            }

            int totalPages = body.get("totalPages").asInt();
            if (page >= totalPages - 1) {
                break;
            }
            page++;
        }

        assertEquals(totalElements, collectedUnique.size(), "No debe haber omisiones en el recorrido paginado");
        assertEquals(collectedOrdered.size(), collectedUnique.size(), "No debe haber duplicados entre paginas");

        for (String clave : claves) {
            assertTrue(collectedUnique.contains(clave), "Falta la clave esperada: " + clave);
        }
    }
}
