# Quickstart - Autenticacion de Empleados (JWT)

## Prerrequisitos
- Java 17
- Maven 3.9+
- Docker y Docker Compose

## 1) Levantar PostgreSQL local
```bash
docker compose -f docker/docker-compose.yml up -d
```

## 2) Configurar variables de entorno minimas
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET` (secreto de firma JWT fuera de repositorio)
- `JWT_EXPIRATION_MINUTES=60`

## 3) Ejecutar servicio
```bash
mvn spring-boot:run
```

## 4) Login y uso de token
### 4.1 Obtener JWT
```bash
curl -X POST http://localhost:8081/api/v2/auth/login \
  -H "Content-Type: application/json" \
  -d '{"correo":"empleado@empresa.com","password":"secreto"}'
```

### 4.2 Consumir endpoint protegido
```bash
curl http://localhost:8081/api/v2/empleados \
  -H "Authorization: Bearer <access_token>"
```

## 5) Casos de validacion rapida
- Login exitoso con cuenta activa devuelve `200` y token Bearer con expiracion a 60 minutos.
- Login con correo no registrado, password incorrecta o cuenta inactiva devuelve `401` sin revelar causa sensible.
- Request a endpoint protegido sin token, con token invalido o expirado devuelve `401`.
- Tras expirar token, se requiere login nuevamente (sin refresh token).

## 6) Ejecutar pruebas
```bash
mvn test
```

## Resultado esperado
- Endpoint de login operativo con emision JWT.
- Endpoints protegidos accesibles solo con Bearer valido y vigente.
- Contrato OpenAPI actualizado para autenticacion y seguridad.
- Pruebas unitarias/integracion/contrato cubren escenarios clave de autenticacion.
