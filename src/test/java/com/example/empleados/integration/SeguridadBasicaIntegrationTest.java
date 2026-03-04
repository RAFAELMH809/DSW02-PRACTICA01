package com.example.empleados.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class SeguridadBasicaIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldReturnUnauthorizedWithoutCredentials() {
        ResponseEntity<String> response = testRestTemplate.exchange("/api/v2/empleados", HttpMethod.GET, null, String.class);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
