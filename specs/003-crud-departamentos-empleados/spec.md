# Feature Specification: CRUD de Departamentos Vinculado a Empleados

**Feature Branch**: `003-crud-departamentos-empleados`  
**Created**: 2026-03-12  
**Status**: Draft  
**Input**: User description: "003- crear un CRUD de departamentos unido con el de empleados deve de llevar clave y nombre"

## Clarifications

### Session 2026-03-12

- Q: Como se define la regla de identidad de `clave` de departamento (mutabilidad y mayusculas/minusculas)? → A: La `clave` es inmutable despues de crear y se compara de forma case-insensitive.
- Q: El departamento debe ser obligatorio al crear empleado? → A: Si, el empleado debe crearse con departamento obligatorio.
- Q: Como migrar empleados existentes sin departamento al volver obligatoria la relacion? → A: Crear `SIN_DEPTO` y asignarlo en migracion.
- Q: Como deben referenciar los empleados al departamento en las operaciones de negocio? → A: Referenciar por `clave` de departamento.
- Q: Se puede eliminar el departamento tecnico `SIN_DEPTO`? → A: No, `SIN_DEPTO` no se puede eliminar.

### Session 2026-03-12 (NFR)

- Q: Que nivel de autorizacion aplica para gestionar departamentos? → A: Misma que empleados: cualquier usuario autenticado con Bearer valido.
- Q: El listado de departamentos debe estar paginado? → A: Si, con paginacion usando parametros `page` y `size` igual que empleados.
- Q: Se requiere auditoria en operaciones CRUD de departamentos? → A: Si, registrar en log quien creo, actualizo o elimino un departamento (quien + que + cuando).
- Q: Existe target de tiempo para operaciones de escritura (crear, actualizar, eliminar)? → A: No, sin target de rendimiento para escrituras.
- Q: Cual es la politica de concurrencia en actualizaciones de departamento? → A: Last-write-wins: la ultima escritura sobreescribe sin control de version.

### Session 2026-03-19

- Q: Cual es el formato valido de `clave` para departamento? → A: `clave` MUST cumplir regex `^[A-Z0-9_]{2,20}$`.
- Q: Cual es el orden por defecto del listado paginado de departamentos? → A: Ordenar por `clave` ascendente (`clave ASC`).
- Q: Que codigo HTTP usar para `clave` de departamento duplicada? → A: Responder `409 Conflict`.
- Q: Cual es el tamano maximo permitido para `size` en paginacion? → A: `size` maximo permitido es 100.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Registrar departamentos (Priority: P1)

Como usuario autenticado, quiero crear departamentos con clave y nombre para organizar a los empleados por area.

**Why this priority**: Sin departamentos registrados no existe la base de la funcionalidad ni se puede vincular empleados.

**Independent Test**: Se prueba creando un departamento con clave y nombre validos y verificando que quede disponible para consulta posterior.

**Acceptance Scenarios**:

1. **Given** que no existe un departamento con la clave enviada, **When** el usuario autenticado registra un departamento con clave y nombre validos, **Then** el sistema crea el departamento y devuelve sus datos guardados.
2. **Given** que ya existe un departamento con la misma clave, **When** el usuario autenticado intenta registrarlo nuevamente, **Then** el sistema rechaza la operacion e informa que la clave debe ser unica.

---

### User Story 2 - Consultar y listar departamentos (Priority: P2)

Como usuario autenticado, quiero consultar un departamento por clave y listar departamentos para ubicar rapidamente la informacion y su relacion con empleados.

**Why this priority**: Permite usar la informacion creada y validar que la relacion con empleados este correctamente representada.

**Independent Test**: Se prueba consultando por clave y listando registros existentes para verificar exactitud y completitud de la informacion mostrada.

**Acceptance Scenarios**:

1. **Given** que existe un departamento registrado, **When** el usuario autenticado lo consulta por clave, **Then** el sistema devuelve su clave, nombre y la referencia de empleados asociados.
2. **Given** que existen multiples departamentos registrados, **When** el usuario autenticado solicita el listado indicando `page` y `size` validos, **Then** el sistema devuelve una pagina de resultados ordenada por defecto por `clave ASC`, con la informacion principal y `employeeCount` de cada departamento.
3. **Given** que el usuario autenticado recorre sucesivamente las paginas disponibles del listado, **When** completa la navegacion hasta la ultima pagina, **Then** puede recuperar el total de departamentos disponibles sin omisiones ni duplicados.
4. **Given** que no existe un departamento con la clave consultada, **When** el usuario autenticado realiza la consulta, **Then** el sistema responde que el departamento no fue encontrado.

Nota de salida funcional: la referencia de empleados asociados se representa como `employeeCount` en la respuesta de departamentos.

---

### User Story 3 - Actualizar y eliminar departamentos (Priority: P3)

Como usuario autenticado, quiero actualizar el nombre de un departamento y eliminar departamentos sin empleados asociados, para mantener la estructura organizacional actualizada.

**Why this priority**: Completa el ciclo CRUD y protege la consistencia de los empleados ya vinculados.

**Independent Test**: Se prueba modificando datos de un departamento existente y eliminando un departamento sin dependencias para confirmar comportamiento esperado.

**Acceptance Scenarios**:

1. **Given** que existe un departamento, **When** el usuario autenticado actualiza su nombre con un valor valido, **Then** el sistema guarda los cambios y devuelve la version actualizada.
2. **Given** que un departamento tiene empleados asociados, **When** el usuario autenticado intenta eliminarlo, **Then** el sistema rechaza la eliminacion e informa que primero deben desvincularse o reasignarse los empleados.
3. **Given** que un departamento no tiene empleados asociados, **When** el usuario autenticado lo elimina, **Then** el sistema elimina el registro correctamente.
4. **Given** el departamento tecnico `SIN_DEPTO`, **When** el usuario autenticado intenta eliminarlo, **Then** el sistema rechaza la operacion aunque no tenga empleados asociados.

---

### User Story 4 - Gestionar empleados con departamento obligatorio (Priority: P3)

Como usuario autenticado, quiero crear y actualizar empleados indicando una `clave` de departamento valida para mantener la relacion obligatoria entre empleados y departamentos.

**Why this priority**: Completa la integracion del nuevo dominio de departamentos con el CRUD de empleados y garantiza integridad referencial.

**Independent Test**: Se prueba creando y actualizando empleados con `departamentoClave` valida, invalida y ausente, verificando aceptacion o rechazo segun corresponda.

**Acceptance Scenarios**:

1. **Given** que existe un departamento valido, **When** se crea un empleado con `departamentoClave` valida, **Then** el sistema crea el empleado y lo vincula a ese departamento.
2. **Given** que no se envia `departamentoClave`, **When** se intenta crear un empleado, **Then** el sistema rechaza la operacion indicando que el departamento es obligatorio.
3. **Given** que se envia una `departamentoClave` inexistente, **When** se crea o actualiza un empleado, **Then** el sistema rechaza la operacion indicando que el departamento no existe.
4. **Given** empleados historicos sin departamento, **When** se ejecuta la migracion del feature, **Then** quedan asignados al departamento tecnico `SIN_DEPTO`.

### Edge Cases

- Creacion o actualizacion con clave vacia, nula o con formato invalido.
- Creacion o actualizacion con nombre vacio, nulo o que solo contiene espacios.
- Creacion de `clave` repetida usando distinta combinacion de mayusculas/minusculas (por ejemplo, `RH` y `rh`).
- Eliminacion o consulta de un departamento inexistente.
- Intento de vincular un empleado a un departamento inexistente.
- Intento de crear empleado sin departamento.
- Existencia de empleados historicos sin departamento al aplicar la migracion.
- Intento de eliminar el departamento tecnico `SIN_DEPTO`.
- Dos usuarios actualizando el nombre del mismo departamento de forma simultanea (last-write-wins, sin error de conflicto).
- Solicitudes de listado con `size` mayor a 100 se rechazan por exceder limite permitido.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST permitir crear departamentos con los campos obligatorios `clave` y `nombre`.
- **FR-001a**: La `clave` de departamento MUST cumplir el patron `^[A-Z0-9_]{2,20}$`.
- **FR-002**: El sistema MUST garantizar que la `clave` de departamento sea unica dentro del catalogo y comparada de forma case-insensitive.
- **FR-003**: El sistema MUST permitir consultar un departamento por su clave.
- **FR-004**: El sistema MUST permitir listar departamentos exclusivamente mediante paginacion usando parametros `page` y `size`, con valores predeterminados `page=0` y `size=10`, devolviendo `employeeCount` por departamento y orden por defecto `clave ASC`; para recuperar la totalidad del catalogo se deben recorrer todas las paginas.
- **FR-004a**: El sistema MUST validar `size` en rango permitido y rechazar valores mayores a 100.
- **FR-005**: El sistema MUST permitir actualizar solo el `nombre` de un departamento existente.
- **FR-005a**: El sistema MUST tratar la `clave` de departamento como inmutable despues de su creacion.
- **FR-006**: El sistema MUST permitir eliminar departamentos que no tengan empleados asociados, excepto el departamento tecnico `SIN_DEPTO`.
- **FR-007**: El sistema MUST impedir eliminar departamentos que tengan empleados asociados, mostrando un mensaje claro de validacion.
- **FR-008**: El sistema MUST mantener la relacion 1:N entre `Departamento` y `Empleado`: un departamento puede tener cero o muchos empleados, y cada empleado debe pertenecer a exactamente un departamento.
- **FR-009**: El sistema MUST validar que al registrar o actualizar un empleado, el departamento referenciado exista.
- **FR-010**: El sistema MUST devolver errores de negocio claros para casos de duplicidad, datos invalidos y recursos no encontrados.
- **FR-010a**: En creacion de departamento con `clave` duplicada, el sistema MUST responder `409 Conflict`.
- **FR-011**: El sistema MUST exigir `departamento` obligatorio al crear empleados; no se permiten empleados sin departamento.
- **FR-012**: En migracion de datos, el sistema MUST crear un departamento tecnico `SIN_DEPTO` y asignarlo a empleados historicos sin departamento antes de imponer restriccion obligatoria.
- **FR-013**: En operaciones de empleados, la referencia de departamento MUST realizarse mediante `clave` de departamento.
- **FR-014**: El sistema MUST prohibir la eliminacion del departamento tecnico `SIN_DEPTO` en cualquier circunstancia.
- **FR-015**: El sistema MUST registrar en log cada operacion de creacion, actualizacion y eliminacion de departamento, incluyendo identidad del usuario autenticado, tipo de operacion y marca de tiempo.

**Constitution-aligned mandatory constraints (backend projects):**

- **AUTH-001**: Endpoints de departamentos MUST requerir Bearer token valido, con identico modelo de autorizacion que los endpoints de empleados; no se requiere rol especial adicional.
- **DATA-001**: Los nuevos datos persistidos MUST quedar definidos en PostgreSQL e incluir su impacto de migracion.
- **ENV-001**: La ejecucion local e integracion MUST seguir siendo reproducible mediante Docker/Compose.
- **DOC-001**: Endpoints nuevos o modificados MUST incluir requisitos de documentacion OpenAPI/Swagger.
- **QUAL-001**: La feature MUST definir pruebas unitarias e integracion para operaciones CRUD y reglas de relacion con empleados.

### Key Entities *(include if feature involves data)*

- **Departamento**: Unidad organizacional con `clave` (inmutable y unica case-insensitive) y `nombre`; puede tener cero o muchos empleados asociados (1:N).
- **Empleado**: Persona gestionada por el sistema que pertenece a exactamente un departamento, referenciado por `clave`; la relacion es obligatoria (N:1).

### Assumptions

- La gestion de departamentos sera realizada por cualquier usuario autenticado con Bearer valido, sin distincion de rol especial; mismo modelo de autorizacion que empleados.
- La clave de departamento sera tratada como identificador funcional para operaciones de consulta y mantenimiento.
- Un empleado no puede pertenecer simultaneamente a mas de un departamento; la relacion es exactamente 1 departamento por empleado (N:1).
- Todo empleado nuevo debe quedar asociado a un departamento valido desde su creacion.
- La politica de concurrencia en actualizaciones de departamento es last-write-wins; no se implementa optimistic locking.

### Dependencies

- Disponibilidad de la autenticacion existente para proteger operaciones administrativas.
- Actualizacion del modelo actual de empleados para incluir la referencia al departamento.
- Migraciones de base de datos para crear estructura de departamentos y relacion con empleados.
- Migracion controlada de datos existentes para asignar `SIN_DEPTO` donde falte departamento.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de operaciones de creacion de departamentos con clave y nombre validos se completan exitosamente en pruebas funcionales.
- **SC-002**: Al menos el 95% de consultas y listados de departamentos devuelven resultado en menos de 2 segundos bajo carga operativa normal, incluyendo respuestas paginadas.
- **SC-003**: El 100% de intentos de eliminar departamentos con empleados asociados son bloqueados con mensaje de validacion comprensible.
- **SC-004**: Al menos el 95% de casos de uso CRUD de departamentos se completan sin asistencia adicional durante pruebas de aceptacion con usuarios del equipo.
- **SC-005**: El 100% de operaciones de escritura sobre departamentos (crear, actualizar, eliminar) generan entrada de log con usuario, operacion y marca de tiempo verificable en pruebas de integracion.
