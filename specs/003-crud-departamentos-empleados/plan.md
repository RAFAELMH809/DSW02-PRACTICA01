# Implementation Plan: CRUD de Departamentos Vinculado a Empleados

**Branch**: `003-crud-departamentos-empleados` | **Date**: 2026-03-12 | **Spec**: [specs/003-crud-departamentos-empleados/spec.md](./spec.md)
**Input**: Feature specification from `/specs/003-crud-departamentos-empleados/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Implementar un CRUD de departamentos integrado al dominio actual de empleados, agregando la entidad
`Departamento` con `clave` y `nombre`, endpoints protegidos con Bearer token, paginacion para el
listado, auditoria en logs y reglas de integridad donde cada empleado pertenece obligatoriamente a
exactamente un departamento. La solucion se implementara en el backend Spring Boot 3 + Java 17 ya
existente, con PostgreSQL y migraciones versionadas para crear la tabla de departamentos, poblar el
departamento tecnico `SIN_DEPTO` y vincular empleados historicos sin departamento.

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3 (Web, Security, Data JPA, Validation), springdoc-openapi, Flyway, PostgreSQL Driver  
**Storage**: PostgreSQL  
**Testing**: JUnit 5, Spring Boot Test, MockMvc, Spring Security Test, Testcontainers (PostgreSQL)  
**Target Platform**: Linux server / contenedor Docker
**Project Type**: web-service backend REST  
**Performance Goals**: p95 < 2 s para consultas y listados paginados de departamentos  
**Constraints**: `clave` de departamento inmutable y case-insensitive, relacion 1:N departamento-empleados con FK obligatoria, migracion tecnica con `SIN_DEPTO`, Bearer token igual que empleados, auditoria por logs, politica last-write-wins sin optimistic locking, sin target adicional para escrituras  
**Scale/Scope**: 1 nueva entidad principal, 5 operaciones CRUD de departamentos, cambios en create/update/list/read de empleados para incluir referencia de departamento

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

## Constitution Check (Pre-Phase 0)

- Stack gate: **PASS** - Se mantiene Spring Boot 3.x con Java 17 y arquitectura por capas existente.
- Security gate: **PASS** - Los endpoints de departamentos reutilizan JWT Bearer y el mismo modelo de autorizacion que empleados.
- Data gate: **PASS** - PostgreSQL sigue siendo la persistencia principal y el feature define migraciones versionadas para departamentos y relacion con empleados.
- Environment gate: **PASS** - El proyecto ya usa Docker/Compose y Testcontainers para ejecucion reproducible.
- API contract gate: **PASS** - Se contempla contrato OpenAPI para endpoints nuevos y ajustes del contrato de empleados.
- Quality gate: **PASS** - Se definira cobertura unitaria, integracion y contrato para CRUD de departamentos y relacion con empleados.
- Frontend gate (Principle VI): **PARTIAL (N/A condicionado)** - Este feature es backend-only y no implementa Angular 22 LTS ni flujos Cypress/login. Para el camino de Empleados existe artefacto verificable de autenticacion en `specs/001-autenticacion-empleados/contracts/autenticacion.openapi.yaml` (endpoint `POST /api/v2/auth/login`). El segundo camino (login de Departamentos) queda como dependencia pendiente y su criterio de salida verificable es contar con contrato OpenAPI en `specs/003-crud-departamentos-empleados/contracts/departamentos-autenticacion.openapi.yaml` que documente `POST /api/v2/departamentos/auth/login` y pruebas E2E Cypress asociadas en el repositorio frontend.

## Constitution Check (Post-Phase 1 Design)

- Stack gate: **PASS** - El diseno utiliza `controller`, `service`, `repository`, `domain`, `dto` y migraciones dentro del modulo actual.
- Security gate: **PASS** - Los endpoints nuevos quedan protegidos con Bearer token, sin roles adicionales.
- Data gate: **PASS** - El modelo incluye `Departamento`, referencia obligatoria desde `Empleado` y migracion controlada con `SIN_DEPTO`.
- Environment gate: **PASS** - La estrategia local/integracion sigue soportada por Docker Compose y perfiles Spring.
- API contract gate: **PASS** - El contrato OpenAPI para departamentos y la actualizacion del contrato de empleados quedan planificados en `tasks.md`.
- Quality gate: **PASS** - La cobertura de pruebas (contrato/integracion/seguridad/auditoria/performance) queda planificada en `tasks.md` y se valida durante implementacion.
- Frontend gate (Principle VI): **PARTIAL (N/A condicionado)** - El diseno de Phase 1 se limita a contratos y modelo backend, por lo que no materializa Angular/Cypress ni las dos rutas de login. Se valida trazabilidad del camino de Empleados con `specs/001-autenticacion-empleados/contracts/autenticacion.openapi.yaml`; permanece pendiente el camino de Departamentos hasta disponer del artefacto `specs/003-crud-departamentos-empleados/contracts/departamentos-autenticacion.openapi.yaml` y su suite Cypress de login en frontend.

## Project Structure

### Documentation (this feature)

```text
specs/003-crud-departamentos-empleados/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── checklists/
│   ├── environment.md
│   ├── acceptance.md
│   └── frontend-auth-e2e.md
├── contracts/
│   └── departamentos.openapi.yaml
│   └── departamentos-autenticacion.openapi.yaml
└── tasks.md
```

### Source Code (repository root)

```text
src/
└── main/
  ├── java/com/example/empleados/
  │   ├── config/
  │   ├── controller/
  │   ├── domain/
  │   ├── dto/
  │   ├── exception/
  │   ├── repository/
  │   └── service/
  │       └── impl/
  └── resources/
    ├── application.yml
    ├── application-dev.yml
    └── db/migration/

src/
└── test/
  └── java/com/example/empleados/
    ├── contract/
    ├── integration/
    └── unit/

docker/
└── docker-compose.yml
```

**Structure Decision**: Proyecto unico backend Spring Boot. El feature agrega un nuevo agregado de
dominio `Departamento` y ajusta el agregado `Empleado` existente para mantener la relacion
obligatoria por `clave`, reutilizando la estructura actual de paquetes, seguridad y pruebas.

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Principle VI (Frontend gate) en estado PARTIAL (N/A condicionado) | El alcance aprobado del feature es backend-only y se prioriza cerrar contratos y reglas de negocio de departamentos/empleados sin bloquear la entrega por UI Angular. Ya existe evidencia verificable del camino de Empleados en `specs/001-autenticacion-empleados/contracts/autenticacion.openapi.yaml`; el camino de login de Departamentos depende de un frente posterior. | Marcar PASS ahora seria incorrecto porque aun no existen implementacion Angular 22 LTS ni pruebas Cypress para login de Departamentos. Cambiar alcance para incluir frontend en este feature rompe la planificacion y dependencias definidas en el backlog. |
