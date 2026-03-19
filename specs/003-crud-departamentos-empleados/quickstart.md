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

## Validacion manual (2026-03-19)

- Se ejecuto validacion de comandos de calidad y regresion del feature:
  - `mvn "-Dtest=com.example.empleados.**.*Test" test` -> `43` pruebas en verde.
  - `mvn "-Dtest=DepartamentosPerformanceIntegrationTest" test` -> `p95_ms=104` (< 2000 ms).
- En entorno local se detecto una inconsistencia operativa al levantar `spring-boot:run` en perfil `dev` sin sobreescribir datasource (`password authentication failed` para `empleados_user`), causada por configuracion/estado local de base de datos.
- Para smoke reproducible se recomienda ejecutar primero el ciclo Compose en entorno limpio o alinear credenciales/puerto del datasource local con `docker/docker-compose.yml`.
