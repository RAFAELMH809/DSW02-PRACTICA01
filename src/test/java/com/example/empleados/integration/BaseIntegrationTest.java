package com.example.empleados.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.example.empleados.repository.EmpleadoRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Autowired
    protected EmpleadoRepository empleadoRepository;

    @BeforeEach
    void cleanDb() {
        empleadoRepository.deleteAll();
    }
}
