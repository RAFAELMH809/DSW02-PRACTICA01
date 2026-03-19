package com.example.empleados.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.empleados.domain.Departamento;

public interface DepartamentoRepository extends JpaRepository<Departamento, String> {
    Optional<Departamento> findByClaveIgnoreCase(String clave);
    boolean existsByClaveIgnoreCase(String clave);
}
