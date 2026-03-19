package com.example.empleados.service;

import org.springframework.stereotype.Component;

import com.example.empleados.domain.Departamento;
import com.example.empleados.dto.DepartamentoRequest;
import com.example.empleados.dto.DepartamentoResponse;

@Component
public class DepartamentoMapper {

    public Departamento toEntity(DepartamentoRequest request, String normalizedClave) {
        Departamento departamento = new Departamento();
        departamento.setClave(normalizedClave);
        departamento.setNombre(request.getNombre().trim());
        return departamento;
    }

    public void updateEntity(Departamento departamento, String nombre) {
        departamento.setNombre(nombre.trim());
    }

    public DepartamentoResponse toResponse(Departamento departamento, long employeeCount) {
        DepartamentoResponse response = new DepartamentoResponse();
        response.setClave(departamento.getClave());
        response.setNombre(departamento.getNombre());
        response.setEmployeeCount(employeeCount);
        return response;
    }
}
