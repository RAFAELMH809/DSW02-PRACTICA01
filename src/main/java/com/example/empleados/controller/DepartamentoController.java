package com.example.empleados.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.empleados.dto.DepartamentoRequest;
import com.example.empleados.dto.DepartamentoResponse;
import com.example.empleados.dto.DepartamentoUpdateRequest;
import com.example.empleados.service.DepartamentoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/departamentos")
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    @PostMapping
    public ResponseEntity<DepartamentoResponse> crear(@Valid @RequestBody DepartamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departamentoService.crear(request));
    }

    @GetMapping
    public Page<DepartamentoResponse> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return departamentoService.listar(PageRequest.of(page, size));
    }

    @GetMapping("/{clave}")
    public DepartamentoResponse obtenerPorClave(@PathVariable String clave) {
        return departamentoService.obtenerPorClave(clave);
    }

    @PutMapping("/{clave}")
    public DepartamentoResponse actualizar(@PathVariable String clave,
                                           @Valid @RequestBody DepartamentoUpdateRequest request) {
        return departamentoService.actualizar(clave, request);
    }

    @DeleteMapping("/{clave}")
    public ResponseEntity<Void> eliminar(@PathVariable String clave) {
        departamentoService.eliminar(clave);
        return ResponseEntity.noContent().build();
    }
}
