# Tasks: Autenticacion de Empleados

**Input**: Design documents from `/specs/001-autenticacion-empleados/`
**Prerequisites**: plan.md (required), spec.md (required), research.md, data-model.md, contracts/

**Tests**: Se incluyen tareas de pruebas porque `QUAL-001` exige cobertura unitaria, integracion y contrato.

**Organization**: Tareas agrupadas por historia de usuario para implementacion y prueba independiente.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar configuracion base y entorno reproducible.

- [ ] T001 Actualizar variables JWT de ejemplo en `.env.example`
- [ ] T002 Ajustar propiedades base de JWT y seguridad en `src/main/resources/application.yml`
- [ ] T003 [P] Ajustar propiedades por perfil de desarrollo en `src/main/resources/application-dev.yml`
- [ ] T004 [P] Actualizar variables de entorno requeridas en `docker/docker-compose.yml`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura tecnica obligatoria antes de cualquier historia.

**CRITICAL**: Ninguna historia inicia hasta cerrar esta fase.

- [ ] T005 Crear migracion de credenciales en `src/main/resources/db/migration/V2__create_credenciales_empleado_table.sql`
- [ ] T006 [P] Crear migracion de eventos de autenticacion en `src/main/resources/db/migration/V3__create_eventos_autenticacion_table.sql`
- [ ] T007 [P] Crear entidad `CredencialEmpleado` en `src/main/java/com/example/empleados/domain/CredencialEmpleado.java`
- [ ] T008 [P] Crear entidad `EventoAutenticacion` en `src/main/java/com/example/empleados/domain/EventoAutenticacion.java`
- [ ] T009 [P] Crear repositorio de credenciales en `src/main/java/com/example/empleados/repository/CredencialEmpleadoRepository.java`
- [ ] T010 [P] Crear repositorio de eventos en `src/main/java/com/example/empleados/repository/EventoAutenticacionRepository.java`
- [ ] T011 Implementar proveedor JWT (firma, iat, exp=60m, validacion) en `src/main/java/com/example/empleados/service/JwtTokenService.java`
- [ ] T012 Reemplazar seguridad basica por JWT Bearer en `src/main/java/com/example/empleados/config/SecurityConfig.java`

**Checkpoint**: Fundacion lista para historias.

---

## Phase 3: User Story 1 - Iniciar sesion con correo y contrasena (Priority: P1) 🎯 MVP

**Goal**: Login de empleado que emite JWT Bearer con expiracion a 60 minutos y sin lockout.

**Independent Test**: Login exitoso devuelve token; login fallido/inactivo devuelve 401; intentos fallidos repetidos no bloquean cuenta.

### Tests for User Story 1

- [ ] T013 [P] [US1] Crear contrato de login exitoso/fallido en `src/test/java/com/example/empleados/contract/LoginEmpleadoContractTest.java`
- [ ] T014 [P] [US1] Crear prueba de login exitoso con cuenta activa en `src/test/java/com/example/empleados/integration/LoginEmpleadoExitosoIntegrationTest.java`
- [ ] T015 [P] [US1] Crear prueba de credenciales invalidas con mensaje generico en `src/test/java/com/example/empleados/integration/LoginEmpleadoCredencialesInvalidasIntegrationTest.java`
- [ ] T016 [P] [US1] Crear prueba de login rechazado por cuenta inactiva en `src/test/java/com/example/empleados/integration/LoginEmpleadoCuentaInactivaIntegrationTest.java`
- [ ] T017 [P] [US1] Crear prueba de intentos fallidos repetidos sin bloqueo automatico en `src/test/java/com/example/empleados/integration/LoginSinBloqueoPorIntentosFallidosIntegrationTest.java`

### Implementation for User Story 1

- [ ] T018 [P] [US1] Crear DTO `LoginRequest` en `src/main/java/com/example/empleados/dto/LoginRequest.java`
- [ ] T019 [P] [US1] Crear DTO `LoginResponse` en `src/main/java/com/example/empleados/dto/LoginResponse.java`
- [ ] T020 [US1] Implementar servicio de autenticacion en `src/main/java/com/example/empleados/service/AutenticacionService.java`
- [ ] T021 [US1] Implementar endpoint `POST /api/v2/auth/login` en `src/main/java/com/example/empleados/controller/AutenticacionController.java`
- [ ] T022 [US1] Registrar eventos `SUCCESS`, `FAIL_INVALID_CREDENTIALS`, `FAIL_INACTIVE` y garantizar ausencia de lockout (FR-010) en `src/main/java/com/example/empleados/service/AutenticacionService.java`

**Checkpoint**: MVP funcional y demostrable.

---

## Phase 4: User Story 2 - Bloquear acceso sin credenciales validas (Priority: P2)

**Goal**: Proteger endpoints de empleados con JWT Bearer valido y no expirado.

**Independent Test**: Sin token/invalido/expirado responde 401; token valido permite acceso protegido.

### Tests for User Story 2

- [ ] T023 [P] [US2] Actualizar contrato de seguridad Bearer para endpoints protegidos en `specs/001-autenticacion-empleados/contracts/autenticacion.openapi.yaml`
- [ ] T024 [P] [US2] Crear prueba sin token en endpoint protegido en `src/test/java/com/example/empleados/integration/AccesoProtegidoSinTokenIntegrationTest.java`
- [ ] T025 [P] [US2] Crear prueba con token invalido en endpoint protegido en `src/test/java/com/example/empleados/integration/AccesoProtegidoTokenInvalidoIntegrationTest.java`
- [ ] T026 [P] [US2] Crear prueba con token expirado en endpoint protegido en `src/test/java/com/example/empleados/integration/AccesoProtegidoTokenExpiradoIntegrationTest.java`

### Implementation for User Story 2

- [ ] T027 [US2] Implementar filtro de autenticacion JWT en `src/main/java/com/example/empleados/config/JwtAuthenticationFilter.java`
- [ ] T028 [US2] Integrar filtro JWT en la cadena de seguridad en `src/main/java/com/example/empleados/config/SecurityConfig.java`
- [ ] T029 [US2] Migrar prueba de creacion de empleado para usar Bearer en `src/test/java/com/example/empleados/integration/CrearEmpleadoIntegrationTest.java`
- [ ] T030 [US2] Migrar pruebas CRUD restantes a Bearer en `src/test/java/com/example/empleados/integration`

**Checkpoint**: Endpoints protegidos operan con JWT.

---

## Phase 5: User Story 3 - Gestionar estados de cuenta en autenticacion (Priority: P3)

**Goal**: Validar cuenta activa solo en login y mantener token vigente hasta expiracion.

**Independent Test**: Cuenta inactiva no puede loguear; token emitido antes de inactivar sigue valido hasta expirar.

### Tests for User Story 3

- [ ] T031 [P] [US3] Crear prueba de token vigente tras inactivar cuenta en `src/test/java/com/example/empleados/integration/TokenVigenteTrasInactivarCuentaIntegrationTest.java`
- [ ] T032 [P] [US3] Crear prueba de nuevo login rechazado tras inactivar cuenta en `src/test/java/com/example/empleados/integration/LoginRechazadoTrasInactivarCuentaIntegrationTest.java`

### Implementation for User Story 3

- [ ] T033 [US3] Aplicar validacion de estado activo solo en login en `src/main/java/com/example/empleados/service/AutenticacionService.java`
- [ ] T034 [US3] Mantener validacion de endpoint protegido basada en token (sin revalidar estado de cuenta) en `src/main/java/com/example/empleados/config/JwtAuthenticationFilter.java`

**Checkpoint**: Regla de estado de cuenta implementada segun spec.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre de calidad, performance y documentacion.

- [ ] T035 [P] Actualizar quickstart con flujo JWT final en `specs/001-autenticacion-empleados/quickstart.md`
- [ ] T036 [P] Actualizar checklist de calidad de especificacion en `specs/001-autenticacion-empleados/checklists/requirements.md`
- [ ] T037 [P] Diseñar y ejecutar prueba de rendimiento de login para SC-006 (p95 < 2s) en `src/test/java/com/example/empleados/integration/LoginPerformanceIntegrationTest.java`
- [ ] T038 [P] Publicar evidencia de medicion p95 y resultados en `specs/001-autenticacion-empleados/checklists/performance.md`
- [ ] T039 Ejecutar regression suite de autenticacion y endpoints protegidos en `src/test/java/com/example/empleados`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: inicia sin dependencias.
- **Phase 2 (Foundational)**: depende de Setup y bloquea historias.
- **Phase 3 (US1)**: depende de Foundational.
- **Phase 4 (US2)**: depende de Foundational y usa emision JWT de US1 para pruebas end-to-end.
- **Phase 5 (US3)**: depende de Foundational y del flujo JWT ya implementado.
- **Phase 6 (Polish)**: depende de historias completadas.

### User Story Dependencies

- **US1 (P1)**: independiente tras fundacion; define MVP.
- **US2 (P2)**: independiente funcionalmente, pero requiere token emitido para pruebas completas.
- **US3 (P3)**: requiere flujo de login y validacion token para validar transicion activa/inactiva.

### Within Each User Story

- Pruebas primero y en estado fallido inicial.
- DTO/entidades antes de servicios.
- Servicios antes de controladores/filtros.
- Integracion/regresion al final de la historia.

## Parallel Opportunities

- Setup en paralelo: `T003`, `T004`.
- Foundational en paralelo: `T006`, `T007`, `T008`, `T009`, `T010`.
- US1 en paralelo: `T013`-`T019`.
- US2 en paralelo: `T023`-`T026`.
- US3 en paralelo: `T031`, `T032`.
- Polish en paralelo: `T035`-`T038`.

## Parallel Example: User Story 1

```bash
T013 Contract login
T014 Login exitoso
T015 Credenciales invalidas
T016 Cuenta inactiva
T017 Sin lockout
T018 DTO LoginRequest
T019 DTO LoginResponse
```

## Parallel Example: User Story 2

```bash
T023 OpenAPI bearer
T024 Sin token
T025 Token invalido
T026 Token expirado
```

## Parallel Example: User Story 3

```bash
T031 Token vigente tras inactivar
T032 Nuevo login rechazado tras inactivar
```

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Setup.
2. Completar Foundational.
3. Completar US1.
4. Validar MVP con pruebas de login y no-lockout.

### Incremental Delivery

1. Setup + Foundational.
2. US1 (login JWT).
3. US2 (proteccion Bearer).
4. US3 (regla estado activo).
5. Polish + performance + regression.

### Parallel Team Strategy

1. Equipo completo en Setup/Foundational.
2. Luego distribuir historias por desarrollador.
3. Integrar y ejecutar regression final.

## Notes

- Formato checklist aplicado: `- [ ] T### [P?] [US?] Descripcion con ruta`.
- Labels `[US1]`, `[US2]`, `[US3]` solo aparecen en fases de historias.
- Cada historia es implementable y comprobable de forma independiente.
