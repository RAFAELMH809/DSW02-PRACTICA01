CREATE TABLE IF NOT EXISTS departamentos (
    clave VARCHAR(30) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_departamentos PRIMARY KEY (clave)
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_departamentos_clave_lower
    ON departamentos (LOWER(clave));

INSERT INTO departamentos (clave, nombre)
VALUES ('SIN_DEPTO', 'Sin Departamento')
ON CONFLICT (clave) DO NOTHING;
