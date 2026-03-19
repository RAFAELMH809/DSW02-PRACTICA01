# Phase 0 Research - CRUD de Departamentos Vinculado a Empleados

## Decision 1: Identidad funcional de Departamento
- Decision: La entidad Departamento se identifica funcionalmente por `clave`, con comparacion case-insensitive e inmutabilidad despues de crear.
- Rationale: Evita ambiguedad de identidad (`RH` vs `rh`) y reduce errores operativos en integracion con empleados.
- Alternatives considered:
  - Clave mutable: descartado por riesgo de romper referencias historicas.
  - Comparacion case-sensitive: descartado por duplicados semanticamente equivalentes.

## Decision 2: Integracion Empleado-Departamento
- Decision: Todo empleado debe referenciar exactamente un departamento por `departamentoClave` (relacion N:1 obligatoria).
- Rationale: Cumple reglas de dominio y evita empleados huerfanos.
- Alternatives considered:
  - Permitir empleados sin departamento: descartado por inconsistencia de dominio.
  - Referencia por ID interno: descartado para mantener lenguaje de negocio por clave.

## Decision 3: Estrategia de migracion de datos existentes
- Decision: Crear departamento tecnico `SIN_DEPTO` y asignarlo en migracion a empleados sin departamento antes de imponer obligatoriedad.
- Rationale: Permite evolucion segura del esquema sin bloquear despliegues por datos legacy.
- Alternatives considered:
  - Fallar migracion si hay nulos: descartado por alto riesgo operativo.
  - Permitir NULL temporalmente: descartado por deuda tecnica y reglas ambiguas.

## Decision 4: Politica de eliminacion
- Decision: Solo se puede eliminar un departamento sin empleados asociados y nunca se permite eliminar `SIN_DEPTO`.
- Rationale: Protege integridad referencial y conserva fallback tecnico de migracion.
- Alternatives considered:
  - Permitir eliminar `SIN_DEPTO`: descartado por riesgo de inconsistencia historica.

## Decision 5: Seguridad y autorizacion
- Decision: Endpoints de departamentos usan el mismo modelo de empleados: Bearer token valido, sin rol especial adicional.
- Rationale: Reutiliza politica vigente y minimiza complejidad de autorizacion.
- Alternatives considered:
  - Rol ADMIN exclusivo: descartado por no ser requisito actual del dominio.

## Decision 6: Respuesta de consulta/listado
- Decision: La representacion de relacion con empleados se expone como `employeeCount`.
- Rationale: Entrega visibilidad de uso del departamento sin sobrecargar payload ni acoplar DTOs.
- Alternatives considered:
  - Lista completa de empleados: descartado por peso y acoplamiento.
  - No exponer relacion: descartado por necesidad funcional de contexto.

## Decision 7: Auditoria y observabilidad
- Decision: Registrar en logs operaciones de escritura (crear/actualizar/eliminar) con usuario, operacion y timestamp, con verificacion en pruebas.
- Rationale: Cubre trazabilidad operativa y criterio de exito de auditoria.
- Alternatives considered:
  - Sin auditoria: descartado por incumplir FR-015/SC-005.
  - Auditoria persistida en tabla dedicada: pospuesta por alcance.

## Decision 8: Concurrencia y rendimiento
- Decision: Politica de actualizacion last-write-wins (sin optimistic locking).
- Rationale: Menor complejidad para alcance actual.
- Alternatives considered:
  - Optimistic locking: pospuesto por no ser requisito obligatorio.

- Decision: Objetivo de rendimiento aplica a consulta/listado paginado (p95 < 2s); escrituras sin target explicito.
- Rationale: Alineado con NFR acordados.
