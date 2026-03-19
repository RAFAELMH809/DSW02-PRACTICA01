# Quickstart - CRUD de Departamentos Vinculado a Empleados

## Prerrequisitos
- Java 17
- Maven 3.9+
- Docker y Docker Compose

## 1) Levantar base de datos local
```bash
docker compose -f docker/docker-compose.yml up -d
```

## 2) Ejecutar la aplicacion
```bash
mvn spring-boot:run
```

## 3) Obtener token Bearer (si aplica login del proyecto)
```bash
curl -X POST http://localhost:8081/api/v2/auth/login \
  -H "Content-Type: application/json" \
  -d '{"correo":"empleado@empresa.com","password":"secreto"}'
```

## 4) Probar CRUD de departamentos

### 4.1 Crear departamento
```bash
curl -X POST http://localhost:8081/api/v2/departamentos \
  -H "Authorization: Bearer <access_token>" \
  -H "Content-Type: application/json" \
  -d '{"clave":"RH","nombre":"Recursos Humanos"}'
```

### 4.2 Listar departamentos (paginado)
```bash
curl "http://localhost:8081/api/v2/departamentos?page=0&size=10" \
  -H "Authorization: Bearer <access_token>"
```

### 4.3 Consultar por clave
```bash
curl http://localhost:8081/api/v2/departamentos/RH \
  -H "Authorization: Bearer <access_token>"
```

### 4.4 Actualizar nombre
```bash
curl -X PUT http://localhost:8081/api/v2/departamentos/RH \
  -H "Authorization: Bearer <access_token>" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Gestion Humana"}'
```

### 4.5 Eliminar departamento
```bash
curl -X DELETE http://localhost:8081/api/v2/departamentos/RH \
  -H "Authorization: Bearer <access_token>"
```

## 5) Casos de validacion recomendados
- Crear clave duplicada con distinto case (`RH` y `rh`) debe fallar.
- Eliminar departamento con empleados asociados debe fallar.
- Eliminar `SIN_DEPTO` debe fallar siempre.
- Crear empleado sin `departamentoClave` debe fallar.
- Acceder sin token / token invalido / token expirado debe responder 401.

## 6) Ejecutar pruebas
```bash
mvn test
```

## Resultado esperado
- CRUD de departamentos funcional con seguridad Bearer.
- Integracion obligatoria con empleados por `departamentoClave`.
- Migracion historica con `SIN_DEPTO` aplicada.
- Auditoria de escrituras y criterios de calidad verificables.
