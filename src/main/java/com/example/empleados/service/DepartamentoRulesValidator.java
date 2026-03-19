package com.example.empleados.service;

import java.util.Locale;

import org.springframework.stereotype.Component;

import com.example.empleados.exception.BadRequestException;
import com.example.empleados.exception.ConflictException;
import com.example.empleados.repository.DepartamentoRepository;
import com.example.empleados.repository.EmpleadoRepository;

@Component
public class DepartamentoRulesValidator {

    public static final String SIN_DEPTO = "SIN_DEPTO";

    private final DepartamentoRepository departamentoRepository;
    private final EmpleadoRepository empleadoRepository;

    public DepartamentoRulesValidator(DepartamentoRepository departamentoRepository,
                                      EmpleadoRepository empleadoRepository) {
        this.departamentoRepository = departamentoRepository;
        this.empleadoRepository = empleadoRepository;
    }

    public String normalizeClave(String clave) {
        if (clave == null || clave.trim().isEmpty()) {
            throw new BadRequestException("La clave del departamento es obligatoria");
        }
        return clave.trim().toUpperCase(Locale.ROOT);
    }

    public void validarClaveUnica(String claveNormalizada) {
        if (departamentoRepository.existsByClaveIgnoreCase(claveNormalizada)) {
            throw new ConflictException("Ya existe un departamento con clave " + claveNormalizada);
        }
    }

    public void validarDepartamentoNoTecnico(String claveNormalizada) {
        if (SIN_DEPTO.equalsIgnoreCase(claveNormalizada)) {
            throw new BadRequestException("No se permite eliminar el departamento SIN_DEPTO");
        }
    }

    public void validarSinEmpleadosAsociados(String claveNormalizada) {
        if (empleadoRepository.existsByDepartamentoClaveIgnoreCase(claveNormalizada)) {
            throw new BadRequestException("No se puede eliminar un departamento con empleados asociados");
        }
    }

    public void validarDepartamentoExiste(String claveNormalizada) {
        if (!departamentoRepository.existsByClaveIgnoreCase(claveNormalizada)) {
            throw new BadRequestException("No existe el departamento con clave " + claveNormalizada);
        }
    }
}
