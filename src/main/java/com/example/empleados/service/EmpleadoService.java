package com.example.empleados.service;

import java.util.List;

import com.example.empleados.dto.EmpleadoRequest;
import com.example.empleados.dto.EmpleadoResponse;

public interface EmpleadoService {
    EmpleadoResponse crear(EmpleadoRequest request);
    List<EmpleadoResponse> listar();
    EmpleadoResponse obtenerPorClave(String clave);
    EmpleadoResponse actualizar(String clave, EmpleadoRequest request);
    void eliminar(String clave);
}
