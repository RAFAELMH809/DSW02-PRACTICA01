package com.example.empleados.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.empleados.domain.Departamento;
import com.example.empleados.dto.DepartamentoRequest;
import com.example.empleados.dto.DepartamentoResponse;
import com.example.empleados.dto.DepartamentoUpdateRequest;
import com.example.empleados.exception.NotFoundException;
import com.example.empleados.repository.DepartamentoRepository;
import com.example.empleados.repository.EmpleadoRepository;
import com.example.empleados.service.DepartamentoMapper;
import com.example.empleados.service.DepartamentoRulesValidator;
import com.example.empleados.service.DepartamentoService;

@Service
@Transactional
public class DepartamentoServiceImpl implements DepartamentoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartamentoServiceImpl.class);

    private final DepartamentoRepository departamentoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final DepartamentoMapper departamentoMapper;
    private final DepartamentoRulesValidator rulesValidator;

    public DepartamentoServiceImpl(DepartamentoRepository departamentoRepository,
                                   EmpleadoRepository empleadoRepository,
                                   DepartamentoMapper departamentoMapper,
                                   DepartamentoRulesValidator rulesValidator) {
        this.departamentoRepository = departamentoRepository;
        this.empleadoRepository = empleadoRepository;
        this.departamentoMapper = departamentoMapper;
        this.rulesValidator = rulesValidator;
    }

    @Override
    public DepartamentoResponse crear(DepartamentoRequest request) {
        String clave = rulesValidator.normalizeClave(request.getClave());
        rulesValidator.validarClaveUnica(clave);

        Departamento departamento = departamentoMapper.toEntity(request, clave);
        Departamento guardado = departamentoRepository.save(departamento);
        LOGGER.info("AUDIT departamento_creado clave={}", guardado.getClave());
        return mapResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartamentoResponse> listar(Pageable pageable) {
        return departamentoRepository.findAll(pageable).map(this::mapResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartamentoResponse obtenerPorClave(String clave) {
        Departamento departamento = getByClaveOrThrow(clave);
        return mapResponse(departamento);
    }

    @Override
    public DepartamentoResponse actualizar(String clave, DepartamentoUpdateRequest request) {
        Departamento departamento = getByClaveOrThrow(clave);
        departamentoMapper.updateEntity(departamento, request.getNombre());
        Departamento actualizado = departamentoRepository.save(departamento);
        LOGGER.info("AUDIT departamento_actualizado clave={}", actualizado.getClave());
        return mapResponse(actualizado);
    }

    @Override
    public void eliminar(String clave) {
        String claveNormalizada = rulesValidator.normalizeClave(clave);
        Departamento departamento = departamentoRepository.findByClaveIgnoreCase(claveNormalizada)
                .orElseThrow(() -> new NotFoundException("Departamento no encontrado para clave " + claveNormalizada));

        rulesValidator.validarDepartamentoNoTecnico(departamento.getClave());
        rulesValidator.validarSinEmpleadosAsociados(departamento.getClave());

        departamentoRepository.delete(departamento);
        LOGGER.info("AUDIT departamento_eliminado clave={}", departamento.getClave());
    }

    private Departamento getByClaveOrThrow(String clave) {
        String claveNormalizada = rulesValidator.normalizeClave(clave);
        return departamentoRepository.findByClaveIgnoreCase(claveNormalizada)
                .orElseThrow(() -> new NotFoundException("Departamento no encontrado para clave " + claveNormalizada));
    }

    private DepartamentoResponse mapResponse(Departamento departamento) {
        long employeeCount = empleadoRepository.countByDepartamentoClaveIgnoreCase(departamento.getClave());
        return departamentoMapper.toResponse(departamento, employeeCount);
    }
}
