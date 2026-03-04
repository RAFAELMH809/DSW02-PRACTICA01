package com.example.empleados.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.empleados.domain.Empleado;
import com.example.empleados.domain.EmpleadoId;

public interface EmpleadoRepository extends JpaRepository<Empleado, EmpleadoId> {
    Optional<Empleado> findByClave(String clave);
    boolean existsByClave(String clave);
}
