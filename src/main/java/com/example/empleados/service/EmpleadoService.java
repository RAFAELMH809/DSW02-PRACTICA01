package com.example.empleados.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.empleados.dto.EmpleadoRequest;
import com.example.empleados.dto.EmpleadoResponse;

public interface EmpleadoService {
    EmpleadoResponse crear(EmpleadoRequest request);
    Page<EmpleadoResponse> listar(Pageable pageable);
    EmpleadoResponse obtenerPorClave(String clave);
    EmpleadoResponse actualizar(String clave, EmpleadoRequest request);
    void eliminar(String clave);
}
