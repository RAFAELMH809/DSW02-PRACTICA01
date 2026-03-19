package com.example.empleados.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class EliminarDepartamentoTecnicoIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldNotDeleteSinDepto() {
        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos/SIN_DEPTO", HttpMethod.DELETE, HttpEntity.EMPTY, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
