# Feature Specification: CRUD de Empleados

**Feature Branch**: `001-crud-empleados`  
**Created**: 2026-02-25  
**Status**: Draft  
**Input**: User description: "Modificar especificación para que `clave` sea un prefijo `EMP-` seguido de un autonumérico como PK compuesta, manteniendo `nombre`, `dirección` y `teléfono` con máximo de 100 caracteres."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Registrar empleados (Priority: P1)

Como usuario autorizado, quiero registrar empleados con nombre, dirección y teléfono para comenzar a construir el catálogo de personal, dejando que el sistema genere la clave.

**Why this priority**: Sin alta de empleados no existe información base para consultar, actualizar o eliminar.

**Independent Test**: Puede probarse creando un empleado válido y verificando que queda disponible para consulta posterior.

**Acceptance Scenarios**:

1. **Given** un usuario autorizado, **When** registra un empleado con nombre, dirección y teléfono válidos, **Then** el sistema guarda el empleado, genera la clave con formato `EMP-<autonumérico>` y confirma su creación.
2. **Given** un usuario autorizado, **When** intenta registrar un empleado con nombre, dirección o teléfono mayores a 100 caracteres, **Then** el sistema rechaza la operación con mensaje de validación claro.
3. **Given** múltiples altas concurrentes de empleados, **When** el sistema genera claves para cada alta, **Then** cada clave resultante es única y conserva el prefijo `EMP-`.

---

### User Story 2 - Consultar empleados (Priority: P2)

Como usuario autorizado, quiero consultar empleados por clave y en listado para ubicar información del personal cuando la necesite.

**Why this priority**: La consulta entrega valor inmediato al negocio al permitir usar la información capturada.

**Independent Test**: Puede probarse con datos existentes verificando consulta individual por clave y consulta de listado general.

**Acceptance Scenarios**:

1. **Given** empleados previamente registrados, **When** el usuario solicita la lista de empleados, **Then** el sistema devuelve todos los registros disponibles.
2. **Given** una clave existente, **When** el usuario consulta el empleado por clave, **Then** el sistema devuelve los datos completos del empleado.
3. **Given** una clave inexistente, **When** el usuario consulta por clave, **Then** el sistema indica que el empleado no existe.

---

### User Story 3 - Actualizar y eliminar empleados (Priority: P3)

Como usuario autorizado, quiero actualizar y eliminar empleados para mantener el catálogo exacto y sin registros obsoletos.

**Why this priority**: Completa el ciclo CRUD y asegura calidad de datos en el tiempo.

**Independent Test**: Puede probarse modificando campos válidos de un empleado y eliminando un registro existente, verificando resultados en consultas.

**Acceptance Scenarios**:

1. **Given** un empleado existente, **When** el usuario actualiza nombre, dirección y/o teléfono con valores válidos, **Then** el sistema guarda los cambios y devuelve el registro actualizado.
2. **Given** un empleado existente, **When** el usuario elimina el empleado, **Then** el sistema confirma la eliminación y el registro deja de aparecer en consultas.
3. **Given** una clave inexistente, **When** el usuario intenta actualizar o eliminar, **Then** el sistema informa que no existe un empleado asociado.

---

### Edge Cases

- Campos de texto exactamente de 100 caracteres se aceptan; con 101 o más se rechazan.
- Campos de texto vacíos o compuestos solo por espacios se rechazan.
- Consultas, actualizaciones o eliminaciones con clave que no cumpla el patrón `EMP-<autonumérico>` se rechazan.
- Intentos de operación sin credenciales válidas se rechazan.
- Eliminación repetida de la misma clave después de una eliminación exitosa devuelve "no encontrado".
- Consulta de listado sin empleados devuelve resultado vacío, no error.

## Assumptions

- La `clave` es una PK compuesta lógica por dos componentes: prefijo fijo `EMP-` + secuencia numérica autogenerada.
- En el alta, el usuario proporciona únicamente `nombre`, `dirección` y `teléfono`; la `clave` la genera el sistema.
- La `clave` generada no puede modificarse durante actualización.
- Este alcance se limita a un único tipo de usuario autorizado para operaciones CRUD.
- El comportamiento de validación de longitud aplica en creación y actualización.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST permitir registrar empleados con los campos `nombre`, `dirección` y `teléfono`.
- **FR-002**: El sistema MUST generar automáticamente la `clave` con formato `EMP-<autonumérico>` para cada alta exitosa.
- **FR-003**: El sistema MUST limitar `nombre`, `dirección` y `teléfono` a un máximo de 100 caracteres cada uno.
- **FR-004**: El sistema MUST tratar la `clave` como llave primaria compuesta por prefijo y componente numérico, garantizando unicidad por registro.
- **FR-005**: El sistema MUST permitir consultar un empleado por `clave`.
- **FR-006**: El sistema MUST permitir consultar el listado de empleados.
- **FR-007**: El sistema MUST permitir actualizar `nombre`, `dirección` y `teléfono` de un empleado existente usando su `clave`, sin permitir modificar la `clave`.
- **FR-008**: El sistema MUST permitir eliminar un empleado existente usando su `clave`.
- **FR-009**: El sistema MUST validar que la `clave` usada en operaciones por identificador cumpla el patrón `EMP-<autonumérico>`.
- **FR-010**: El sistema MUST devolver mensajes claros cuando una operación falle por validación, formato de clave, inexistencia o falta de autorización.
- **FR-011**: El sistema MUST requerir autenticación para todas las operaciones CRUD de empleados conforme a la política de seguridad vigente.
- **FR-012**: El sistema MUST documentar las operaciones CRUD y sus reglas de validación en el contrato público del servicio.

**Constitution-aligned mandatory constraints (backend projects):**

- **AUTH-001**: Operaciones CRUD de empleados MUST exigir autenticación según la política de seguridad del proyecto.
- **DATA-001**: El modelo de empleados MUST persistirse en un almacenamiento relacional del proyecto, incluyendo cambios de esquema versionados.
- **ENV-001**: El entorno de ejecución local e integración MUST ser reproducible.
- **DOC-001**: El contrato del servicio MUST estar publicado y mantenerse actualizado.
- **QUAL-001**: La entrega MUST incluir pruebas que cubran validación de longitud, unicidad de clave y operaciones CRUD autorizadas.

### Key Entities *(include if feature involves data)*

- **Empleado**: Representa un registro de personal con atributos `clave` (PK compuesta lógica: prefijo fijo `EMP-` + secuencia numérica), `nombre` (texto hasta 100), `dirección` (texto hasta 100) y `teléfono` (texto hasta 100).

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de intentos de alta con `nombre`, `dirección` y `teléfono` de más de 100 caracteres son rechazados.
- **SC-002**: El 100% de claves generadas en altas exitosas cumplen el patrón `EMP-<autonumérico>` y no presentan duplicados.
- **SC-003**: Usuarios autorizados completan operaciones individuales de crear, consultar, actualizar o eliminar en menos de 10 segundos en al menos 95% de los intentos.
- **SC-004**: Al menos 95% de casos de prueba definidos para CRUD de empleados pasan en validación previa a entrega.
