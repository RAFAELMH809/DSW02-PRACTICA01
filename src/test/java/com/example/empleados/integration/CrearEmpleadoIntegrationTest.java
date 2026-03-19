package com.example.empleados.integration;

import com.example.empleados.dto.EmpleadoRequest;
import com.example.empleados.dto.EmpleadoResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

class CrearEmpleadoIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldCreateEmpleadoWithGeneratedKey() {
        EmpleadoRequest request = new EmpleadoRequest();
        request.setNombre("Pedro");
        request.setDireccion("Calle A");
        request.setTelefono("555-111");
        request.setDepartamentoClave("SIN_DEPTO");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<EmpleadoResponse> response = testRestTemplate
                .withBasicAuth("admin", "admin123")
            .exchange("/api/v2/empleados", HttpMethod.POST, new HttpEntity<>(request, headers), EmpleadoResponse.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        EmpleadoResponse body = response.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertNotNull(body.getClave());
        Assertions.assertTrue(body.getClave().matches("^EMP-\\d+$"));
    }
}



