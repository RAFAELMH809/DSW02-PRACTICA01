# Implementation Plan: CRUD de Empleados

**Branch**: `001-crud-empleados` | **Date**: 2026-02-26 | **Spec**: [specs/001-crud-empleados/spec.md](./spec.md)
**Input**: Feature specification from `/specs/001-crud-empleados/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Implementar un CRUD de empleados con campos `clave`, `nombre`, `direccion` y `telefono`, donde
la `clave` se genera automáticamente con formato `EMP-<autonumérico>` y funciona como PK
compuesta lógica (prefijo fijo + secuencia numérica). Los campos de texto de negocio tienen
máximo 100 caracteres. La solución se diseñará como servicio backend en Spring Boot 3 + Java 17,
protegido con HTTP Basic, persistencia en PostgreSQL con migraciones versionadas, ejecución local
con Docker y contrato documentado en OpenAPI/Swagger.

## Technical Context

<!--
  ACTION REQUIRED: Replace the content in this section with the technical details
  for the project. The structure here is presented in advisory capacity to guide
  the iteration process.
-->

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3 (Web, Security, Data JPA, Validation), springdoc-openapi, Flyway, PostgreSQL Driver  
**Storage**: PostgreSQL  
**Testing**: JUnit 5, Spring Boot Test, MockMvc, Testcontainers (PostgreSQL)  
**Target Platform**: Linux server / contenedor Docker
**Project Type**: web-service backend REST  
**Performance Goals**: p95 < 500 ms para operaciones CRUD simples con dataset pequeño/medio  
**Constraints**: HTTP Basic obligatorio, `nombre`/`direccion`/`telefono` con máximo 100, `clave` autogenerada con patrón `EMP-<autonumérico>`, migraciones versionadas, sin secretos en repositorio  
**Scale/Scope**: MVP de una entidad (`Empleado`) con 5 endpoints CRUD y generación automática de identificador

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

## Constitution Check (Pre-Phase 0)

- Stack gate: **PASS** - Se define Spring Boot 3 + Java 17.
- Security gate: **PASS** - CRUD protegido por HTTP Basic; secretos por entorno.
- Data gate: **PASS** - PostgreSQL único con migraciones versionadas.
- Environment gate: **PASS** - Estrategia con Docker/Compose para entorno reproducible.
- API contract gate: **PASS** - Contrato OpenAPI/Swagger requerido.
- Quality gate: **PASS** - Se define cobertura de pruebas unitarias e integración.

## Constitution Check (Post-Phase 1 Design)

- Stack gate: **PASS** - Diseño en estructura Spring estándar por capas.
- Security gate: **PASS** - Contrato aplica `basicAuth` global para endpoints CRUD.
- Data gate: **PASS** - `data-model.md` define entidad y reglas; migración inicial prevista.
- Environment gate: **PASS** - `quickstart.md` incluye arranque de PostgreSQL con Docker.
- API contract gate: **PASS** - `contracts/empleados.openapi.yaml` define endpoints y respuestas.
- Quality gate: **PASS** - Estrategia de pruebas explicitada en `research.md` y quickstart.

## Project Structure

### Documentation (this feature)

```text
specs/001-crud-empleados/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── empleados.openapi.yaml
└── tasks.md             # Phase 2 output (/speckit.tasks)
```

### Source Code (repository root)
<!--
  ACTION REQUIRED: Replace the placeholder tree below with the concrete layout
  for this feature. Delete unused options and expand the chosen structure with
  real paths (e.g., apps/admin, packages/something). The delivered plan must
  not include Option labels.
-->

```text
src/
└── main/
  ├── java/
  │   └── com/example/empleados/
  │       ├── controller/
  │       ├── service/
  │       ├── repository/
  │       ├── domain/
  │       ├── dto/
  │       └── config/
  └── resources/
    ├── db/migration/
    └── application.yml

src/
└── test/
  └── java/com/example/empleados/
    ├── unit/
    ├── integration/
    └── contract/

docker-compose.yml
```

**Structure Decision**: Se adopta proyecto único backend Spring Boot con separación por capas,
tests por tipo (unit/integration/contract) y `docker-compose.yml` para PostgreSQL local.

## Complexity Tracking

> No se registran violaciones constitucionales en esta planificación.

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| N/A | N/A | N/A |
