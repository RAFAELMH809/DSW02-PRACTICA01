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

class EliminarEmpleadoIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldDeleteEmpleado() {
        EmpleadoRequest createRequest = new EmpleadoRequest();
        createRequest.setNombre("Laura");
        createRequest.setDireccion("Calle D");
        createRequest.setTelefono("555-444");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        EmpleadoResponse created = testRestTemplate.withBasicAuth("admin", "admin123")
            .exchange("/api/v2/empleados", HttpMethod.POST, new HttpEntity<>(createRequest, headers), EmpleadoResponse.class)
                .getBody();
        Assertions.assertNotNull(created);

        ResponseEntity<Void> deleteResponse = testRestTemplate.withBasicAuth("admin", "admin123")
            .exchange("/api/v2/empleados/" + created.getClave(), HttpMethod.DELETE, null, Void.class);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
    }
}
