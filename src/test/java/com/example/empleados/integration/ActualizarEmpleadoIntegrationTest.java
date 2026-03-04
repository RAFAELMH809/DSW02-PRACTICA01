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

class ActualizarEmpleadoIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldUpdateWithoutChangingKey() {
        EmpleadoRequest createRequest = new EmpleadoRequest();
        createRequest.setNombre("Jose");
        createRequest.setDireccion("Calle C");
        createRequest.setTelefono("555-333");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        EmpleadoResponse created = testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/empleados", HttpMethod.POST, new HttpEntity<>(createRequest, headers), EmpleadoResponse.class)
                .getBody();
        Assertions.assertNotNull(created);

        EmpleadoRequest updateRequest = new EmpleadoRequest();
        updateRequest.setNombre("Jose Updated");
        updateRequest.setDireccion("Calle C2");
        updateRequest.setTelefono("555-334");

        ResponseEntity<EmpleadoResponse> updatedResponse = testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/empleados/" + created.getClave(), HttpMethod.PUT, new HttpEntity<>(updateRequest, headers), EmpleadoResponse.class);

        Assertions.assertTrue(updatedResponse.getStatusCode().is2xxSuccessful());
        EmpleadoResponse updatedBody = updatedResponse.getBody();
        Assertions.assertNotNull(updatedBody);
        Assertions.assertEquals(created.getClave(), updatedBody.getClave());
    }
}
