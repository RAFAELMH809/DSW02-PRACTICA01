<!--
Sync Impact Report
- Version change: 3.2.0 -> 3.2.1
- Modified principles:
  - V. Calidad verificable con pruebas automatizadas
  - VI. Frontend Web Obligatorio (Angular 22 LTS + Cypress)
- Added sections:
  - Ninguna
- Removed sections:
  - Ninguna
- Templates requiring updates:
  - ✅ updated: .specify/templates/plan-template.md
  - ✅ updated: .specify/templates/spec-template.md
  - ✅ updated: .specify/templates/tasks-template.md
  - ✅ no action required: .specify/templates/commands/*.md (directorio no existe en este repositorio)
  - ✅ updated: frontend/README.md
- Follow-up TODOs:
  - Ninguno
-->

# DSW02-Practica01 Constitution

## Core Principles

### I. Plataforma Backend Obligatoria (Spring Boot 3 + Java 17)
Toda implementación de backend MUST usar Spring Boot 3.x sobre Java 17. Se MUST mantener
consistencia en arquitectura por capas (controller, service, repository) y configuración por
perfiles (`dev`, `test`, `prod`). Se MUST evitar introducir frameworks alternos para APIs HTTP
sin enmienda formal de esta constitución.

Rationale: un stack único reduce complejidad operativa, acelera onboarding y mejora
mantenibilidad.

### II. Seguridad API con JWT Bearer
Los endpoints protegidos MUST requerir autenticación JWT Bearer mediante Spring Security. El
backend MUST exponer un flujo de login para emitir tokens firmados con expiración definida por
entorno y validación de integridad/tiempo de vida en cada request protegida. Los secretos de
firma MUST resolverse fuera del código fuente y rotarse por política operativa.
En producción, JWT Bearer MUST operar exclusivamente sobre HTTPS.

Rationale: JWT Bearer habilita APIs stateless, mejora escalabilidad y permite control explícito de
expiración/rechazo de tokens en contratos y pruebas automatizadas.

### III. Persistencia PostgreSQL y Docker como estándar
La persistencia de datos MUST realizarse en PostgreSQL. El entorno local y de integración MUST
ejecutar PostgreSQL con Docker/Compose para asegurar reproducibilidad. Todo cambio de esquema
MUST versionarse con migraciones (Flyway o Liquibase) y aplicarse de forma automática en
arranque o pipeline.

Rationale: un motor único y entornos reproducibles eliminan desviaciones entre desarrollo,
pruebas y despliegue.

### IV. Contrato API y documentación OpenAPI/Swagger
Toda API REST MUST estar documentada con OpenAPI 3 y publicada con Swagger UI. Cada endpoint
MUST incluir descripción funcional, esquemas de request/response, códigos de error y requisitos
de autenticación. Ningún endpoint nuevo o modificado se considera terminado sin su documentación
actualizada.

Rationale: la documentación ejecutable evita ambigüedad contractual y reduce retrabajo entre
equipos consumidores.

### V. Calidad verificable con pruebas automatizadas
Cada cambio MUST incluir pruebas automáticas proporcionales al riesgo: unitarias para lógica de
negocio y seguridad, e integración para repositorios y flujos autenticados contra PostgreSQL en
contenedor. Para funcionalidades web frontend, las pruebas E2E MUST ejecutarse con Cypress en
flujos críticos de autenticación y CRUD. El pipeline MUST bloquear merge ante fallas de tests,
migraciones o build.

Rationale: la calidad verificable protege el contrato API, la seguridad y la estabilidad en
producción.

### VI. Frontend Web Obligatorio (Angular 22 LTS + Cypress)
El frontend oficial del proyecto MUST implementarse en Angular 22 LTS. Las pruebas del frontend
MUST ejecutarse con Cypress para validar flujos críticos de autenticación y CRUD. Cypress MUST
ser la herramienta E2E aprobada para frontend salvo enmienda formal de esta constitución. La
aplicación web MUST exponer dos caminos de autenticación independientes: uno para el CRUD de
Empleados y otro para el CRUD de Departamentos. El orden de entrega MUST ser secuencial: primero
se entrega el login de Empleados y después se implementa el login de Departamentos.

Rationale: una base frontend única con pruebas E2E uniformes reduce ambigüedad funcional y
permite validar navegación/autorización real para ambos módulos de negocio.

## Estándares Técnicos y Operativos

- Runtime MUST ser Java 17 LTS.
- Framework base MUST ser Spring Boot 3.x con módulos de seguridad, web y acceso a datos según
	necesidad de la funcionalidad.
- Base de datos oficial MUST ser PostgreSQL (misma familia de versión entre entornos).
- Contenedores MUST definirse en `docker-compose` para ejecución local y validación de integración.
- Configuración sensible MUST estar fuera del repositorio y resolverse por entorno.
- Logging de seguridad y errores MUST ser estructurado y suficiente para auditoría técnica.
- El dominio organizacional MUST soportar CRUD de departamentos con `clave` y `nombre`, y la
  relación obligatoria de cada `Empleado` con un `Departamento` válido.
- El frontend MUST usar Angular 22 LTS y las pruebas E2E MUST implementarse con Cypress.
- Cypress MUST NOT ser sustituido en pruebas E2E frontend sin enmienda constitucional aprobada.
- El sistema MUST incluir dos rutas de login separadas para Empleados y Departamentos con
  implementación secuencial (Empleados primero, Departamentos después).

## Flujo de Desarrollo y Quality Gates

1. Toda iniciativa MUST iniciar con `spec.md`, continuar con `plan.md` y desglosarse en
	 `tasks.md`.
2. Cada PR MUST evidenciar: cumplimiento de autenticación, migraciones aplicables,
	 documentación Swagger actualizada y tests ejecutados.
3. Un cambio que altere contrato API o esquema de datos MUST incluir estrategia de compatibilidad
	 y/o migración.
4. Ningún cambio se integra a rama principal sin revisión de cumplimiento constitucional.

## Governance
Esta constitución prevalece sobre prácticas ad-hoc del repositorio.

- Enmiendas MUST proponerse vía PR con justificación, impacto en templates y plan de adopción.
- La política de versionado constitucional MUST seguir SemVer:
	- MAJOR: eliminación o redefinición incompatible de principios.
	- MINOR: adición de principios/secciones o expansión normativa sustancial.
	- PATCH: clarificaciones editoriales sin cambio normativo.
- Toda revisión de `plan.md` y PR MUST incluir una verificación explícita de cumplimiento.
- Si existe conflicto entre documentos, este archivo y su versión vigente tienen prioridad.

**Version**: 3.2.1 | **Ratified**: 2026-02-25 | **Last Amended**: 2026-03-25
