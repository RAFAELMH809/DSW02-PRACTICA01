package com.example.empleados.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.empleados.dto.EmpleadoRequest;
import com.example.empleados.dto.EmpleadoResponse;
import com.example.empleados.service.EmpleadoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @PostMapping
    public ResponseEntity<EmpleadoResponse> crear(@Valid @RequestBody EmpleadoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(empleadoService.crear(request));
    }

    @GetMapping
    public List<EmpleadoResponse> listar() {
        return empleadoService.listar();
    }

    @GetMapping("/{clave}")
    public EmpleadoResponse obtenerPorClave(@PathVariable String clave) {
        return empleadoService.obtenerPorClave(clave);
    }

    @PutMapping("/{clave}")
    public EmpleadoResponse actualizar(@PathVariable String clave, @Valid @RequestBody EmpleadoRequest request) {
        return empleadoService.actualizar(clave, request);
    }

    @DeleteMapping("/{clave}")
    public ResponseEntity<Void> eliminar(@PathVariable String clave) {
        empleadoService.eliminar(clave);
        return ResponseEntity.noContent().build();
    }
}
