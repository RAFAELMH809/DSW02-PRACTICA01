# Data Model - CRUD de Empleados

## Entity: Empleado

### Fields
- `clave_prefijo` (string, PK component, required, fixed value: `EMP-`)
- `clave_numero` (integer, PK component, required, auto-increment sequence)
- `clave` (string, generated, unique, format: `EMP-<autonumérico>`, read-only)
- `nombre` (string, required, maxLength: 100)
- `direccion` (string, required, maxLength: 100)
- `telefono` (string, required, maxLength: 100)
- `created_at` (timestamp, required, system managed)
- `updated_at` (timestamp, required, system managed)

## Validation Rules
- En alta, los campos de entrada obligatorios son `nombre`, `direccion` y `telefono`.
- `clave_prefijo` y `clave_numero` se generan por sistema y conforman la PK compuesta.
- `clave` se deriva de ambos componentes y debe cumplir patrón `EMP-<autonumérico>`.
- `clave` no es editable en operaciones de actualización.
- `nombre`, `direccion` y `telefono` aceptan 1..100 caracteres (trim aplicado para validación).
- Valores vacíos o con solo espacios se consideran inválidos.

## Relationships
- No hay relaciones obligatorias con otras entidades en este alcance.

## State Transitions
- **Created**: empleado registrado exitosamente con `clave` autogenerada en formato `EMP-<autonumérico>`.
- **Updated**: empleado existente modificado en `nombre`, `direccion` y/o `telefono`.
- **Deleted**: empleado removido lógicamente del catálogo operativo (en este alcance se modela como eliminación definitiva).
