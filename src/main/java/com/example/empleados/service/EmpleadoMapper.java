package com.example.empleados.service;

import java.util.Locale;

import org.springframework.stereotype.Component;

import com.example.empleados.domain.Empleado;
import com.example.empleados.dto.EmpleadoRequest;
import com.example.empleados.dto.EmpleadoResponse;

@Component
public class EmpleadoMapper {

    public Empleado toEntity(EmpleadoRequest request) {
        Empleado empleado = new Empleado();
        empleado.setNombre(request.getNombre().trim());
        empleado.setDireccion(request.getDireccion().trim());
        empleado.setTelefono(request.getTelefono().trim());
        empleado.setDepartamentoClave(request.getDepartamentoClave().trim().toUpperCase(Locale.ROOT));
        return empleado;
    }

    public void updateEntity(Empleado entity, EmpleadoRequest request) {
        entity.setNombre(request.getNombre().trim());
        entity.setDireccion(request.getDireccion().trim());
        entity.setTelefono(request.getTelefono().trim());
        entity.setDepartamentoClave(request.getDepartamentoClave().trim().toUpperCase(Locale.ROOT));
    }

    public EmpleadoResponse toResponse(Empleado empleado) {
        EmpleadoResponse response = new EmpleadoResponse();
        String clave = empleado.getClave();
        if (clave == null && empleado.getId() != null
                && empleado.getId().getClavePrefijo() != null
                && empleado.getId().getClaveNumero() != null) {
            clave = empleado.getId().getClavePrefijo() + empleado.getId().getClaveNumero();
        }
        response.setClave(clave);
        response.setNombre(empleado.getNombre());
        response.setDireccion(empleado.getDireccion());
        response.setTelefono(empleado.getTelefono());
        response.setDepartamentoClave(empleado.getDepartamentoClave());
        return response;
    }
}
