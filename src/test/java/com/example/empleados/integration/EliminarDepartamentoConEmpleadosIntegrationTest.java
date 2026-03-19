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

class EliminarDepartamentoConEmpleadosIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldFailDeleteWhenDepartamentoHasEmployees() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        DepartamentoRequest createDept = new DepartamentoRequest();
        createDept.setClave("OPS");
        createDept.setNombre("Operaciones");
        testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos", HttpMethod.POST,
                        new HttpEntity<>(createDept, headers), Object.class);

        EmpleadoRequest empleadoRequest = new EmpleadoRequest();
        empleadoRequest.setNombre("Ana");
        empleadoRequest.setDireccion("Calle 1");
        empleadoRequest.setTelefono("555-000");
        empleadoRequest.setDepartamentoClave("OPS");

        testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/empleados", HttpMethod.POST,
                        new HttpEntity<>(empleadoRequest, headers), Object.class);

        ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "admin123")
                .exchange("/api/v2/departamentos/OPS", HttpMethod.DELETE, HttpEntity.EMPTY, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
