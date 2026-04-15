# Feature Specification: CRUD de Departamentos Vinculado a Empleados

**Feature Branch**: `003-crud-departamentos-empleados`  
**Created**: 2026-03-12  
**Status**: Ready  
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

### Session 2026-03-25

- Q: Cual es el mapeo HTTP para errores funcionales principales de departamentos? → A: `404` no encontrado, `400` validacion/formato invalido y `409` para eliminacion bloqueada por dependencias o regla de negocio.
- Q: Que nivel de detalle debe devolver `GET /departamentos/{clave}` sobre empleados asociados? → A: Devolver solo `employeeCount`, sin lista embebida de empleados.
- Q: Como se maneja `clave` en operaciones por identificador y validacion de referencia? → A: Todas las operaciones por `clave` son case-insensitive y el sistema normaliza internamente a mayusculas.
- Q: Que estrategia de borrado aplica para departamentos sin empleados asociados? → A: Eliminacion logica (soft delete) marcando el departamento como inactivo.
- Q: Como se exponen departamentos inactivos tras soft delete en consultas/listados? → A: Se excluyen de listados y en consulta por clave se tratan como `404 Not Found`.
- Q: Que perfil de carga define "operacion normal" para validar rendimiento de consultas/listados? → A: Medir con 20 usuarios concurrentes y 1,000 departamentos.
- Q: Cual es el protocolo de aceptacion para medir "sin asistencia" en SC-004? → A: 10 usuarios internos; exito si 95% completa 6 escenarios CRUD sin ayuda.
- Q: Cual es el payload minimo obligatorio para errores `400/404/409`? → A: Incluir `code`, `message` y `timestamp` en todos esos errores.
- Q: Que codigo HTTP debe devolver `DELETE` cuando el soft delete de departamento es exitoso? → A: `204 No Content`.
- Q: Como se expresa el cumplimiento de dos rutas de login separadas (Empleados y Departamentos) en este feature? → A: El sistema MUST exponer y validar dos rutas de login separadas, con flujo secuencial Empleados -> Departamentos.

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

1. **Given** que existe un departamento registrado, **When** el usuario autenticado lo consulta por clave, **Then** el sistema devuelve su clave, nombre y `employeeCount` como referencia de empleados asociados.
2. **Given** que existen multiples departamentos registrados, **When** el usuario autenticado solicita el listado indicando `page` y `size` validos, **Then** el sistema devuelve una pagina de resultados ordenada por defecto por `clave ASC`, con la informacion principal y `employeeCount` de cada departamento.
3. **Given** que el usuario autenticado recorre sucesivamente las paginas disponibles del listado, **When** completa la navegacion hasta la ultima pagina, **Then** puede recuperar el total de departamentos disponibles sin omisiones ni duplicados.
4. **Given** que no existe un departamento con la clave consultada, **When** el usuario autenticado realiza la consulta, **Then** el sistema responde que el departamento no fue encontrado.
5. **Given** que un departamento fue marcado como inactivo por soft delete, **When** el usuario autenticado intenta consultarlo por clave, **Then** el sistema responde `404 Not Found`.

Nota de salida funcional: la referencia de empleados asociados se representa como `employeeCount` en la respuesta de departamentos.

---

### User Story 3 - Actualizar y eliminar departamentos (Priority: P3)

Como usuario autenticado, quiero actualizar el nombre de un departamento y eliminar departamentos sin empleados asociados, para mantener la estructura organizacional actualizada.

**Why this priority**: Completa el ciclo CRUD y protege la consistencia de los empleados ya vinculados.

**Independent Test**: Se prueba modificando datos de un departamento existente y eliminando un departamento sin dependencias para confirmar comportamiento esperado.

**Acceptance Scenarios**:

1. **Given** que existe un departamento, **When** el usuario autenticado actualiza su nombre con un valor valido, **Then** el sistema guarda los cambios y devuelve la version actualizada.
2. **Given** que un departamento tiene empleados asociados, **When** el usuario autenticado intenta eliminarlo, **Then** el sistema rechaza la eliminacion e informa que primero deben desvincularse o reasignarse los empleados.
3. **Given** que un departamento no tiene empleados asociados, **When** el usuario autenticado lo elimina, **Then** el sistema aplica eliminacion logica, marca el departamento como inactivo y responde `204 No Content`.
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
5. **Given** un usuario en frontend, **When** completa login de Empleados y luego login de Departamentos, **Then** el sistema valida ambas rutas separadas y permite acceso al flujo correspondiente.

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
- Consulta o uso de un departamento inactivo tras soft delete debe tratarse como recurso no disponible.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST permitir crear departamentos con los campos obligatorios `clave` y `nombre`.
- **FR-001a**: La `clave` de departamento MUST cumplir el patron `^[A-Z0-9_]{2,20}$`.
- **FR-002**: El sistema MUST garantizar que la `clave` de departamento sea unica dentro del catalogo y comparada de forma case-insensitive.
- **FR-003**: El sistema MUST permitir consultar un departamento por su clave.
- **FR-003a**: Las operaciones por `clave` de departamento (`GET`, `PUT`, `DELETE`) MUST ser case-insensitive y la `clave` MUST normalizarse a mayusculas de forma interna.
- **FR-004**: El sistema MUST permitir listar departamentos exclusivamente mediante paginacion usando parametros `page` y `size`, con valores predeterminados `page=0` y `size=10`, devolviendo `employeeCount` por departamento y orden por defecto `clave ASC`; para recuperar la totalidad del catalogo se deben recorrer todas las paginas.
- **FR-004b**: En `GET /departamentos/{clave}`, el sistema MUST devolver `employeeCount` como referencia de empleados asociados y MUST NOT incluir una lista embebida de empleados.
- **FR-004c**: El listado `GET /departamentos` MUST excluir departamentos inactivos por soft delete.
- **FR-004a**: El sistema MUST validar `size` en rango permitido y rechazar valores mayores a 100.
- **FR-005**: El sistema MUST permitir actualizar solo el `nombre` de un departamento existente.
- **FR-005a**: El sistema MUST tratar la `clave` de departamento como inmutable despues de su creacion.
- **FR-006**: El sistema MUST aplicar eliminacion logica (soft delete) para departamentos sin empleados asociados, excepto el departamento tecnico `SIN_DEPTO`.
- **FR-006a**: Cuando `DELETE /departamentos/{clave}` aplica soft delete de forma exitosa, el sistema MUST responder `204 No Content`.
- **FR-007**: El sistema MUST impedir eliminar departamentos que tengan empleados asociados, mostrando un mensaje claro de validacion.
- **FR-008**: El sistema MUST mantener la relacion 1:N entre `Departamento` y `Empleado`: un departamento puede tener cero o muchos empleados, y cada empleado debe pertenecer a exactamente un departamento.
- **FR-009**: El sistema MUST validar que al registrar o actualizar un empleado, el departamento referenciado exista.
- **FR-010**: El sistema MUST manejar errores de negocio de forma consistente segun reglas especificas en `FR-010a`, `FR-010b`, `FR-010c` y `FR-010d`.
- **FR-010a**: En creacion de departamento con `clave` duplicada, el sistema MUST responder `409 Conflict`.
- **FR-010b**: El sistema MUST mapear errores funcionales de departamentos como sigue: `404 Not Found` para recurso inexistente, `400 Bad Request` para validacion/formato invalido y `409 Conflict` para eliminacion bloqueada por dependencias o regla de negocio.
- **FR-010c**: Un departamento inactivo por soft delete MUST tratarse como recurso no disponible y responder `404 Not Found` en consultas por `clave`.
- **FR-010d**: Los errores `400`, `404` y `409` MUST incluir un payload minimo estandar con `code`, `message` y `timestamp`.
- **FR-011**: El sistema MUST exigir `departamento` obligatorio al crear empleados; no se permiten empleados sin departamento.
- **FR-012**: En migracion de datos, el sistema MUST crear un departamento tecnico `SIN_DEPTO` y asignarlo a empleados historicos sin departamento antes de imponer restriccion obligatoria.
- **FR-013**: En operaciones de empleados, la referencia de departamento MUST realizarse mediante `clave` de departamento.
- **FR-013a**: La validacion de `departamentoClave` en operaciones de empleados MUST ser case-insensitive y aplicar la misma normalizacion interna a mayusculas.
- **FR-014**: Ante intento de eliminar `SIN_DEPTO`, el sistema MUST responder error de negocio explicito con mensaje claro y codigo HTTP consistente con el contrato OpenAPI.
- **FR-015**: El sistema MUST registrar en log cada operacion de creacion, actualizacion y eliminacion de departamento, incluyendo identidad del usuario autenticado, tipo de operacion y marca de tiempo.
- **FR-016**: El sistema MUST soportar dos rutas de login separadas para frontend: una para Empleados y otra para Departamentos, con flujo secuencial Empleados -> Departamentos.

**Constitution-aligned mandatory constraints (backend projects):**

- **AUTH-001**: Endpoints de departamentos MUST requerir Bearer token valido, con identico modelo de autorizacion que los endpoints de empleados; no se requiere rol especial adicional.
- **DATA-001**: Los nuevos datos persistidos MUST quedar definidos en PostgreSQL e incluir su impacto de migracion.
- **ENV-001**: La ejecucion local e integracion MUST seguir siendo reproducible mediante Docker/Compose.
- **DOC-001**: Endpoints nuevos o modificados MUST incluir requisitos de documentacion OpenAPI/Swagger.
- **QUAL-001**: La feature MUST definir pruebas unitarias e integracion para operaciones CRUD y reglas de relacion con empleados.

### Key Entities *(include if feature involves data)*

- **Departamento**: Unidad organizacional con `clave` (inmutable y unica case-insensitive), `nombre` y estado de actividad para eliminacion logica; puede tener cero o muchos empleados asociados (1:N).
- **Empleado**: Persona gestionada por el sistema que pertenece a exactamente un departamento, referenciado por `clave`; la relacion es obligatoria (N:1).

### Assumptions

- La gestion de departamentos sera realizada por cualquier usuario autenticado con Bearer valido, sin distincion de rol especial; mismo modelo de autorizacion que empleados.
- La clave de departamento sera tratada como identificador funcional para operaciones de consulta y mantenimiento.
- Un empleado no puede pertenecer simultaneamente a mas de un departamento; la relacion es exactamente 1 departamento por empleado (N:1).
- Todo empleado nuevo debe quedar asociado a un departamento valido desde su creacion.
- La politica de concurrencia en actualizaciones de departamento es last-write-wins; no se implementa optimistic locking.
- La eliminacion de departamentos se implementa como soft delete y no como borrado fisico.

### Dependencies

- Disponibilidad de la autenticacion existente para proteger operaciones administrativas.
- Actualizacion del modelo actual de empleados para incluir la referencia al departamento.
- Migraciones de base de datos para crear estructura de departamentos y relacion con empleados.
- Migracion controlada de datos existentes para asignar `SIN_DEPTO` donde falte departamento.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de operaciones de creacion de departamentos con clave y nombre validos se completan exitosamente en pruebas funcionales.
- **SC-002**: Al menos el 95% de consultas y listados de departamentos devuelven resultado en menos de 2 segundos, medido con 20 usuarios concurrentes y un dataset de 1,000 departamentos, incluyendo respuestas paginadas.
- **SC-003**: El 100% de intentos de eliminar departamentos con empleados asociados son bloqueados con `409 Conflict` y payload estandar (`code`, `message`, `timestamp`), donde `code` pertenece al catalogo de errores documentado.
- **SC-004**: En una prueba de aceptacion con 10 usuarios internos, al menos el 95% MUST completar 6 escenarios CRUD de departamentos sin asistencia adicional.
- **SC-005**: El 100% de operaciones de escritura sobre departamentos (crear, actualizar, eliminar) generan entrada de log con usuario, operacion y marca de tiempo verificable en pruebas de integracion.
