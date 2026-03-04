package com.example.empleados.service.impl;

import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.example.empleados.exception.BadRequestException;
import com.example.empleados.repository.SequenceRepository;
import com.example.empleados.service.KeyGeneratorService;

@Service
public class KeyGeneratorServiceImpl implements KeyGeneratorService {

    private static final Pattern CLAVE_PATTERN = Pattern.compile("^EMP-\\d+$");

    private final SequenceRepository sequenceRepository;

    public KeyGeneratorServiceImpl(SequenceRepository sequenceRepository) {
        this.sequenceRepository = sequenceRepository;
    }

    @Override
    public String generarClave() {
        Long sequenceValue = sequenceRepository.nextEmpleadoClave();
        return "EMP-" + sequenceValue;
    }

    @Override
    public void validarFormato(String clave) {
        if (clave == null || !CLAVE_PATTERN.matcher(clave).matches()) {
            throw new BadRequestException("Formato de clave inválido. Use EMP-<autonumérico>");
        }
    }
}
