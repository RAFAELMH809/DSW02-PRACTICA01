# Tasks: CRUD de Departamentos Vinculado a Empleados

**Input**: Design documents from `/specs/003-crud-departamentos-empleados/`
**Prerequisites**: plan.md (required), spec.md (required), research.md, data-model.md, contracts/, quickstart.md

**Tests**: Se incluyen tareas de pruebas porque `QUAL-001` exige cobertura unitaria e integracion para CRUD, seguridad y reglas de relacion con empleados.

**Organization**: Las tareas se agrupan por historia de usuario para permitir implementacion y validacion independientes.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Puede ejecutarse en paralelo (archivos distintos, sin dependencia directa)
- **[Story]**: Historia de usuario (`[US1]`, `[US2]`, `[US3]`, `[US4]`)
- Todas las tareas incluyen rutas de archivo especificas

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar artefactos base del feature y su documentacion tecnica.

- [ ] T001 Crear contrato inicial de departamentos en `specs/003-crud-departamentos-empleados/contracts/departamentos.openapi.yaml`
- [ ] T002 [P] Preparar guion de ejecucion del feature en `specs/003-crud-departamentos-empleados/quickstart.md`
- [ ] T003 [P] Consolidar decisiones tecnicas del feature en `specs/003-crud-departamentos-empleados/research.md`
- [ ] T004 [P] Consolidar modelo de datos en `specs/003-crud-departamentos-empleados/data-model.md`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura obligatoria para habilitar todas las historias.

**CRITICAL**: Ninguna historia inicia hasta completar esta fase.

- [ ] T005 Crear tabla `departamentos` y constraints base en `src/main/resources/db/migration/V2__create_departamentos_table.sql`
- [ ] T006 Crear migracion de vinculo obligatorio empleados-departamentos con backfill `SIN_DEPTO` en `src/main/resources/db/migration/V3__link_empleados_departamentos.sql`
- [ ] T007 [P] Crear entidad `Departamento` en `src/main/java/com/example/empleados/domain/Departamento.java`
- [ ] T008 [P] Actualizar entidad `Empleado` para referencia obligatoria a departamento en `src/main/java/com/example/empleados/domain/Empleado.java`
- [ ] T009 [P] Crear repositorio `DepartamentoRepository` en `src/main/java/com/example/empleados/repository/DepartamentoRepository.java`
- [ ] T010 [P] Crear DTOs de departamento en `src/main/java/com/example/empleados/dto/DepartamentoRequest.java` y `src/main/java/com/example/empleados/dto/DepartamentoResponse.java`
- [ ] T011 [P] Crear DTO de actualizacion de departamento en `src/main/java/com/example/empleados/dto/DepartamentoUpdateRequest.java`
- [ ] T012 [P] Crear mapper `DepartamentoMapper` en `src/main/java/com/example/empleados/service/DepartamentoMapper.java`
- [ ] T013 Crear servicio de reglas compartidas (`clave`, `SIN_DEPTO`, eliminacion) en `src/main/java/com/example/empleados/service/DepartamentoRulesValidator.java`
- [ ] T014 Ajustar request/response de empleados para `departamentoClave` obligatorio en `src/main/java/com/example/empleados/dto/EmpleadoRequest.java` y `src/main/java/com/example/empleados/dto/EmpleadoResponse.java`
- [ ] T015 Configurar manejo de errores de negocio para duplicidad como `409` en `src/main/java/com/example/empleados/controller/GlobalExceptionHandler.java`

**Checkpoint**: Fundacion completa; historias habilitadas.

---

## Phase 3: User Story 1 - Registrar departamentos (Priority: P1) 🎯 MVP

**Goal**: Crear departamentos con `clave` y `nombre` validos, con clave inmutable y unicidad case-insensitive.

**Independent Test**: Crear departamento valido devuelve `201`; clave duplicada (incluyendo case-insensitive) devuelve `409`.

### Tests for User Story 1

- [ ] T016 [P] [US1] Crear contrato de `POST /api/v2/departamentos` en `src/test/java/com/example/empleados/contract/CrearDepartamentoContractTest.java`
- [ ] T017 [P] [US1] Crear prueba de integracion de alta exitosa en `src/test/java/com/example/empleados/integration/CrearDepartamentoIntegrationTest.java`
- [ ] T018 [P] [US1] Crear prueba de duplicidad case-insensitive en `src/test/java/com/example/empleados/integration/CrearDepartamentoClaveDuplicadaIntegrationTest.java`
- [ ] T019 [P] [US1] Crear prueba unitaria de formato de clave `^[A-Z0-9_]{2,20}$` en `src/test/java/com/example/empleados/unit/DepartamentoValidationTest.java`

### Implementation for User Story 1

- [ ] T020 [US1] Crear interfaz `DepartamentoService` en `src/main/java/com/example/empleados/service/DepartamentoService.java`
- [ ] T021 [US1] Implementar alta de departamento en `src/main/java/com/example/empleados/service/impl/DepartamentoServiceImpl.java`
- [ ] T022 [US1] Implementar endpoint `POST /api/v2/departamentos` en `src/main/java/com/example/empleados/controller/DepartamentoController.java`
- [ ] T023 [US1] Agregar auditoria de creacion (usuario + operacion + timestamp) en `src/main/java/com/example/empleados/service/impl/DepartamentoServiceImpl.java`
- [ ] T024 [US1] Documentar `409 Conflict` por clave duplicada en `specs/003-crud-departamentos-empleados/contracts/departamentos.openapi.yaml`

**Checkpoint**: MVP de departamentos listo y demostrable.

---

## Phase 4: User Story 2 - Consultar y listar departamentos (Priority: P2)

**Goal**: Consultar por clave y listar departamentos con paginacion, orden por defecto `clave ASC`, `size <= 100` y `employeeCount`.

**Independent Test**: `GET` por clave devuelve departamento; listado paginado devuelve metadata, orden estable y valida limite de size.

### Tests for User Story 2

- [ ] T025 [P] [US2] Crear contrato de `GET /api/v2/departamentos` paginado en `src/test/java/com/example/empleados/contract/ConsultarDepartamentoContractTest.java`
- [ ] T026 [P] [US2] Crear prueba de consulta por clave existente en `src/test/java/com/example/empleados/integration/ObtenerDepartamentoIntegrationTest.java`
- [ ] T027 [P] [US2] Crear prueba de consulta por clave inexistente (`404`) en `src/test/java/com/example/empleados/integration/ObtenerDepartamentoNoEncontradoIntegrationTest.java`
- [ ] T028 [P] [US2] Crear prueba de listado paginado y orden `clave ASC` en `src/test/java/com/example/empleados/integration/ListarDepartamentosIntegrationTest.java`
- [ ] T029 [P] [US2] Crear prueba de validacion de `size > 100` en `src/test/java/com/example/empleados/integration/ListarDepartamentosSizeInvalidoIntegrationTest.java`
- [ ] T030 [P] [US2] Crear prueba de acceso sin token (`401`) en `src/test/java/com/example/empleados/integration/AccesoDepartamentoSinTokenIntegrationTest.java`
- [ ] T031 [P] [US2] Crear prueba de acceso con token invalido (`401`) en `src/test/java/com/example/empleados/integration/AccesoDepartamentoTokenInvalidoIntegrationTest.java`

### Implementation for User Story 2

- [ ] T032 [US2] Implementar consulta por clave en `src/main/java/com/example/empleados/service/impl/DepartamentoServiceImpl.java`
- [ ] T033 [US2] Implementar listado paginado con orden por defecto `clave ASC` y limite `size <= 100` en `src/main/java/com/example/empleados/service/impl/DepartamentoServiceImpl.java`
- [ ] T034 [US2] Implementar endpoints `GET /api/v2/departamentos` y `GET /api/v2/departamentos/{clave}` en `src/main/java/com/example/empleados/controller/DepartamentoController.java`
- [ ] T035 [US2] Exponer `employeeCount` en mapper y response en `src/main/java/com/example/empleados/service/DepartamentoMapper.java` y `src/main/java/com/example/empleados/dto/DepartamentoResponse.java`
- [ ] T036 [US2] Documentar parametros `page/size`, limite maximo y orden por defecto en `specs/003-crud-departamentos-empleados/contracts/departamentos.openapi.yaml`

**Checkpoint**: Consulta y listado independientes, con reglas de paginacion verificadas.

---

## Phase 5: User Story 3 - Actualizar y eliminar departamentos (Priority: P3)

**Goal**: Actualizar solo `nombre` y eliminar departamentos con reglas de negocio (`SIN_DEPTO` no eliminable, bloqueo con empleados).

**Independent Test**: Actualizar nombre funciona; eliminar con empleados o `SIN_DEPTO` falla; eliminar sin dependencias devuelve `204`.

### Tests for User Story 3

- [ ] T037 [P] [US3] Crear contrato de `PUT/DELETE /api/v2/departamentos/{clave}` en `src/test/java/com/example/empleados/contract/ActualizarEliminarDepartamentoContractTest.java`
- [ ] T038 [P] [US3] Crear prueba de actualizacion de nombre exitosa en `src/test/java/com/example/empleados/integration/ActualizarDepartamentoIntegrationTest.java`
- [ ] T039 [P] [US3] Crear prueba de eliminacion exitosa sin empleados en `src/test/java/com/example/empleados/integration/EliminarDepartamentoIntegrationTest.java`
- [ ] T040 [P] [US3] Crear prueba de eliminacion bloqueada por empleados asociados en `src/test/java/com/example/empleados/integration/EliminarDepartamentoConEmpleadosIntegrationTest.java`
- [ ] T041 [P] [US3] Crear prueba de eliminacion prohibida de `SIN_DEPTO` en `src/test/java/com/example/empleados/integration/EliminarDepartamentoTecnicoIntegrationTest.java`

### Implementation for User Story 3

- [ ] T042 [US3] Implementar actualizacion de nombre (last-write-wins) en `src/main/java/com/example/empleados/service/impl/DepartamentoServiceImpl.java`
- [ ] T043 [US3] Implementar validaciones de eliminacion (`SIN_DEPTO` y empleados asociados) en `src/main/java/com/example/empleados/service/impl/DepartamentoServiceImpl.java`
- [ ] T044 [US3] Implementar endpoints `PUT` y `DELETE` en `src/main/java/com/example/empleados/controller/DepartamentoController.java`
- [ ] T045 [US3] Agregar auditoria de actualizacion/eliminacion en `src/main/java/com/example/empleados/service/impl/DepartamentoServiceImpl.java`
- [ ] T046 [US3] Ajustar contrato OpenAPI para errores de eliminacion y actualizacion en `specs/003-crud-departamentos-empleados/contracts/departamentos.openapi.yaml`

**Checkpoint**: CRUD de departamentos completo y consistente.

---

## Phase 6: User Story 4 - Gestionar empleados con departamento obligatorio (Priority: P3)

**Goal**: Integrar empleados con `departamentoClave` obligatoria, validando existencia de departamento en create/update.

**Independent Test**: Crear/actualizar empleado con departamento valido funciona; sin departamento o inexistente falla; migracion asigna historicos a `SIN_DEPTO`.

### Tests for User Story 4

- [ ] T047 [P] [US4] Crear prueba de alta de empleado con departamento valido en `src/test/java/com/example/empleados/integration/CrearEmpleadoConDepartamentoIntegrationTest.java`
- [ ] T048 [P] [US4] Crear prueba de alta de empleado sin departamento en `src/test/java/com/example/empleados/integration/CrearEmpleadoSinDepartamentoIntegrationTest.java`
- [ ] T049 [P] [US4] Crear prueba de alta de empleado con departamento inexistente en `src/test/java/com/example/empleados/integration/CrearEmpleadoDepartamentoInexistenteIntegrationTest.java`
- [ ] T050 [P] [US4] Crear prueba de actualizacion de empleado con nuevo departamento valido en `src/test/java/com/example/empleados/integration/ActualizarEmpleadoDepartamentoIntegrationTest.java`
- [ ] T051 [P] [US4] Crear prueba de migracion de historicos a `SIN_DEPTO` en `src/test/java/com/example/empleados/integration/MigracionEmpleadoSinDepartamentoIntegrationTest.java`

### Implementation for User Story 4

- [ ] T052 [US4] Actualizar mapeo de `departamentoClave` en `src/main/java/com/example/empleados/service/EmpleadoMapper.java`
- [ ] T053 [US4] Validar existencia de departamento en create/update de empleados en `src/main/java/com/example/empleados/service/impl/EmpleadoServiceImpl.java`
- [ ] T054 [US4] Ajustar controlador de empleados para contrato actualizado en `src/main/java/com/example/empleados/controller/EmpleadoController.java`
- [ ] T055 [US4] Exponer `departamentoClave` en respuestas de empleados en `src/main/java/com/example/empleados/dto/EmpleadoResponse.java`
- [ ] T056 [US4] Documentar cambios de empleados (`departamentoClave` obligatoria) en `specs/003-crud-departamentos-empleados/contracts/departamentos.openapi.yaml`

**Checkpoint**: Integracion empleados-departamentos obligatoria y validada.

---

## Phase 7: Polish & Cross-Cutting Concerns

**Purpose**: Cierre de calidad, evidencia de NFR y validacion operativa final.

- [ ] T057 [P] Actualizar checklist de calidad del feature en `specs/003-crud-departamentos-empleados/checklists/requirements.md`
- [ ] T058 [P] Generar evidencia de auditoria de escrituras en `specs/003-crud-departamentos-empleados/checklists/audit.md`
- [ ] T059 [P] Crear prueba de rendimiento para SC-002 (p95 < 2s) en `src/test/java/com/example/empleados/integration/DepartamentosPerformanceIntegrationTest.java`
- [ ] T060 [P] Publicar evidencia de rendimiento en `specs/003-crud-departamentos-empleados/checklists/performance.md`
- [ ] T061 Ejecutar suite de regresion empleados+departamentos en `src/test/java/com/example/empleados`
- [ ] T062 Ejecutar validacion manual de quickstart en `specs/003-crud-departamentos-empleados/quickstart.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: inicio inmediato.
- **Phase 2 (Foundational)**: depende de Setup y bloquea todas las historias.
- **Phase 3 (US1)**: depende de Foundational.
- **Phase 4 (US2)**: depende de Foundational y del agregado de departamentos.
- **Phase 5 (US3)**: depende de Foundational y del agregado de departamentos.
- **Phase 6 (US4)**: depende de Foundational y del agregado de departamentos.
- **Phase 7 (Polish)**: depende de cierre de historias objetivo.

### User Story Dependencies

- **US1 (P1)**: base del feature; habilita historias posteriores.
- **US2 (P2)**: puede avanzar tras Foundational, pero se valida mejor con datos de US1.
- **US3 (P3)**: depende funcionalmente de US1.
- **US4 (P3)**: depende de US1 y migraciones de Foundational.

### Dependency Graph (Story Order)

- `Setup -> Foundational -> US1 -> (US2 || US3 || US4) -> Polish`

### Within Each User Story

- Pruebas primero (deben fallar antes de implementar).
- DTO/mapper/repository antes de servicio.
- Servicio antes de controlador.
- Contrato OpenAPI sincronizado al cierre de la historia.

### Parallel Opportunities

- Setup paralelizable: `T002`, `T003`, `T004`.
- Foundational paralelizable: `T007`, `T008`, `T009`, `T010`, `T011`, `T012`.
- US1 paralelizable: `T016`, `T017`, `T018`, `T019`.
- US2 paralelizable: `T025`, `T026`, `T027`, `T028`, `T029`, `T030`, `T031`.
- US3 paralelizable: `T037`, `T038`, `T039`, `T040`, `T041`.
- US4 paralelizable: `T047`, `T048`, `T049`, `T050`, `T051`.
- Polish paralelizable: `T057`, `T058`, `T059`, `T060`.

---

## Parallel Example: User Story 2

```bash
Task: "T025 [US2] Contrato de listado/consulta"
Task: "T026 [US2] Integracion obtener por clave"
Task: "T028 [US2] Integracion listado paginado + orden"
Task: "T029 [US2] Integracion size > 100"
```

## Parallel Example: User Story 3

```bash
Task: "T038 [US3] Integracion actualizar nombre"
Task: "T039 [US3] Integracion eliminar sin empleados"
Task: "T040 [US3] Integracion eliminar con empleados"
Task: "T041 [US3] Integracion eliminar SIN_DEPTO"
```

## Parallel Example: User Story 4

```bash
Task: "T047 [US4] Crear empleado con departamento"
Task: "T048 [US4] Crear empleado sin departamento"
Task: "T049 [US4] Crear empleado con departamento inexistente"
Task: "T050 [US4] Actualizar departamento de empleado"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Setup (Phase 1).
2. Completar Foundational (Phase 2).
3. Completar US1 (Phase 3).
4. Validar alta + duplicidad case-insensitive + `409 Conflict`.

### Incremental Delivery

1. Entregar base tecnica (Phase 1 + 2).
2. Entregar US1 (crear departamentos).
3. Entregar US2 (consulta/listado paginado).
4. Entregar US3 (actualizar/eliminar con reglas).
5. Entregar US4 (integracion obligatoria empleados-departamentos).
6. Cerrar con Polish y evidencias.

### Parallel Team Strategy

1. Equipo completo en Setup + Foundational.
2. Luego distribuir por historia:
   - Equipo A: US2
   - Equipo B: US3
   - Equipo C: US4
3. Integrar y validar con regression en Phase 7.
