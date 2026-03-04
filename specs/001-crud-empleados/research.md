# Phase 0 Research - CRUD de Empleados

## Decision 1: Formato de `clave` del empleado
- **Decision**: Usar `clave` alfanumérica obligatoria, única y de longitud máxima 100 caracteres.
- **Rationale**: Mantiene consistencia con la restricción solicitada de longitud en los campos principales, evita dependencia de formato numérico/UUID y simplifica validación para MVP.
- **Alternatives considered**:
  - Clave numérica: obliga reglas adicionales no pedidas (rango, formato).
  - UUID: agrega complejidad y verbosidad para captura manual.

## Decision 2: Persistencia y migraciones
- **Decision**: Persistir en PostgreSQL con migración versionada inicial para tabla `empleados`.
- **Rationale**: Cumple constitución del proyecto y mantiene trazabilidad de cambios de esquema.
- **Alternatives considered**:
  - Base embebida para desarrollo: descartada por romper paridad entre entornos.
  - SQL manual sin versionado: descartado por falta de control evolutivo.

## Decision 3: Seguridad de acceso
- **Decision**: Proteger todos los endpoints CRUD de empleados con HTTP Basic; solo endpoints técnicos (p. ej. health) pueden ser públicos.
- **Rationale**: Alinea con constitución y reduce ambigüedad de autorización en pruebas y aceptación.
- **Alternatives considered**:
  - Proteger solo escrituras: deja lecturas expuestas sin requerimiento explícito.
  - Control por roles complejos: fuera de alcance del MVP.

## Decision 4: Contrato y documentación
- **Decision**: Definir contrato OpenAPI 3 para CRUD completo de empleados con esquemas y códigos de error.
- **Rationale**: Permite validación temprana del contrato y trazabilidad con los requisitos funcionales.
- **Alternatives considered**:
  - Documentación solo textual: insuficiente para validación automática.

## Decision 5: Estrategia de pruebas
- **Decision**: Cubrir unit tests de validaciones (longitud, obligatoriedad, duplicidad) e integration tests de CRUD autenticado contra PostgreSQL en Docker.
- **Rationale**: Cumple gate de calidad y verifica flujos críticos de negocio y seguridad.
- **Alternatives considered**:
  - Solo pruebas unitarias: no verifica integración real con persistencia y autenticación.
