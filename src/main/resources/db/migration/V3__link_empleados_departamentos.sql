ALTER TABLE empleados
    ADD COLUMN IF NOT EXISTS departamento_clave VARCHAR(30);

UPDATE empleados
SET departamento_clave = 'SIN_DEPTO'
WHERE departamento_clave IS NULL;

ALTER TABLE empleados
    ALTER COLUMN departamento_clave SET NOT NULL;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.table_constraints
        WHERE constraint_name = 'fk_empleados_departamentos'
          AND table_name = 'empleados'
    ) THEN
        ALTER TABLE empleados
            ADD CONSTRAINT fk_empleados_departamentos
                FOREIGN KEY (departamento_clave)
                REFERENCES departamentos(clave);
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_empleados_departamento_clave
    ON empleados (departamento_clave);
