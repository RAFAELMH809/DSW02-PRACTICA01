# Phase 0 Research - Autenticacion de Empleados

## Decision 1: Mecanismo de autenticacion para API
- **Decision**: Implementar autenticacion basada en JWT Bearer emitido por endpoint de login (`correo` + `password`) y validado en cada request protegida.
- **Rationale**: Es el comportamiento explicitamente requerido por la especificacion (FR-005, FR-006, FR-011, FR-012, FR-013) y permite desacoplar autenticacion inicial de acceso posterior a endpoints protegidos.
- **Alternatives considered**:
  - HTTP Basic en cada request: rechazado por no cumplir flujo de login con emision de token.
  - Session cookie stateful: rechazado por no ser el modelo esperado para API REST en este alcance.

## Decision 2: Vida de token y renovacion
- **Decision**: Configurar expiracion exacta a 60 minutos desde emision y omitir refresh token.
- **Rationale**: Cumple aclaraciones funcionales y reduce superficie de implementacion en MVP.
- **Alternatives considered**:
  - Refresh token: descartado por FR-012.
  - Expiracion variable por perfil: descartado para evitar ambiguedad de contrato en esta iteracion.

## Decision 3: Validacion de estado de cuenta
- **Decision**: Verificar `activo/inactivo` solo durante el login; tokens vigentes emitidos antes de inactivar cuenta permanecen validos hasta expirar.
- **Rationale**: Alineado con aclaraciones de negocio y FR-013.
- **Alternatives considered**:
  - Revalidar estado en cada request: descartado por ir contra reglas acordadas del feature.
  - Revocacion inmediata de tokens al inactivar: descartado por fuera de alcance (sin revocation list).

## Decision 4: Modelo de datos para credenciales y auditoria
- **Decision**: Mantener correo unico y hash de password en entidad de empleado (o tabla asociada 1:1) y registrar intentos en tabla de eventos de autenticacion.
- **Rationale**: Satisface autenticacion y trazabilidad operativa (FR-007) sin incorporar componentes externos.
- **Alternatives considered**:
  - Persistir passwords en texto plano: descartado por riesgo critico de seguridad.
  - No persistir eventos: descartado por incumplir requisito de auditoria.

## Decision 5: Reglas de error y no enumeracion de cuentas
- **Decision**: Responder errores de autenticacion con mensaje generico uniforme para correo inexistente, password incorrecta e inactivo, registrando detalle interno solo en logs/eventos.
- **Rationale**: Cumple FR-008 y evita exponer informacion sensible.
- **Alternatives considered**:
  - Mensajes especificos por causa: descartado por riesgo de enumeracion de cuentas.

## Decision 6: Integracion con stack actual Spring Boot
- **Decision**: Implementar filtro JWT en Spring Security, endpoint `POST /api/v2/auth/login`, y aplicar `bearerAuth` en endpoints de empleados.
- **Rationale**: Encaja con arquitectura existente (controlador/servicio/repositorio/config) y simplifica pruebas con MockMvc + integration tests.
- **Alternatives considered**:
  - Gateway externo de autenticacion: descartado por alcance y complejidad.
  - OAuth2 completo: descartado por no requerido en el feature.

## Decision 7: Resolucion de clarificaciones pendientes
- **Decision**: No quedan items `NEEDS CLARIFICATION` tras revisar especificacion y sesion de clarificaciones del 2026-03-11.
- **Rationale**: Todas las dudas relevantes (bloqueo, expiracion, refresh, estado activo) estan explicitamente resueltas en `spec.md`.
- **Alternatives considered**:
  - Posponer decisiones a `tasks.md`: descartado para evitar ambiguedad en fase de implementacion.
