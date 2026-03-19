package com.example.empleados.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class MigracionEmpleadoSinDepartamentoIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldKeepSinDeptoAndPreventNullDepartamentoForEmpleados() {
        Integer sinDeptoCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM departamentos WHERE clave = 'SIN_DEPTO'", Integer.class);
        assertEquals(1, sinDeptoCount);

        String isNullable = jdbcTemplate.queryForObject(
                """
                SELECT is_nullable
                FROM information_schema.columns
                WHERE table_name = 'empleados' AND column_name = 'departamento_clave'
                """,
                String.class);
        assertEquals("NO", isNullable);

        Integer empleadosSinDepartamento = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM empleados WHERE departamento_clave IS NULL", Integer.class);
        assertEquals(0, empleadosSinDepartamento);
    }
}
