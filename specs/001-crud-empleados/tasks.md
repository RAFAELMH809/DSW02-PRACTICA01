# Tasks: CRUD de Empleados

**Input**: Design documents from `/specs/001-crud-empleados/`  
**Prerequisites**: plan.md (required), spec.md (required), research.md, data-model.md, contracts/, quickstart.md

**Tests**: Se incluyen tareas de pruebas porque la especificación exige cobertura de validación, seguridad y CRUD autorizado.

**Organization**: Las tareas se agrupan por historia de usuario para permitir implementación y validación independientes.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Puede ejecutarse en paralelo (archivos distintos, sin dependencia directa)
- **[Story]**: Historia de usuario (`[US1]`, `[US2]`, `[US3]`)
- Todas las tareas incluyen rutas de archivo específicas

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Inicializar proyecto backend y base operativa

- [X] T001 Crear estructura base del proyecto Spring Boot en `pom.xml`
- [X] T002 [P] Crear archivo de orquestación PostgreSQL en `docker-compose.yml`
- [X] T003 [P] Configurar propiedades base de aplicación en `src/main/resources/application.yml`
- [X] T004 [P] Configurar perfil local con credenciales por entorno en `src/main/resources/application-dev.yml`
- [X] T005 [P] Crear clase principal Spring Boot en `src/main/java/com/example/empleados/EmpleadosApplication.java`
- [X] T006 Configurar dependencias de pruebas (JUnit, MockMvc, Testcontainers) en `pom.xml`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura obligatoria antes de cualquier historia

**⚠️ CRITICAL**: Ninguna historia de usuario inicia antes de completar esta fase

- [X] T007 Crear migración inicial de tabla empleados en `src/main/resources/db/migration/V1__create_empleados_table.sql`
- [X] T008 [P] Implementar entidad `Empleado` en `src/main/java/com/example/empleados/domain/Empleado.java`
- [X] T009 [P] Implementar repositorio JPA de empleados en `src/main/java/com/example/empleados/repository/EmpleadoRepository.java`
- [X] T010 [P] Implementar DTOs base (`EmpleadoRequest`, `EmpleadoResponse`) en `src/main/java/com/example/empleados/dto/`
- [X] T011 Implementar configuración de seguridad HTTP Basic en `src/main/java/com/example/empleados/config/SecurityConfig.java`
- [X] T012 [P] Implementar configuración OpenAPI/Swagger en `src/main/java/com/example/empleados/config/OpenApiConfig.java`
- [X] T013 [P] Implementar manejador global de errores en `src/main/java/com/example/empleados/controller/GlobalExceptionHandler.java`
- [X] T014 Definir servicio base de empleados con interfaz en `src/main/java/com/example/empleados/service/EmpleadoService.java`
- [X] T015 Alinear contrato OpenAPI base con seguridad y esquemas en `specs/001-crud-empleados/contracts/empleados.openapi.yaml`
- [X] T016 Implementar estrategia de generación de clave compuesta (`EMP-` + secuencia) en `src/main/java/com/example/empleados/service/KeyGeneratorService.java`


**Checkpoint**: Base técnica lista; historias pueden avanzar

---

## Phase 3: User Story 1 - Registrar empleados (Priority: P1) 🎯 MVP

**Goal**: Permitir alta de empleados con validaciones de negocio

**Independent Test**: Crear un empleado válido autenticado, rechazar sobrelongitud y verificar generación única de `clave` con patrón `EMP-<autonumérico>`

### Tests for User Story 1

- [X] T017 [P] [US1] Crear prueba de contrato `POST /empleados` verificando clave autogenerada en `src/test/java/com/example/empleados/contract/CrearEmpleadoContractTest.java`
- [X] T018 [P] [US1] Crear prueba de integración de alta exitosa con patrón `EMP-<autonumérico>` en `src/test/java/com/example/empleados/integration/CrearEmpleadoIntegrationTest.java`
- [X] T019 [P] [US1] Crear prueba unitaria de validación de longitud (<=100) en `src/test/java/com/example/empleados/unit/EmpleadoValidationTest.java`
- [X] T020 [P] [US1] Crear prueba de integración de unicidad de clave autogenerada en altas concurrentes en `src/test/java/com/example/empleados/integration/CrearEmpleadoClaveUnicaConcurrenteIntegrationTest.java`

### Implementation for User Story 1

- [X] T021 [US1] Implementar servicio de creación con generación automática de clave en `src/main/java/com/example/empleados/service/impl/EmpleadoServiceImpl.java`
- [X] T022 [US1] Implementar endpoint `POST /empleados` sin entrada de clave en `src/main/java/com/example/empleados/controller/EmpleadoController.java`
- [X] T023 [US1] Implementar mapeador DTO-entidad para creación con clave generada en `src/main/java/com/example/empleados/service/EmpleadoMapper.java`
- [X] T024 [US1] Documentar alta con clave autogenerada y respuestas 201/400/401 en `specs/001-crud-empleados/contracts/empleados.openapi.yaml`

**Checkpoint**: Alta de empleados funcional y verificable de forma independiente

---

## Phase 4: User Story 2 - Consultar empleados (Priority: P2)

**Goal**: Permitir consulta por clave y listado completo

**Independent Test**: Consultar listado y detalle con autenticación; validar 404 para clave inexistente

### Tests for User Story 2

- [X] T025 [P] [US2] Crear prueba de contrato `GET /empleados` y `GET /empleados/{clave}` con patrón de clave válido en `src/test/java/com/example/empleados/contract/ConsultarEmpleadoContractTest.java`
- [X] T026 [P] [US2] Crear prueba de integración para listado de empleados en `src/test/java/com/example/empleados/integration/ListarEmpleadosIntegrationTest.java`
- [X] T027 [P] [US2] Crear prueba de integración para consulta por clave con formato inválido y por clave inexistente en `src/test/java/com/example/empleados/integration/ObtenerEmpleadoNoEncontradoIntegrationTest.java`

### Implementation for User Story 2

- [X] T028 [US2] Implementar servicio de consulta por clave y listado en `src/main/java/com/example/empleados/service/impl/EmpleadoServiceImpl.java`
- [X] T029 [US2] Implementar endpoints `GET /empleados` y `GET /empleados/{clave}` con validación de patrón de clave en `src/main/java/com/example/empleados/controller/EmpleadoController.java`
- [X] T030 [US2] Documentar respuestas 200/400/401/404 para consultas en `specs/001-crud-empleados/contracts/empleados.openapi.yaml`

**Checkpoint**: Consultas funcionales y testeables sin depender de US3

---

## Phase 5: User Story 3 - Actualizar y eliminar empleados (Priority: P3)

**Goal**: Permitir actualización y eliminación por clave

**Independent Test**: Actualizar campos válidos y eliminar registro existente, validando 404 para inexistentes

### Tests for User Story 3

- [X] T031 [P] [US3] Crear prueba de contrato `PUT /empleados/{clave}` y `DELETE /empleados/{clave}` en `src/test/java/com/example/empleados/contract/ActualizarEliminarEmpleadoContractTest.java`
- [X] T032 [P] [US3] Crear prueba de integración de actualización exitosa sin cambio de clave en `src/test/java/com/example/empleados/integration/ActualizarEmpleadoIntegrationTest.java`
- [X] T033 [P] [US3] Crear prueba de integración de eliminación exitosa (204) en `src/test/java/com/example/empleados/integration/EliminarEmpleadoIntegrationTest.java`
- [X] T034 [P] [US3] Crear prueba de integración para formato de clave inválido e inexistente (400/404) en `src/test/java/com/example/empleados/integration/ActualizarEliminarEmpleadoNoEncontradoIntegrationTest.java`

### Implementation for User Story 3

- [X] T035 [US3] Implementar servicio de actualización y eliminación en `src/main/java/com/example/empleados/service/impl/EmpleadoServiceImpl.java`
- [X] T036 [US3] Implementar endpoints `PUT /empleados/{clave}` y `DELETE /empleados/{clave}` con validación de patrón de clave en `src/main/java/com/example/empleados/controller/EmpleadoController.java`
- [X] T037 [US3] Documentar respuestas 200/204/400/401/404 para actualización y eliminación en `specs/001-crud-empleados/contracts/empleados.openapi.yaml`

**Checkpoint**: CRUD completo operativo por historia independiente

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre transversal de calidad y documentación

- [X] T038 [P] Actualizar guía de ejecución y validación en `specs/001-crud-empleados/quickstart.md`
- [X] T039 Endurecer configuración de seguridad por perfil (credenciales por entorno) en `src/main/resources/application-dev.yml`
- [X] T040 [P] Añadir prueba de integración de autenticación fallida (401) en `src/test/java/com/example/empleados/integration/SeguridadBasicaIntegrationTest.java`
- [X] T041 Ejecutar validación final de pruebas y documentar resultado en `specs/001-crud-empleados/quickstart.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: inicio inmediato
- **Phase 2 (Foundational)**: depende de Phase 1; bloquea todas las historias
- **Phase 3-5 (User Stories)**: dependen de Phase 2
- **Phase 6 (Polish)**: depende de historias objetivo completadas

### User Story Dependencies

- **US1 (P1)**: inicia al terminar Foundational; no depende de otras historias
- **US2 (P2)**: inicia al terminar Foundational; depende de datos creados por flujo de US1 para pruebas reales
- **US3 (P3)**: inicia al terminar Foundational; usa base de US1/US2 pero mantiene validación independiente

### Dependency Graph (Story Order)

- `Setup -> Foundational -> US1 -> US2 -> US3 -> Polish`
- Paralelizable por equipo: `US2 || US3` después de completar `US1` mínimo para dataset semilla

### Within Each User Story

- Pruebas primero (deben fallar antes de implementar)
- Servicio antes que controlador
- Actualización de contrato OpenAPI al cerrar cada historia

### Parallel Opportunities

- Setup paralelizable: `T002`, `T003`, `T004`, `T005`
- Foundational paralelizable: `T008`, `T009`, `T010`, `T012`, `T013`
- US1 paralelizable: `T017`, `T018`, `T019`, `T020`
- US2 paralelizable: `T025`, `T026`, `T027`
- US3 paralelizable: `T031`, `T032`, `T033`, `T034`
- Polish paralelizable: `T038`, `T040`

---

## Parallel Example: User Story 1

```bash
# Tests de US1 en paralelo:
Task: "T017 [US1] Contrato POST /empleados"
Task: "T018 [US1] Integración alta exitosa"
Task: "T019 [US1] Unitarias de longitud"
Task: "T020 [US1] Clave única concurrente"

# Implementación en paralelo (sin conflicto directo):
Task: "T023 [US1] EmpleadoMapper"
Task: "T024 [US1] Documentación OpenAPI de alta"
```

## Parallel Example: User Story 2

```bash
# Tests de consulta en paralelo:
Task: "T025 [US2] Contrato GET"
Task: "T026 [US2] Integración listado"
Task: "T027 [US2] Integración clave inválida/404"
```

## Parallel Example: User Story 3

```bash
# Tests de actualización/eliminación en paralelo:
Task: "T031 [US3] Contrato PUT/DELETE"
Task: "T032 [US3] Integración actualización"
Task: "T033 [US3] Integración eliminación"
Task: "T034 [US3] Integración clave inválida/404"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Phase 1 (Setup)
2. Completar Phase 2 (Foundational)
3. Completar Phase 3 (US1)
4. Validar alta de empleados de punta a punta
5. Presentar MVP

### Incremental Delivery

1. Base técnica (Phase 1 + 2)
2. Entregar US1 (alta)
3. Entregar US2 (consultas)
4. Entregar US3 (actualizar/eliminar)
5. Cerrar con Polish

### Parallel Team Strategy

1. Equipo A: infraestructura (Phase 1 + 2)
2. Equipo B: US1
3. Equipo C: US2/US3 tras disponibilidad de dataset base
4. Integración y cierre en Phase 6
