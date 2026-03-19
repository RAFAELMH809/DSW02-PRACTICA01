package com.example.empleados.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.empleados.dto.DepartamentoRequest;
import com.example.empleados.dto.EmpleadoRequest;
import com.example.empleados.dto.EmpleadoResponse;

class ActualizarEmpleadoDepartamentoIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldUpdateEmpleadoDepartamento() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DepartamentoRequest dept1 = new DepartamentoRequest();
        dept1.setClave("A");
        dept1.setNombre("Area A");

        DepartamentoRequest dept2 = new DepartamentoRequest();
        dept2.setClave("B");
        dept2.setNombre("Area B");

        testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos", HttpMethod.POST, new HttpEntity<>(dept1, headers), Object.class);
        testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos", HttpMethod.POST, new HttpEntity<>(dept2, headers), Object.class);

        EmpleadoRequest createRequest = new EmpleadoRequest();
        createRequest.setNombre("Eva");
        createRequest.setDireccion("Calle 10");
        createRequest.setTelefono("555-111");
        createRequest.setDepartamentoClave("A");

        EmpleadoResponse created = testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/empleados", HttpMethod.POST, new HttpEntity<>(createRequest, headers), EmpleadoResponse.class)
                .getBody();

        assertNotNull(created);

        EmpleadoRequest updateRequest = new EmpleadoRequest();
        updateRequest.setNombre("Eva");
        updateRequest.setDireccion("Calle 10");
        updateRequest.setTelefono("555-111");
        updateRequest.setDepartamentoClave("B");

        ResponseEntity<EmpleadoResponse> response = testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/empleados/" + created.getClave(), HttpMethod.PUT,
                        new HttpEntity<>(updateRequest, headers), EmpleadoResponse.class);

        assertEquals("B", response.getBody().getDepartamentoClave());
    }
}
