package com.example.empleados.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.empleados.dto.DepartamentoRequest;
import com.example.empleados.dto.EmpleadoRequest;
import com.example.empleados.dto.EmpleadoResponse;

class CrearEmpleadoConDepartamentoIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldCreateEmpleadoWithValidDepartamento() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DepartamentoRequest createDept = new DepartamentoRequest();
        createDept.setClave("QA");
        createDept.setNombre("Calidad");
        testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos", HttpMethod.POST,
                        new HttpEntity<>(createDept, headers), Object.class);

        EmpleadoRequest request = new EmpleadoRequest();
        request.setNombre("Marcos");
        request.setDireccion("Calle QA");
        request.setTelefono("555-454");
        request.setDepartamentoClave("QA");

        ResponseEntity<EmpleadoResponse> response = testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/empleados", HttpMethod.POST,
                        new HttpEntity<>(request, headers), EmpleadoResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("QA", response.getBody().getDepartamentoClave());
    }
}
