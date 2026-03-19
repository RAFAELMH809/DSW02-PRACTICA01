# Data Model - CRUD de Departamentos Vinculado a Empleados

## Entity: Departamento

### Fields
- `clave` (string, required, unique case-insensitive, immutable)
- `nombre` (string, required)
- `created_at` (timestamp, required, system managed)
- `updated_at` (timestamp, required, system managed)

### Validation Rules
- `clave` no puede ser nula, vacia o solo espacios.
- `clave` debe ser unica sin distinguir mayusculas/minusculas.
- `clave` no puede cambiarse despues de crear el registro.
- `nombre` no puede ser nulo, vacio o solo espacios.
- `SIN_DEPTO` es una clave reservada de sistema y no puede eliminarse.

## Entity: Empleado (extension del modelo existente)

### Fields (relevantes para este feature)
- `clave` (string, PK funcional existente)
- `departamento_clave` (string, required, FK -> Departamento.clave)
- `nombre` (string, required)
- `direccion` (string, required)
- `telefono` (string, required)
- `created_at` (timestamp, required)
- `updated_at` (timestamp, required)

### Validation Rules
- `departamento_clave` es obligatorio al crear empleado.
- `departamento_clave` debe existir en Departamento.
- Un empleado pertenece a exactamente un departamento.
- Un departamento puede tener cero o muchos empleados.

## Derived View/Data
- `employeeCount` (integer, derived): total de empleados por departamento para respuestas de consulta/listado.

## Relationships
- `Departamento (1)` -> `Empleado (N)` mediante `Empleado.departamento_clave`.
- Integridad referencial obligatoria en base de datos y validacion de negocio en capa de servicio.

## Migration Notes
- Crear tabla `departamentos`.
- Insertar departamento tecnico `SIN_DEPTO`.
- Backfill: asignar `SIN_DEPTO` a empleados historicos sin departamento.
- Aplicar constraint NOT NULL + FK en `empleados.departamento_clave`.

## Deletion Rules
- Prohibido eliminar `SIN_DEPTO`.
- Prohibido eliminar cualquier departamento con empleados asociados.
- Permitido eliminar departamento sin empleados (excepto `SIN_DEPTO`).
