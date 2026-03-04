package com.example.empleados.integration;

import com.example.empleados.dto.EmpleadoRequest;
import com.example.empleados.dto.EmpleadoResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Set;

class CrearEmpleadoClaveUnicaConcurrenteIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldGenerateUniqueKeysAcrossMultipleCreates() {
        Set<String> claves = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            EmpleadoRequest request = new EmpleadoRequest();
            request.setNombre("Nombre " + i);
            request.setDireccion("Direccion " + i);
            request.setTelefono("Telefono " + i);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<EmpleadoResponse> response = testRestTemplate
                    .withBasicAuth("admin", "admin123")
                    .exchange("/api/v2/empleados", HttpMethod.POST, new HttpEntity<>(request, headers), EmpleadoResponse.class);

            Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
            EmpleadoResponse body = response.getBody();
            Assertions.assertNotNull(body);
            Assertions.assertTrue(claves.add(body.getClave()));
        }
    }
}
