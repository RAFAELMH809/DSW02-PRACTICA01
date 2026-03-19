# Implementation Plan: Autenticacion de Empleados

**Branch**: `001-autenticacion-empleados` | **Date**: 2026-03-11 | **Spec**: [specs/001-autenticacion-empleados/spec.md](./spec.md)
**Input**: Feature specification from `/specs/001-autenticacion-empleados/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Implementar autenticacion de empleados por correo y contrasena para emision de JWT Bearer con
expiracion exacta de 60 minutos, sin refresh token, y aplicar validacion de token en endpoints
protegidos de empleados. La solucion se implementara en Spring Boot 3 + Java 17, persistencia en
PostgreSQL con migraciones versionadas, ejecucion reproducible con Docker/Compose y contrato
OpenAPI que documente login, esquema Bearer y respuestas de error seguras.

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3 (Web, Security, Data JPA, Validation), springdoc-openapi, JWT library (jjwt o equivalente), Flyway, PostgreSQL Driver  
**Storage**: PostgreSQL  
**Testing**: JUnit 5, Spring Boot Test, MockMvc, Testcontainers (PostgreSQL)  
**Target Platform**: Linux server / contenedor Docker
**Project Type**: web-service backend REST  
**Performance Goals**: p95 < 2 s en validacion de autenticacion (alineado a SC-006)  
**Constraints**: JWT expira en 60 minutos exactos, sin refresh token, validacion de estado activo solo en login, mensajes de error sin filtrado de existencia de cuenta, secretos por entorno  
**Scale/Scope**: 1 endpoint de login + aplicacion de seguridad JWT sobre endpoints protegidos existentes de empleados

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

## Constitution Check (Pre-Phase 0)

- Stack gate: **PASS** - Se mantiene Spring Boot 3.x con Java 17 y arquitectura por capas.
- Security gate: **PASS** - La constitucion vigente define JWT Bearer para endpoints protegidos y el feature implementa login con emision/validacion de token conforme a dicha norma.
- Data gate: **PASS** - PostgreSQL unico y plan de migraciones versionadas para credenciales/eventos.
- Environment gate: **PASS** - Docker/Compose existente para ejecucion local/integracion reproducible.
- API contract gate: **PASS** - Se definira contrato OpenAPI 3 para `/api/v2/auth/login` y seguridad Bearer en recursos protegidos.
- Quality gate: **PASS** - Se define cobertura unitaria/integracion/contrato para login y acceso protegido.

## Constitution Check (Post-Phase 1 Design)

- Stack gate: **PASS** - Diseno en componentes Spring (`config`, `service`, `controller`, `repository`).
- Security gate: **PASS** - Contrato y quickstart quedan en JWT Bearer sin refresh token, alineado con la constitucion y la especificacion del feature.
- Data gate: **PASS** - `data-model.md` incluye entidades de autenticacion y estrategia de migraciones.
- Environment gate: **PASS** - `quickstart.md` usa `docker/docker-compose.yml` + perfiles Spring.
- API contract gate: **PASS** - `contracts/autenticacion.openapi.yaml` documenta endpoint de login y seguridad Bearer.
- Quality gate: **PASS** - Se cubren escenarios exitosos/fallidos, token expirado y acceso a recursos protegidos.

## Project Structure

### Documentation (this feature)

```text
specs/001-autenticacion-empleados/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── autenticacion.openapi.yaml
└── tasks.md             # Phase 2 output (/speckit.tasks)
```

### Source Code (repository root)

```text
src/
└── main/
  ├── java/com/example/empleados/
  │   ├── config/
  │   ├── controller/
  │   ├── service/
  │   ├── repository/
  │   ├── domain/
  │   ├── dto/
  │   └── exception/
  └── resources/
    ├── application.yml
    ├── application-dev.yml
    └── db/migration/

src/
└── test/
  └── java/com/example/empleados/
    ├── unit/
    ├── integration/
    └── contract/

docker/
└── docker-compose.yml
```

**Structure Decision**: Proyecto unico backend Spring Boot; el feature se implementa en el
modulo actual agregando capa de autenticacion JWT sobre endpoints de empleados, manteniendo tests
separados por tipo y PostgreSQL en Docker Compose.

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Ninguna | N/A | N/A |
