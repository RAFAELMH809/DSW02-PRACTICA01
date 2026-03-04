CREATE SEQUENCE IF NOT EXISTS empleados_clave_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS empleados (
    clave_prefijo VARCHAR(4) NOT NULL DEFAULT 'EMP-',
    clave_numero BIGINT NOT NULL DEFAULT nextval('empleados_clave_seq'),
    clave VARCHAR(20) GENERATED ALWAYS AS (clave_prefijo || clave_numero::text) STORED,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(100) NOT NULL,
    telefono VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_empleados PRIMARY KEY (clave_prefijo, clave_numero),
    CONSTRAINT uk_empleados_clave UNIQUE (clave)
);
