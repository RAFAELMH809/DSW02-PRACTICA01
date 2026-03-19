package com.example.empleados.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.empleados.dto.DepartamentoRequest;
import com.example.empleados.dto.DepartamentoResponse;
import com.example.empleados.dto.DepartamentoUpdateRequest;

public interface DepartamentoService {
    DepartamentoResponse crear(DepartamentoRequest request);
    Page<DepartamentoResponse> listar(Pageable pageable);
    DepartamentoResponse obtenerPorClave(String clave);
    DepartamentoResponse actualizar(String clave, DepartamentoUpdateRequest request);
    void eliminar(String clave);
}
