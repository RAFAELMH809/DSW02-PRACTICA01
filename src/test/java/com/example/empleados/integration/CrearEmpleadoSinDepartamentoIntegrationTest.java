package com.example.empleados.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.empleados.dto.EmpleadoRequest;

class CrearEmpleadoSinDepartamentoIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldFailWhenDepartamentoIsMissing() {
        EmpleadoRequest request = new EmpleadoRequest();
        request.setNombre("Luis");
        request.setDireccion("Calle 9");
        request.setTelefono("555-909");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/empleados", HttpMethod.POST,
                        new HttpEntity<>(request, headers), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
