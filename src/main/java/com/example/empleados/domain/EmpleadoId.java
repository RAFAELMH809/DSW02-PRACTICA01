package com.example.empleados.domain;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class EmpleadoId implements Serializable {

    @Column(name = "clave_prefijo", length = 4, nullable = false)
    private String clavePrefijo;

    @Column(name = "clave_numero", nullable = false)
    private Long claveNumero;

    public EmpleadoId() {
    }

    public EmpleadoId(String clavePrefijo, Long claveNumero) {
        this.clavePrefijo = clavePrefijo;
        this.claveNumero = claveNumero;
    }

    public String getClavePrefijo() {
        return clavePrefijo;
    }

    public void setClavePrefijo(String clavePrefijo) {
        this.clavePrefijo = clavePrefijo;
    }

    public Long getClaveNumero() {
        return claveNumero;
    }

    public void setClaveNumero(Long claveNumero) {
        this.claveNumero = claveNumero;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof EmpleadoId empleadoId)) {
            return false;
        }
        return Objects.equals(clavePrefijo, empleadoId.clavePrefijo)
                && Objects.equals(claveNumero, empleadoId.claveNumero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clavePrefijo, claveNumero);
    }
}
