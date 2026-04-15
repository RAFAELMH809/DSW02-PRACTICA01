# Tasks: CRUD de Departamentos Vinculado a Empleados

**Input**: Design documents from `/specs/003-crud-departamentos-empleados/`
**Prerequisites**: `plan.md` (required), `spec.md` (required), `research.md`, `data-model.md`, `contracts/`, `quickstart.md`

**Tests**: Se incluyen tareas de pruebas porque `QUAL-001` exige cobertura unitaria e integracion para operaciones CRUD y reglas de relacion con empleados.

**Organization**: Las tareas se agrupan por historia de usuario para permitir implementacion y validacion independientes.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Puede ejecutarse en paralelo (archivos distintos, sin dependencias de tareas incompletas)
- **[Story]**: Historia de usuario (`[US1]`, `[US2]`, `[US3]`, `[US4]`)
- Todas las tareas incluyen ruta de archivo especifica

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar artefactos de feature y evidencia base de calidad.

- [ ] T001 Validar consistencia final de metadatos y aclaraciones del feature (sin cambiar requisitos aprobados) en `specs/003-crud-departamentos-empleados/spec.md`
- [ ] T002 [P] Actualizar pasos operativos y smoke del feature en `specs/003-crud-departamentos-empleados/quickstart.md`
- [ ] T003 [P] Crear checklist de aceptacion del feature en `specs/003-crud-departamentos-empleados/checklists/acceptance.md`
- [ ] T004 [P] Crear checklist de entorno reproducible en `specs/003-crud-departamentos-empleados/checklists/environment.md`
- [X] T005 [P] Crear checklist de evidencia E2E frontend en `specs/003-crud-departamentos-empleados/checklists/frontend-auth-e2e.md`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura transversal obligatoria para todas las historias.

**CRITICAL**: Ninguna historia de usuario inicia hasta terminar esta fase.

- [ ] T006 Crear migracion de tabla `departamentos` con columna de estado activo para soft delete en `src/main/resources/db/migration/V2__create_departamentos_table.sql`
- [ ] T007 Crear migracion de backfill `SIN_DEPTO` y FK obligatoria en empleados en `src/main/resources/db/migration/V3__link_empleados_departamentos.sql`
- [ ] T008 [P] Implementar entidad `Departamento` con estado de actividad en `src/main/java/com/example/empleados/domain/Departamento.java`
- [ ] T009 [P] Ajustar entidad `Empleado` para `departamentoClave` obligatorio en `src/main/java/com/example/empleados/domain/Empleado.java`
- [ ] T010 [P] Implementar repositorio con busquedas case-insensitive y filtro de activos en `src/main/java/com/example/empleados/repository/DepartamentoRepository.java`
- [ ] T011 [P] Implementar utilitario de normalizacion de clave en `src/main/java/com/example/empleados/service/ClaveNormalizer.java`
- [ ] T012 [P] Implementar excepciones de dominio de departamentos en `src/main/java/com/example/empleados/exception/DepartamentoException.java`
- [ ] T013 Implementar payload de errores estandar (`code`, `message`, `timestamp`) para `400/404/409` en `src/main/java/com/example/empleados/controller/GlobalExceptionHandler.java`
- [ ] T014 [P] Endurecer proteccion Bearer para endpoints de departamentos en `src/main/java/com/example/empleados/config/SecurityConfig.java`
- [ ] T015 [P] Hacer obligatorio `departamentoClave` en request DTO de empleados en `src/main/java/com/example/empleados/dto/EmpleadoRequest.java`
- [ ] T016 Actualizar contrato principal con reglas de soft delete, `204` y payload de error estandar en `specs/003-crud-departamentos-empleados/contracts/departamentos.openapi.yaml`
- [ ] T017 [P] Mantener contrato de login de departamentos para trazabilidad de Principle VI en `specs/003-crud-departamentos-empleados/contracts/departamentos-autenticacion.openapi.yaml`

**Checkpoint**: Base tecnica completa, historias habilitadas.

---

## Phase 3: User Story 1 - Registrar departamentos (Priority: P1) 🎯 MVP

**Goal**: Crear departamentos validando formato de `clave`, unicidad case-insensitive y seguridad.

**Independent Test**: `POST /api/v2/departamentos` crea con `201`; duplicidad case-insensitive devuelve `409`; payload invalido devuelve `400`; sin token devuelve `401`.

### Tests for User Story 1

- [ ] T018 [P] [US1] Crear prueba de contrato para `POST /api/v2/departamentos` en `src/test/java/com/example/empleados/contract/CrearDepartamentoContractTest.java`
- [ ] T019 [P] [US1] Crear prueba de integracion de alta exitosa en `src/test/java/com/example/empleados/integration/CrearDepartamentoIntegrationTest.java`
- [ ] T020 [P] [US1] Crear prueba de duplicidad case-insensitive (`409`) en `src/test/java/com/example/empleados/integration/CrearDepartamentoClaveDuplicadaIntegrationTest.java`
- [ ] T021 [P] [US1] Crear prueba de payload invalido (`400`) en `src/test/java/com/example/empleados/integration/CrearDepartamentoPayloadInvalidoIntegrationTest.java`
- [ ] T022 [P] [US1] Crear prueba de acceso sin token (`401`) en `src/test/java/com/example/empleados/integration/CrearDepartamentoSinTokenIntegrationTest.java`
- [ ] T023 [P] [US1] Crear prueba unitaria de normalizacion de `clave` en `src/test/java/com/example/empleados/unit/ClaveNormalizerTest.java`
- [ ] T024 [P] [US1] Crear prueba unitaria de validacion de regex de `clave` en `src/test/java/com/example/empleados/unit/DepartamentoValidationTest.java`

### Implementation for User Story 1

- [ ] T025 [US1] Implementar DTO de alta de departamento en `src/main/java/com/example/empleados/dto/DepartamentoRequest.java`
- [ ] T026 [US1] Implementar DTO de respuesta de departamento con `employeeCount` en `src/main/java/com/example/empleados/dto/DepartamentoResponse.java`
- [ ] T027 [US1] Implementar logica de alta con normalizacion y unicidad case-insensitive en `src/main/java/com/example/empleados/service/impl/DepartamentoServiceImpl.java`
- [ ] T028 [US1] Implementar endpoint `POST /api/v2/departamentos` en `src/main/java/com/example/empleados/controller/DepartamentoController.java`
- [ ] T029 [US1] Agregar auditoria de creacion (usuario, operacion, timestamp) en `src/main/java/com/example/empleados/service/impl/DepartamentoServiceImpl.java`

**Checkpoint**: MVP funcional de alta de departamentos.

---

## Phase 4: User Story 2 - Consultar y listar departamentos (Priority: P2)

**Goal**: Consultar por clave y listar departamentos activos con paginacion, defaults, orden `clave ASC` y `employeeCount`.

**Independent Test**: GET por clave devuelve `employeeCount`; listado respeta defaults y limite `size<=100`; inactivos se excluyen o devuelven `404`.

### Tests for User Story 2

- [ ] T030 [P] [US2] Crear prueba de contrato para `GET /api/v2/departamentos` y `GET /api/v2/departamentos/{clave}` en `src/test/java/com/example/empleados/contract/ConsultarDepartamentoContractTest.java`
- [ ] T031 [P] [US2] Crear prueba de integracion de consulta por clave existente en `src/test/java/com/example/empleados/integration/ObtenerDepartamentoIntegrationTest.java`
- [ ] T032 [P] [US2] Crear prueba de `404` por clave inexistente o inactiva en `src/test/java/com/example/empleados/integration/ObtenerDepartamentoNoEncontradoIntegrationTest.java`
- [ ] T033 [P] [US2] Crear prueba de listado paginado con orden `clave ASC` en `src/test/java/com/example/empleados/integration/ListarDepartamentosIntegrationTest.java`
- [ ] T034 [P] [US2] Crear prueba de defaults de paginacion (`page=0`,`size=10`) en `src/test/java/com/example/empleados/integration/ListarDepartamentosDefaultsPaginacionIntegrationTest.java`
- [ ] T035 [P] [US2] Crear prueba de validacion de `size > 100` (`400`) en `src/test/java/com/example/empleados/integration/ListarDepartamentosSizeInvalidoIntegrationTest.java`
- [ ] T036 [P] [US2] Crear prueba de acceso no autorizado (`401`) en `src/test/java/com/example/empleados/integration/AccesoDepartamentoNoAutorizadoIntegrationTest.java`
- [ ] T037 [P] [US2] Crear prueba unitaria del mapper de `employeeCount` en `src/test/java/com/example/empleados/unit/DepartamentoMapperTest.java`

### Implementation for User Story 2

- [ ] T038 [US2] Implementar mapper de departamentos sin lista embebida de empleados en `src/main/java/com/example/empleados/service/DepartamentoMapper.java`
- [ ] T039 [US2] Implementar consulta por clave case-insensitive filtrando solo activos en `src/main/java/com/example/empleados/service/impl/DepartamentoServiceImpl.java`
- [ ] T040 [US2] Implementar listado paginado de activos con defaults y orden en `src/main/java/com/example/empleados/service/impl/DepartamentoServiceImpl.java`
- [ ] T041 [US2] Implementar endpoints GET de departamentos en `src/main/java/com/example/empleados/controller/DepartamentoController.java`
- [ ] T042 [US2] Implementar consulta de `employeeCount` por departamento en `src/main/java/com/example/empleados/repository/DepartamentoRepository.java`

**Checkpoint**: Consultas/listados independientes y verificables.

---

## Phase 5: User Story 3 - Actualizar y eliminar departamentos (Priority: P3)

**Goal**: Actualizar solo `nombre` y aplicar soft delete con reglas de negocio (`SIN_DEPTO`, asociaciones, `204`).

**Independent Test**: `PUT` actualiza `nombre`; `DELETE` aplica soft delete exitoso con `204`; bloqueos devuelven `409`.

### Tests for User Story 3

- [ ] T043 [P] [US3] Crear prueba de contrato para `PUT/DELETE /api/v2/departamentos/{clave}` en `src/test/java/com/example/empleados/contract/ActualizarEliminarDepartamentoContractTest.java`
- [ ] T044 [P] [US3] Crear prueba de integracion de actualizacion exitosa en `src/test/java/com/example/empleados/integration/ActualizarDepartamentoIntegrationTest.java`
- [ ] T045 [P] [US3] Crear prueba de inmutabilidad de `clave` en `src/test/java/com/example/empleados/integration/ActualizarDepartamentoClaveInmutableIntegrationTest.java`
- [ ] T046 [P] [US3] Crear prueba de soft delete exitoso (`204`) en `src/test/java/com/example/empleados/integration/EliminarDepartamentoIntegrationTest.java`
- [ ] T047 [P] [US3] Crear prueba de bloqueo por empleados asociados (`409`) en `src/test/java/com/example/empleados/integration/EliminarDepartamentoConEmpleadosIntegrationTest.java`
- [ ] T048 [P] [US3] Crear prueba de bloqueo de `SIN_DEPTO` (`409`) en `src/test/java/com/example/empleados/integration/EliminarDepartamentoTecnicoIntegrationTest.java`
- [ ] T049 [P] [US3] Crear prueba de acceso no autorizado en `PUT/DELETE` (`401`) en `src/test/java/com/example/empleados/integration/ModificarDepartamentoSinTokenIntegrationTest.java`
- [ ] T050 [P] [US3] Crear prueba unitaria de reglas de soft delete en `src/test/java/com/example/empleados/unit/DepartamentoRulesValidatorTest.java`

### Implementation for User Story 3

- [ ] T051 [US3] Implementar DTO de actualizacion (solo `nombre`) en `src/main/java/com/example/empleados/dto/DepartamentoUpdateRequest.java`
- [ ] T052 [US3] Implementar actualizacion con politica last-write-wins en `src/main/java/com/example/empleados/service/impl/DepartamentoServiceImpl.java`
- [ ] T053 [US3] Implementar soft delete y validaciones de bloqueo en `src/main/java/com/example/empleados/service/impl/DepartamentoServiceImpl.java`
- [ ] T054 [US3] Implementar endpoints `PUT` y `DELETE` con salida `204` en `src/main/java/com/example/empleados/controller/DepartamentoController.java`
- [ ] T055 [US3] Agregar auditoria de actualizacion/eliminacion en `src/main/java/com/example/empleados/service/impl/DepartamentoServiceImpl.java`

**Checkpoint**: Mantenimiento y eliminacion logica completos.

---

## Phase 6: User Story 4 - Gestionar empleados con departamento obligatorio (Priority: P3)

**Goal**: Forzar `departamentoClave` valida/activa y case-insensitive en create/update de empleados.

**Independent Test**: Empleado con departamento valido funciona; faltante/inexistente/inactivo falla con error estandar.

### Tests for User Story 4

- [ ] T056 [P] [US4] Crear prueba de contrato para create/update de empleados con `departamentoClave` obligatoria en `src/test/java/com/example/empleados/contract/EmpleadoDepartamentoObligatorioContractTest.java`
- [ ] T057 [P] [US4] Crear prueba de integracion de alta sin `departamentoClave` (`400`) en `src/test/java/com/example/empleados/integration/CrearEmpleadoSinDepartamentoIntegrationTest.java`
- [ ] T058 [P] [US4] Crear prueba de integracion con departamento inexistente/inactivo en `src/test/java/com/example/empleados/integration/EmpleadoDepartamentoInvalidoIntegrationTest.java`
- [ ] T059 [P] [US4] Crear prueba de integracion con referencia case-insensitive valida en `src/test/java/com/example/empleados/integration/EmpleadoDepartamentoCaseInsensitiveIntegrationTest.java`
- [ ] T060 [P] [US4] Crear prueba de migracion de historicos a `SIN_DEPTO` en `src/test/java/com/example/empleados/integration/MigracionEmpleadoSinDepartamentoIntegrationTest.java`
- [ ] T061 [P] [US4] Crear prueba unitaria de validacion de departamento activo en `src/test/java/com/example/empleados/unit/EmpleadoDepartamentoValidatorTest.java`

### Implementation for User Story 4

- [ ] T062 [US4] Implementar validacion de departamento existente y activo en create/update en `src/main/java/com/example/empleados/service/impl/EmpleadoServiceImpl.java`
- [ ] T063 [US4] Implementar normalizacion de `departamentoClave` en mapper de empleados en `src/main/java/com/example/empleados/service/EmpleadoMapper.java`
- [ ] T064 [US4] Ajustar endpoints de empleados para contratos de error estandar en `src/main/java/com/example/empleados/controller/EmpleadoController.java`
- [ ] T065 [US4] Exponer `departamentoClave` en response DTO de empleados en `src/main/java/com/example/empleados/dto/EmpleadoResponse.java`
- [ ] T066 [US4] Actualizar contrato OpenAPI de empleados con `departamentoClave` obligatoria en `specs/003-crud-departamentos-empleados/contracts/departamentos.openapi.yaml`
- [ ] T078 [US4] Implementar rutas de login separadas para Empleados y Departamentos en `frontend/src/app/app.routes.ts` y `frontend/src/app/login-page.component.ts`

**Checkpoint**: Integracion obligatoria empleados-departamentos lista.

---

## Phase 7: Polish & Cross-Cutting Concerns

**Purpose**: Cierre de calidad transversal, NFR, evidencia y gates finales.

- [ ] T067 [P] Crear prueba de recorrido completo de paginas sin omisiones ni duplicados en `src/test/java/com/example/empleados/integration/ListarDepartamentosRecorridoPaginasIntegrationTest.java`
- [ ] T068 [P] Crear prueba de performance para `SC-002` (20 concurrentes, 1000 departamentos, p95<2s) en `src/test/java/com/example/empleados/integration/DepartamentosPerformanceIntegrationTest.java`
- [ ] T069 [P] Registrar evidencia de auditoria, performance y SC-004 en `specs/003-crud-departamentos-empleados/checklists/acceptance.md`
- [X] T070 [P] Implementar prueba E2E Cypress de login secuencial Empleados -> Departamentos en `frontend/cypress/e2e/auth/login-empleados-departamentos.cy.ts`
- [X] T071 Ejecutar pruebas E2E Cypress como gate de calidad (`npx cypress run`) y registrar evidencia en `specs/003-crud-departamentos-empleados/checklists/frontend-auth-e2e.md`
- [ ] T072 Ejecutar validacion completa de quickstart y registrar hallazgos en `specs/003-crud-departamentos-empleados/quickstart.md`
- [ ] T073 Ejecutar regresion backend y registrar resultados en `specs/003-crud-departamentos-empleados/checklists/environment.md`
- [ ] T074 [P] Alinear `plan.md` con evidencia final de quality gates y Principle VI en `specs/003-crud-departamentos-empleados/plan.md`
- [ ] T075 [P] [US1] Crear prueba de contrato de error `400` con `code`, `message`, `timestamp` en `src/test/java/com/example/empleados/contract/ErrorPayload400ContractTest.java`
- [ ] T076 [P] [US2] Crear prueba de contrato de error `404` con `code`, `message`, `timestamp` en `src/test/java/com/example/empleados/contract/ErrorPayload404ContractTest.java`
- [ ] T077 [P] [US3] Crear prueba de contrato de error `409` con `code`, `message`, `timestamp` en `src/test/java/com/example/empleados/contract/ErrorPayload409ContractTest.java`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: Sin dependencias.
- **Phase 2 (Foundational)**: Depende de Phase 1 y bloquea todas las historias.
- **Phase 3 (US1)**: Depende de Phase 2.
- **Phase 4 (US2)**: Depende de Phase 2 y de datos base de US1 para validacion completa.
- **Phase 5 (US3)**: Depende de Phase 2 y reglas de dominio de US1.
- **Phase 6 (US4)**: Depende de Phase 2 y reglas de departamentos consolidadas (US1-US3).
- **Phase 7 (Polish)**: Depende de cierre de historias objetivo.

### User Story Dependencies

- **US1 (P1)**: Base funcional para el resto de historias.
- **US2 (P2)**: Puede implementarse tras Foundational; su validacion completa usa datos de US1.
- **US3 (P3)**: Requiere el agregado de departamentos ya operativo.
- **US4 (P3)**: Requiere reglas finales de departamentos para validar referencias activas.

### Story Completion Order

- `Setup -> Foundational -> US1 -> (US2 || US3) -> US4 -> Polish`

### Within Each User Story

- Pruebas primero (deben fallar antes de implementar).
- Validadores/DTOs/mapper antes de servicios.
- Servicios antes de controladores.
- Contrato OpenAPI actualizado antes del checkpoint de cada historia.

### Parallel Opportunities

- Setup paralelizable: `T002`, `T003`, `T004`, `T005`.
- Foundational paralelizable: `T008`, `T009`, `T010`, `T011`, `T012`, `T014`, `T015`, `T017`.
- US1 paralelizable: `T018`, `T019`, `T020`, `T021`, `T022`, `T023`, `T024`.
- US2 paralelizable: `T030`, `T031`, `T032`, `T033`, `T034`, `T035`, `T036`, `T037`.
- US3 paralelizable: `T043`, `T044`, `T045`, `T046`, `T047`, `T048`, `T049`, `T050`.
- US4 paralelizable: `T056`, `T057`, `T058`, `T059`, `T060`, `T061`, `T078`.
- Polish paralelizable: `T067`, `T068`, `T069`, `T070`, `T074`, `T075`, `T076`, `T077`.

---

## Parallel Example: User Story 1

```bash
Task: "T018 [US1] Contract POST /departamentos"
Task: "T019 [US1] Integration create success"
Task: "T020 [US1] Integration duplicate 409"
Task: "T021 [US1] Integration invalid payload 400"
Task: "T023 [US1] Unit key normalization"
```

## Parallel Example: User Story 2

```bash
Task: "T030 [US2] Contract GET endpoints"
Task: "T031 [US2] Integration get by key"
Task: "T033 [US2] Integration pagination sort"
Task: "T034 [US2] Integration default pagination"
Task: "T037 [US2] Unit mapper employeeCount"
```

## Parallel Example: User Story 3

```bash
Task: "T043 [US3] Contract PUT/DELETE"
Task: "T044 [US3] Integration update"
Task: "T046 [US3] Integration soft delete 204"
Task: "T047 [US3] Integration delete blocked with employees"
Task: "T050 [US3] Unit soft delete rules"
```

## Parallel Example: User Story 4

```bash
Task: "T056 [US4] Contract empleados with departamentoClave"
Task: "T057 [US4] Integration missing departamentoClave"
Task: "T058 [US4] Integration invalid/inactive departamento"
Task: "T059 [US4] Integration case-insensitive reference"
Task: "T061 [US4] Unit active-departamento validator"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Phase 1: Setup.
2. Completar Phase 2: Foundational.
3. Completar Phase 3: US1.
4. Validar MVP con contrato, integracion y unitarias de US1.

### Incremental Delivery

1. Base tecnica: Setup + Foundational.
2. Incremento 1: US1 (alta de departamentos).
3. Incremento 2: US2 (consulta/listado).
4. Incremento 3: US3 (actualizacion/soft delete).
5. Incremento 4: US4 (integracion obligatoria con empleados).
6. Cierre: Polish con performance, Cypress y evidencia operativa.

### Parallel Team Strategy

1. Equipo completo en Setup + Foundational.
2. Luego distribuir por historia:
   - Dev A: US2
   - Dev B: US3
   - Dev C: US4
3. Integrar por checkpoints y cerrar con quality gates de Phase 7.
