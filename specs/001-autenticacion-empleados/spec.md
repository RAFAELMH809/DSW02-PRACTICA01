# Feature Specification: Autenticacion de Empleados

**Feature Branch**: `001-autenticacion-empleados`  
**Created**: 2026-03-11  
**Status**: Draft  
**Input**: User description: "Autenticacion con correo y pwd de empleados"

## Clarifications

### Session 2026-03-11

- Q: Que politica se aplicara para intentos fallidos consecutivos de login? -> A: Sin bloqueo por intentos fallidos.
- Q: Que mecanismo de autenticacion se aplicara para sesion de API? -> A: Login con correo/password que devuelve JWT Bearer con expiracion.
- Q: Cual sera el tiempo de expiracion del JWT Bearer? -> A: Expira en 60 minutos.
- Q: Se implementara refresh token para renovar sesiones? -> A: Sin refresh token; al expirar requiere login nuevamente.
- Q: Como se valida estado activo tras emitir un JWT? -> A: Se valida en login; JWT emitido sigue valido hasta expiracion.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Iniciar sesion con correo y contrasena (Priority: P1)

Como empleado autorizado, quiero iniciar sesion con mi correo y contrasena para obtener un token JWT Bearer y acceder a funcionalidades protegidas del sistema.

**Why this priority**: Sin autenticacion no hay control de acceso y cualquier usuario podria interactuar con informacion sensible.

**Independent Test**: Puede probarse enviando credenciales validas y verificando que el acceso se concede a recursos protegidos.

**Acceptance Scenarios**:

1. **Given** un empleado activo con correo y contrasena validos, **When** intenta autenticarse con esas credenciales, **Then** el sistema valida su identidad y devuelve un JWT Bearer con expiracion para acceder a operaciones protegidas.
2. **Given** un empleado activo, **When** intenta autenticarse con contrasena incorrecta, **Then** el sistema rechaza el acceso con un mensaje de credenciales invalidas sin revelar cual dato fallo.
3. **Given** un correo no registrado, **When** se intenta autenticacion, **Then** el sistema rechaza el acceso con respuesta de no autorizado.

---

### User Story 2 - Bloquear acceso sin credenciales validas (Priority: P2)

Como responsable del sistema, quiero que las operaciones protegidas solo sean accesibles con JWT Bearer validos para reducir el riesgo de acceso no autorizado.

**Why this priority**: Complementa el flujo de inicio de sesion y garantiza que la seguridad se aplique de forma consistente en toda la funcionalidad protegida.

**Independent Test**: Puede probarse invocando operaciones protegidas sin credenciales, con credenciales incompletas y con credenciales invalidas, y verificando el rechazo en todos los casos.

**Acceptance Scenarios**:

1. **Given** una solicitud a un recurso protegido sin token Bearer, **When** el sistema la procesa, **Then** responde como no autorizado y no expone datos del recurso.
2. **Given** una solicitud con token Bearer invalido o expirado, **When** el sistema valida la autenticacion, **Then** rechaza la solicitud y mantiene el recurso inaccesible.
3. **Given** un token Bearer expirado, **When** el empleado necesita continuar, **Then** debe realizar login nuevamente con correo y contrasena para obtener un nuevo token.

---

### User Story 3 - Gestionar estados de cuenta en autenticacion (Priority: P3)

Como administrador de personal, quiero que solo cuentas activas puedan autenticarse en el momento del login para reflejar altas y bajas de empleados en el control de acceso.

**Why this priority**: Evita que cuentas inactivas sigan teniendo acceso y mejora la gobernanza operativa.

**Independent Test**: Puede probarse marcando una cuenta como inactiva y verificando que deja de autenticarse, mientras una cuenta activa equivalente si puede autenticarse.

**Acceptance Scenarios**:

1. **Given** una cuenta de empleado inactiva, **When** se intenta autenticacion con credenciales correctas, **Then** el sistema rechaza el acceso por estado de cuenta en el login.
2. **Given** una cuenta activa y credenciales correctas, **When** se intenta autenticacion, **Then** el sistema concede el acceso.
3. **Given** un JWT valido emitido cuando la cuenta estaba activa, **When** la cuenta cambia a inactiva antes de expirar el token, **Then** el token sigue siendo aceptado hasta su expiracion.

### Edge Cases

- Correos con diferencias solo de mayusculas/minusculas se tratan como el mismo identificador de usuario.
- Intentos con correo o contrasena vacios, nulos o compuestos solo por espacios se rechazan.
- Intentos con caracteres no validos en el correo se rechazan con mensaje de validacion.
- Multiples intentos fallidos consecutivos para la misma cuenta no deben conceder acceso en ningun caso.
- Multiples intentos fallidos consecutivos no activan bloqueo automatico de cuenta en este alcance.
- Tokens JWT expirados no habilitan acceso y requieren nuevo login con correo y contrasena.
- Cuentas eliminadas o no encontradas siempre devuelven respuesta de no autorizado sin revelar existencia de la cuenta.
- Si una cuenta pasa a inactiva despues de emitir JWT, los tokens ya emitidos no se revocan en este alcance y vencen por expiracion.

## Assumptions

- El correo del empleado es unico y ya existe en el catalogo de empleados.
- La autenticacion se limita al inicio de sesion con correo y contrasena; no incluye recuperacion de contrasena en este alcance.
- El sistema ya cuenta con al menos un conjunto de endpoints protegidos sobre los cuales se aplicara esta autenticacion.
- Los mensajes de error deben ser claros para el usuario, pero no deben exponer informacion sensible sobre validacion interna.
- El token JWT Bearer emitido en login tendra vigencia de 60 minutos.
- Este alcance no incluye refresh token ni endpoint de renovacion de token.
- El estado activo/inactivo se valida al momento del login, no en cada request autenticado con JWT vigente.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST permitir autenticar empleados mediante correo y contrasena.
- **FR-002**: El sistema MUST validar formato de correo y presencia de contrasena antes de procesar autenticacion.
- **FR-003**: El sistema MUST rechazar autenticacion cuando el correo no exista, la contrasena sea incorrecta o la cuenta este inactiva.
- **FR-004**: El sistema MUST conceder acceso a recursos protegidos solo cuando la autenticacion sea exitosa.
- **FR-005**: El sistema MUST emitir un token JWT Bearer con expiracion cuando el login con correo y contrasena sea exitoso.
- **FR-006**: El sistema MUST rechazar cualquier solicitud a recursos protegidos sin token Bearer, con token invalido o con token expirado.
- **FR-007**: El sistema MUST registrar eventos de autenticacion exitosa y fallida con informacion suficiente para auditoria operativa.
- **FR-008**: El sistema MUST asegurar que las respuestas de error de autenticacion no revelen si el correo existe ni detalles de validacion sensible.
- **FR-009**: El sistema MUST aplicar de forma consistente las mismas reglas de autenticacion JWT en todas las operaciones protegidas de empleados.
- **FR-010**: El sistema MUST no bloquear automaticamente cuentas por intentos fallidos consecutivos; cada intento debe evaluarse y rechazarse o aceptarse segun credenciales y estado de cuenta.
- **FR-011**: El token JWT Bearer emitido MUST expirar exactamente a los 60 minutos desde su emision.
- **FR-012**: El sistema MUST no emitir refresh tokens ni exponer endpoint de renovacion; tras expiracion del JWT se requiere nuevo login.
- **FR-013**: El sistema MUST validar estado activo/inactivo solo durante el login; un JWT ya emitido se mantiene valido hasta su expiracion configurada.

**Constitution-aligned mandatory constraints (backend projects):**

- **AUTH-001**: Los endpoints protegidos MUST definir autenticacion mediante JWT Bearer, incluyendo emision y validacion de expiracion del token.
- **DATA-001**: Los datos persistidos para autenticacion MUST incluir impacto de cambios en esquema y migraciones versionadas.
- **ENV-001**: La ejecucion local e integracion MUST ser reproducible mediante la configuracion de entorno del proyecto.
- **DOC-001**: Endpoints nuevos o modificados MUST reflejar requisitos de autenticacion en la documentacion de contrato del servicio.
- **QUAL-001**: La funcionalidad MUST definir expectativas de pruebas unitarias e integrales para autenticacion exitosa, fallida y acceso protegido.

### Key Entities *(include if feature involves data)*

- **CredencialEmpleado**: Representa la informacion de autenticacion asociada a un empleado, incluyendo correo, secreto de contrasena y estado de cuenta.
- **SesionAutenticada**: Representa el resultado de una autenticacion exitosa con JWT Bearer emitido y su tiempo de expiracion para acceso a recursos protegidos.
- **EventoAutenticacion**: Representa un registro auditable de cada intento de autenticacion con resultado, momento y contexto minimo de trazabilidad.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Al menos 95% de empleados activos con credenciales validas completan autenticacion exitosa en su primer intento.
- **SC-002**: El 100% de intentos de acceso a recursos protegidos sin autenticacion valida son rechazados.
- **SC-003**: El 100% de intentos con cuenta inactiva, inexistente o contrasena incorrecta son rechazados sin exponer datos sensibles en el mensaje de respuesta.
- **SC-004**: El 100% de tokens JWT emitidos reflejan expiracion de 60 minutos desde el momento de login.
- **SC-005**: El 100% de intentos con JWT expirado son rechazados y requieren nuevo login.
- **SC-006**: El tiempo de validacion de autenticacion en condiciones normales es menor a 2 segundos en al menos 95% de los intentos.
- **SC-007**: El 100% de la documentacion y contratos de autenticacion reflejan que no existe flujo de refresh token.
- **SC-008**: El 100% de solicitudes con JWT vigente emitido antes de inactivar la cuenta se aceptan hasta la expiracion del token.
