package com.example.empleados.repository;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class SequenceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Long nextEmpleadoClave() {
        return ((Number) entityManager
                .createNativeQuery("SELECT nextval('empleados_clave_seq')")
                .getSingleResult())
                .longValue();
    }
}
