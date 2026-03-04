package com.example.empleados.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.empleados.domain.Empleado;
import com.example.empleados.domain.EmpleadoId;
import com.example.empleados.dto.EmpleadoRequest;
import com.example.empleados.dto.EmpleadoResponse;
import com.example.empleados.exception.NotFoundException;
import com.example.empleados.repository.EmpleadoRepository;
import com.example.empleados.service.EmpleadoMapper;
import com.example.empleados.service.EmpleadoService;
import com.example.empleados.service.KeyGeneratorService;

@Service
@Transactional
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoMapper empleadoMapper;
    private final KeyGeneratorService keyGeneratorService;

    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository,
                               EmpleadoMapper empleadoMapper,
                               KeyGeneratorService keyGeneratorService) {
        this.empleadoRepository = empleadoRepository;
        this.empleadoMapper = empleadoMapper;
        this.keyGeneratorService = keyGeneratorService;
    }

    @Override
    public EmpleadoResponse crear(EmpleadoRequest request) {
        Empleado empleado = empleadoMapper.toEntity(request);
        String clave = keyGeneratorService.generarClave();
        String[] split = clave.split("-");
        EmpleadoId empleadoId = new EmpleadoId(split[0] + "-", Long.valueOf(split[1]));
        empleado.setId(empleadoId);
        empleadoRepository.save(empleado);
        Empleado guardado = empleadoRepository.findByClave(clave)
                .orElseThrow(() -> new NotFoundException("Empleado no encontrado para clave " + clave));
        return empleadoMapper.toResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmpleadoResponse> listar(Pageable pageable) {
        return empleadoRepository.findAll(pageable)
                .map(empleadoMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public EmpleadoResponse obtenerPorClave(String clave) {
        keyGeneratorService.validarFormato(clave);
        Empleado empleado = empleadoRepository.findByClave(clave)
                .orElseThrow(() -> new NotFoundException("Empleado no encontrado para clave " + clave));
        return empleadoMapper.toResponse(empleado);
    }

    @Override
    public EmpleadoResponse actualizar(String clave, EmpleadoRequest request) {
        keyGeneratorService.validarFormato(clave);
        Empleado empleado = empleadoRepository.findByClave(clave)
                .orElseThrow(() -> new NotFoundException("Empleado no encontrado para clave " + clave));
        empleadoMapper.updateEntity(empleado, request);
        Empleado actualizado = empleadoRepository.save(empleado);
        return empleadoMapper.toResponse(actualizado);
    }

    @Override
    public void eliminar(String clave) {
        keyGeneratorService.validarFormato(clave);
        Empleado empleado = empleadoRepository.findByClave(clave)
                .orElseThrow(() -> new NotFoundException("Empleado no encontrado para clave " + clave));
        empleadoRepository.delete(empleado);
    }
}
