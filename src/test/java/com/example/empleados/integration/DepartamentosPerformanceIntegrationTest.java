package com.example.empleados.integration;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.empleados.dto.DepartamentoRequest;

class DepartamentosPerformanceIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldKeepP95UnderTwoSecondsForPagedDepartmentQueries() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        for (int i = 1; i <= 40; i++) {
            DepartamentoRequest request = new DepartamentoRequest();
            request.setClave(String.format("DP%02d", i));
            request.setNombre("Departamento " + i);

            testRestTemplate.withBasicAuth("admin", "admin123")
                    .exchange("/api/v2/departamentos", HttpMethod.POST,
                            new HttpEntity<>(request, headers), Object.class);
        }

        int runs = 20;
        long[] durationsMs = new long[runs];

        for (int i = 0; i < runs; i++) {
            long start = System.nanoTime();
            ResponseEntity<String> response = testRestTemplate.withBasicAuth("admin", "admin123")
                    .exchange("/api/v2/departamentos?page=0&size=20", HttpMethod.GET,
                            HttpEntity.EMPTY, String.class);
            long end = System.nanoTime();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            durationsMs[i] = (end - start) / 1_000_000;
        }

        Arrays.sort(durationsMs);
        int p95Index = (int) Math.ceil(runs * 0.95) - 1;
        long p95Ms = durationsMs[Math.max(p95Index, 0)];

        System.out.println("PERF_EVIDENCE p95_ms=" + p95Ms + " samples_ms=" + Arrays.toString(durationsMs));

        assertTrue(p95Ms < 2_000, "p95 debe ser menor a 2000ms, valor observado: " + p95Ms + "ms");
    }
}
