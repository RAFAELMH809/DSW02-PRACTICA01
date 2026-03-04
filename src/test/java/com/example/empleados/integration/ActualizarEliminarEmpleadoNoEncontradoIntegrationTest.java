package com.example.empleados.integration;

import com.example.empleados.dto.EmpleadoRequest;
import com.example.empleados.dto.ErrorResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

class ActualizarEliminarEmpleadoNoEncontradoIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldReturnBadRequestForInvalidClaveOnUpdateAndDelete() {
        EmpleadoRequest request = new EmpleadoRequest();
        request.setNombre("Carlos");
        request.setDireccion("Calle E");
        request.setTelefono("555-555");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<ErrorResponse> updateResponse = testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/empleados/INVALID", HttpMethod.PUT, new HttpEntity<>(request, headers), ErrorResponse.class);

        ResponseEntity<ErrorResponse> deleteResponse = testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/empleados/INVALID", HttpMethod.DELETE, null, ErrorResponse.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, updateResponse.getStatusCode());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, deleteResponse.getStatusCode());
    }

    @Test
    void shouldReturnNotFoundForMissingEmpleadoOnUpdateAndDelete() {
        EmpleadoRequest request = new EmpleadoRequest();
        request.setNombre("Carlos");
        request.setDireccion("Calle E");
        request.setTelefono("555-555");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<ErrorResponse> updateResponse = testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/empleados/EMP-9999", HttpMethod.PUT, new HttpEntity<>(request, headers), ErrorResponse.class);

        ResponseEntity<ErrorResponse> deleteResponse = testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/empleados/EMP-9999", HttpMethod.DELETE, null, ErrorResponse.class);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, updateResponse.getStatusCode());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, deleteResponse.getStatusCode());
    }
}
