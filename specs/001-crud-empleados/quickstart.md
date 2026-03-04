# Quickstart - CRUD de Empleados

## Prerrequisitos
- Java 17
- Maven 3.9+
- Docker y Docker Compose

## 1) Levantar PostgreSQL local
```bash
docker compose up -d postgres
```

## 2) Configurar variables de entorno mínimas
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `APP_SECURITY_USERNAME`
- `APP_SECURITY_PASSWORD`

## 3) Ejecutar migraciones y arrancar servicio
```bash
./mvnw spring-boot:run
```

## 4) Validar contrato API
- Abrir Swagger UI en la ruta configurada por el proyecto.
- Verificar endpoints:
  - `POST /empleados` (request solo con `nombre`, `direccion`, `telefono`; `clave` se genera automáticamente como `EMP-<autonumérico>`)
  - `GET /empleados`
  - `GET /empleados/{clave}`
  - `PUT /empleados/{clave}`
  - `DELETE /empleados/{clave}`

## 5) Ejecutar pruebas
```bash
mvn test
```

## Resultado de validación actual
- Maven quedó disponible en modo local del proyecto (`.tools/apache-maven-3.9.12/bin/mvn.cmd`).
- Se ejecutó la suite completa con ese binario: `Tests run: 14, Failures: 0, Errors: 0`.
- Las pruebas de integración usan PostgreSQL de Docker Compose en `localhost:55432` mediante perfil `test` (`src/test/resources/application-test.yml`).
- Resultado final: contratos, unitarias e integración en estado OK.

## Resultado esperado
- CRUD funcional con autenticación HTTP Basic.
- Persistencia en PostgreSQL en contenedor.
- Documentación OpenAPI/Swagger disponible y actualizada.
