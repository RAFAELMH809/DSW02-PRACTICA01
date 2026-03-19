# Tasks: CRUD de Departamentos Vinculado a Empleados

**Input**: Design documents from `/specs/003-crud-departamentos-empleados/`
**Prerequisites**: plan.md (required), spec.md (required)

**Tests**: Se incluyen tareas de pruebas porque `QUAL-001` exige pruebas unitarias e integracion, y el feature necesita validar contrato, migracion y seguridad.

**Organization**: Tareas agrupadas por historia de usuario para implementacion y prueba independiente.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar la documentacion tecnica base del feature y el entorno de configuracion comun.

- [X] T001 Crear contrato OpenAPI inicial de departamentos en `specs/003-crud-departamentos-empleados/contracts/departamentos.openapi.yaml`
- [X] T002 Crear quickstart del feature en `specs/003-crud-departamentos-empleados/quickstart.md`
- [X] T003 [P] Crear research del feature con decisiones de migracion y auditoria en `specs/003-crud-departamentos-empleados/research.md`
- [X] T004 [P] Crear data model del feature en `specs/003-crud-departamentos-empleados/data-model.md`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura tecnica obligatoria antes de cualquier historia.

**CRITICAL**: Ninguna historia inicia hasta cerrar esta fase.

- [X] T005 Crear migracion de tabla de departamentos en `src/main/resources/db/migration/V2__create_departamentos_table.sql`
- [X] T006 [P] Crear migracion de relacion obligatoria y backfill `SIN_DEPTO` en `src/main/resources/db/migration/V3__link_empleados_departamentos.sql`
- [X] T007 [P] Crear entidad `Departamento` en `src/main/java/com/example/empleados/domain/Departamento.java`
- [X] T008 [P] Actualizar entidad `Empleado` con referencia obligatoria a departamento en `src/main/java/com/example/empleados/domain/Empleado.java`
- [X] T009 [P] Crear repositorio `DepartamentoRepository` en `src/main/java/com/example/empleados/repository/DepartamentoRepository.java`
- [X] T010 [P] Crear DTOs `DepartamentoRequest` y `DepartamentoResponse` en `src/main/java/com/example/empleados/dto/DepartamentoRequest.java` y `src/main/java/com/example/empleados/dto/DepartamentoResponse.java`
- [X] T011 [P] Crear mapper `DepartamentoMapper` en `src/main/java/com/example/empleados/service/DepartamentoMapper.java`
- [X] T012 Actualizar DTOs de empleados para requerir `departamentoClave` en `src/main/java/com/example/empleados/dto/EmpleadoRequest.java` y `src/main/java/com/example/empleados/dto/EmpleadoResponse.java`
- [X] T013 Implementar validador de reglas de negocio compartidas (duplicidad, `SIN_DEPTO`, relacion obligatoria) en `src/main/java/com/example/empleados/service/DepartamentoRulesValidator.java`

**Checkpoint**: Fundacion lista para historias.

---

## Phase 3: User Story 1 - Registrar departamentos (Priority: P1) 🎯 MVP

**Goal**: Crear departamentos con `clave` y `nombre`, garantizando unicidad case-insensitive e inmutabilidad de la clave.

**Independent Test**: Crear un departamento con datos validos devuelve `201`; crear uno duplicado por clave o por variacion de mayusculas/minusculas devuelve error claro.

### Tests for User Story 1

- [X] T014 [P] [US1] Crear contrato de alta de departamentos en `src/test/java/com/example/empleados/contract/CrearDepartamentoContractTest.java`
- [X] T015 [P] [US1] Crear prueba de integracion de alta exitosa en `src/test/java/com/example/empleados/integration/CrearDepartamentoIntegrationTest.java`
- [X] T016 [P] [US1] Crear prueba de duplicidad case-insensitive en `src/test/java/com/example/empleados/integration/CrearDepartamentoClaveDuplicadaIntegrationTest.java`
- [X] T017 [P] [US1] Crear prueba unitaria de validacion de `DepartamentoRequest` en `src/test/java/com/example/empleados/unit/DepartamentoValidationTest.java`

### Implementation for User Story 1

- [X] T018 [US1] Crear interfaz `DepartamentoService` en `src/main/java/com/example/empleados/service/DepartamentoService.java`
- [X] T019 [US1] Implementar `DepartamentoServiceImpl` para alta de departamentos en `src/main/java/com/example/empleados/service/impl/DepartamentoServiceImpl.java`
- [X] T020 [US1] Implementar endpoint `POST /api/v2/departamentos` en `src/main/java/com/example/empleados/controller/DepartamentoController.java`
- [X] T021 [US1] Registrar auditoria de creacion de departamentos en logs desde `src/main/java/com/example/empleados/service/impl/DepartamentoServiceImpl.java`

**Checkpoint**: MVP funcional y demostrable.

---

## Phase 4: User Story 2 - Consultar y listar departamentos (Priority: P2)

**Goal**: Consultar departamentos por `clave` y listar departamentos con paginacion `page` y `size`, mostrando relacion con empleados.

**Independent Test**: Consultar por clave devuelve el departamento esperado; listar con paginacion devuelve pagina consistente; consultar inexistente devuelve `404`.

### Tests for User Story 2

- [X] T022 [P] [US2] Crear contrato de consulta y listado paginado en `src/test/java/com/example/empleados/contract/ConsultarDepartamentoContractTest.java`
- [X] T023 [P] [US2] Crear prueba de consulta por clave en `src/test/java/com/example/empleados/integration/ObtenerDepartamentoIntegrationTest.java`
- [X] T024 [P] [US2] Crear prueba de listado paginado en `src/test/java/com/example/empleados/integration/ListarDepartamentosIntegrationTest.java`
- [X] T025 [P] [US2] Crear prueba de no encontrado en consulta por clave en `src/test/java/com/example/empleados/integration/ObtenerDepartamentoNoEncontradoIntegrationTest.java`
- [ ] T052 [P] [US2] Crear prueba de acceso sin token (401) en `src/test/java/com/example/empleados/integration/AccesoDepartamentoSinTokenIntegrationTest.java`
- [ ] T053 [P] [US2] Crear prueba de acceso con token invalido (401) en `src/test/java/com/example/empleados/integration/AccesoDepartamentoTokenInvalidoIntegrationTest.java`
- [ ] T054 [P] [US2] Crear prueba de acceso con token expirado (401) en `src/test/java/com/example/empleados/integration/AccesoDepartamentoTokenExpiradoIntegrationTest.java`

### Implementation for User Story 2

- [X] T026 [US2] Implementar consultas por `clave` y listados paginados en `src/main/java/com/example/empleados/service/impl/DepartamentoServiceImpl.java`
- [X] T027 [US2] Implementar endpoints `GET /api/v2/departamentos` y `GET /api/v2/departamentos/{clave}` en `src/main/java/com/example/empleados/controller/DepartamentoController.java`
- [X] T028 [US2] Ajustar mapper/response para exponer `employeeCount` en `src/main/java/com/example/empleados/service/DepartamentoMapper.java` y `src/main/java/com/example/empleados/dto/DepartamentoResponse.java`
- [X] T029 [US2] Actualizar contrato OpenAPI de departamentos y respuestas paginadas en `specs/003-crud-departamentos-empleados/contracts/departamentos.openapi.yaml`

**Checkpoint**: US1 y US2 operan independientemente.

---

## Phase 5: User Story 3 - Actualizar y eliminar departamentos (Priority: P3)

**Goal**: Actualizar solo el nombre de departamentos y eliminar departamentos validando que no tengan empleados asociados ni sean `SIN_DEPTO`.

**Independent Test**: Actualizar nombre de un departamento funciona; eliminar con empleados asociados o `SIN_DEPTO` falla; eliminar uno sin dependencias responde `204`.

### Tests for User Story 3

- [X] T030 [P] [US3] Crear contrato de actualizacion y eliminacion en `src/test/java/com/example/empleados/contract/ActualizarEliminarDepartamentoContractTest.java`
- [X] T031 [P] [US3] Crear prueba de actualizacion exitosa de nombre en `src/test/java/com/example/empleados/integration/ActualizarDepartamentoIntegrationTest.java`
- [X] T032 [P] [US3] Crear prueba de eliminacion exitosa sin empleados en `src/test/java/com/example/empleados/integration/EliminarDepartamentoIntegrationTest.java`
- [X] T033 [P] [US3] Crear prueba de eliminacion bloqueada por empleados asociados en `src/test/java/com/example/empleados/integration/EliminarDepartamentoConEmpleadosIntegrationTest.java`
- [X] T034 [P] [US3] Crear prueba de eliminacion prohibida de `SIN_DEPTO` en `src/test/java/com/example/empleados/integration/EliminarDepartamentoTecnicoIntegrationTest.java`

### Implementation for User Story 3

- [X] T035 [US3] Implementar actualizacion de nombre con politica last-write-wins en `src/main/java/com/example/empleados/service/impl/DepartamentoServiceImpl.java`
- [X] T036 [US3] Implementar reglas de eliminacion y validacion de `SIN_DEPTO` en `src/main/java/com/example/empleados/service/impl/DepartamentoServiceImpl.java`
- [X] T037 [US3] Implementar endpoints `PUT /api/v2/departamentos/{clave}` y `DELETE /api/v2/departamentos/{clave}` en `src/main/java/com/example/empleados/controller/DepartamentoController.java`
- [X] T038 [US3] Registrar auditoria de actualizacion y eliminacion de departamentos en logs desde `src/main/java/com/example/empleados/service/impl/DepartamentoServiceImpl.java`

**Checkpoint**: Todo el CRUD de departamentos queda funcional.

---

## Phase 6: User Story 4 - Integrar empleados con departamento obligatorio (Priority: P3)

**Goal**: Hacer obligatoria la referencia `departamentoClave` en empleados y validar que exista al crear o actualizar empleados.

**Independent Test**: Crear/actualizar empleado con `departamentoClave` valido funciona; sin departamento o con clave inexistente falla; empleados historicos quedan vinculados a `SIN_DEPTO` tras migracion.

### Tests for User Story 4

- [X] T039 [P] [US4] Crear prueba de alta de empleado con departamento valido en `src/test/java/com/example/empleados/integration/CrearEmpleadoConDepartamentoIntegrationTest.java`
- [X] T040 [P] [US4] Crear prueba de alta de empleado sin departamento en `src/test/java/com/example/empleados/integration/CrearEmpleadoSinDepartamentoIntegrationTest.java`
- [X] T041 [P] [US4] Crear prueba de alta de empleado con departamento inexistente en `src/test/java/com/example/empleados/integration/CrearEmpleadoDepartamentoInexistenteIntegrationTest.java`
- [X] T042 [P] [US4] Crear prueba de actualizacion de empleado con cambio de departamento valido en `src/test/java/com/example/empleados/integration/ActualizarEmpleadoDepartamentoIntegrationTest.java`
- [ ] T043 [P] [US4] Crear prueba de migracion de empleados historicos a `SIN_DEPTO` en `src/test/java/com/example/empleados/integration/MigracionEmpleadoSinDepartamentoIntegrationTest.java`

### Implementation for User Story 4

- [X] T044 [US4] Actualizar `EmpleadoMapper` para mapear `departamentoClave` en `src/main/java/com/example/empleados/service/EmpleadoMapper.java`
- [X] T045 [US4] Actualizar `EmpleadoServiceImpl` para validar existencia de departamento al crear y actualizar en `src/main/java/com/example/empleados/service/impl/EmpleadoServiceImpl.java`
- [X] T046 [US4] Ajustar serializacion y respuestas de empleados para exponer `departamentoClave` en `src/main/java/com/example/empleados/dto/EmpleadoResponse.java`
- [X] T047 [US4] Actualizar contrato/documentacion de empleados con `departamentoClave` en `specs/003-crud-departamentos-empleados/contracts/departamentos.openapi.yaml`

**Checkpoint**: Relacion obligatoria empleados-departamentos implementada y verificable.

---

## Phase 7: Polish & Cross-Cutting Concerns

**Purpose**: Cierre de calidad, documentacion y regresion.

- [ ] T048 [P] Actualizar checklist de calidad del feature en `specs/003-crud-departamentos-empleados/checklists/requirements.md`
- [ ] T049 [P] Crear evidencia de validacion de logs/auditoria en `specs/003-crud-departamentos-empleados/checklists/audit.md`
- [ ] T050 [P] Ejecutar regression suite de empleados y departamentos en `src/test/java/com/example/empleados`
- [ ] T051 Ejecutar validacion manual del quickstart del feature en `specs/003-crud-departamentos-empleados/quickstart.md`
- [ ] T055 [P] Medir rendimiento de consulta/listado paginado para SC-002 en `src/test/java/com/example/empleados/integration/DepartamentosPerformanceIntegrationTest.java`
- [ ] T056 [P] Publicar evidencia de SC-002 (p95 < 2s) en `specs/003-crud-departamentos-empleados/checklists/performance.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: inicia sin dependencias.
- **Phase 2 (Foundational)**: depende de Setup y bloquea todas las historias.
- **Phase 3 (US1)**: depende de Foundational.
- **Phase 4 (US2)**: depende de Foundational y reutiliza la base creada por US1.
- **Phase 5 (US3)**: depende de Foundational y usa el servicio/controlador de departamentos ya creado.
- **Phase 6 (US4)**: depende de Foundational y del modelo `Departamento`; puede ejecutarse tras US1 si el equipo decide priorizar integracion sobre consulta completa.
- **Phase 7 (Polish)**: depende de las historias completadas.

### User Story Dependencies

- **US1 (P1)**: independiente tras fundacion y define el MVP de alta de departamentos.
- **US2 (P2)**: depende de la existencia del agregado `Departamento`, pero es independiente de US3 y US4 para probar consultas.
- **US3 (P3)**: depende del agregado `Departamento` implementado por US1.
- **US4 (P3)**: depende del agregado `Departamento` y de migraciones/DTOs comunes de la fundacion.

### Within Each User Story

- Pruebas primero y en estado fallido inicial.
- Entidades/DTOs/mappers antes de servicios.
- Servicios antes de controladores.
- Contratos y documentacion sincronizados con cada historia.
- Regresion de empleados al final de US4.

## Parallel Opportunities

- Setup en paralelo: `T003`, `T004`.
- Foundational en paralelo: `T006`, `T007`, `T008`, `T009`, `T010`, `T011`.
- US1 en paralelo: `T014`, `T015`, `T016`, `T017`.
- US2 en paralelo: `T022`, `T023`, `T024`, `T025`.
- US3 en paralelo: `T030`, `T031`, `T032`, `T033`, `T034`.
- US4 en paralelo: `T039`, `T040`, `T041`, `T042`, `T043`.
- Polish en paralelo: `T048`, `T049`, `T050`.

## Parallel Example: User Story 1

```bash
T014 Contract alta departamento
T015 Integracion alta exitosa
T016 Integracion duplicidad case-insensitive
T017 Validacion request
```

## Parallel Example: User Story 3

```bash
T030 Contract update/delete
T031 Actualizar nombre
T032 Eliminar sin empleados
T033 Eliminar con empleados
T034 Eliminar SIN_DEPTO
```

## Parallel Example: User Story 4

```bash
T039 Crear empleado con departamento
T040 Crear empleado sin departamento
T041 Crear empleado con departamento inexistente
T042 Actualizar empleado con nuevo departamento
T043 Migracion a SIN_DEPTO
```

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Setup.
2. Completar Foundational.
3. Completar US1.
4. Validar alta de departamentos y duplicidad case-insensitive.

### Incremental Delivery

1. Setup + Foundational.
2. US1 (alta de departamentos).
3. US2 (consulta y listado paginado).
4. US3 (actualizacion y eliminacion).
5. US4 (integracion obligatoria con empleados).
6. Polish + regression.

### Parallel Team Strategy

1. Equipo completo en Setup/Foundational.
2. Luego distribuir por historias: US1/US2, US3 y US4.
3. Integrar contratos, regression y quickstart al cierre.

## Notes

- Formato checklist aplicado: `- [ ] T### [P?] [US?] Descripcion con ruta`.
- Labels `[US1]`, `[US2]`, `[US3]`, `[US4]` solo aparecen en fases de historias.
- Cada historia es independiente y comprobable.
- `US4` existe para aislar la integracion con empleados y mantener trazabilidad de una dependencia transversal del feature.
