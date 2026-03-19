# Data Model - Autenticacion de Empleados

## Entity: CredencialEmpleado

### Fields
- `empleado_id` (string, PK/FK, required)
- `correo` (string, required, unique, normalized lowercase)
- `password_hash` (string, required)
- `activo` (boolean, required, default: true)
- `ultimo_login_exitoso_at` (timestamp, optional)
- `created_at` (timestamp, required, system managed)
- `updated_at` (timestamp, required, system managed)

### Validation Rules
- `correo` debe cumplir formato email valido y tratarse case-insensitive para autenticacion.
- `correo` vacio, nulo o con solo espacios es invalido.
- `password` de entrada vacia, nula o con solo espacios es invalida.
- `password_hash` siempre se persiste con algoritmo seguro (BCrypt/Argon2), nunca texto plano.
- Solo registros con `activo = true` pueden autenticarse en login.

## Entity: SesionAutenticada

### Fields
- `access_token` (string JWT, required)
- `token_type` (string, required, fixed: `Bearer`)
- `expires_at` (timestamp, required)
- `issued_at` (timestamp, required)
- `subject` (string, required, referencia al empleado autenticado)

### Validation Rules
- `expires_at` debe ser exactamente `issued_at + 60 minutos`.
- No existe `refresh_token` en este alcance.
- JWT expirado siempre es invalido para acceso protegido.

## Entity: EventoAutenticacion

### Fields
- `id` (uuid o bigint, PK, required)
- `correo_intentado` (string, required)
- `resultado` (enum, required: `SUCCESS`, `FAIL_INVALID_CREDENTIALS`, `FAIL_INACTIVE`)
- `motivo` (string, optional, solo para auditoria interna)
- `ip_origen` (string, optional)
- `user_agent` (string, optional)
- `ocurrido_at` (timestamp, required)

### Validation Rules
- Todo intento de login debe generar un `EventoAutenticacion`.
- El detalle sensible se conserva solo en auditoria interna, no en respuesta publica.

## Relationships
- `CredencialEmpleado` 1:1 `Empleado` (dominio existente).
- `CredencialEmpleado` 1:N `EventoAutenticacion` por `correo` o `empleado_id` resuelto.
- `SesionAutenticada` es entidad transitoria de respuesta (no requiere persistencia obligatoria en DB).

## State Transitions
- `CredencialEmpleado`: **Activa** -> **Inactiva** (inhabilita nuevos logins).
- `CredencialEmpleado`: **Inactiva** -> **Activa** (rehabilita logins futuros).
- `SesionAutenticada`: **Emitida** -> **Expirada** (a los 60 minutos exactos).
- `EventoAutenticacion`: estado inmutable una vez registrado.
