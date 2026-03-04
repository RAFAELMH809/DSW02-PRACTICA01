package com.example.empleados.service;

public interface KeyGeneratorService {
    String generarClave();
    void validarFormato(String clave);
}
