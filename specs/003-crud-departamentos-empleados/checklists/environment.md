# Environment Reproducibility Smoke - T066

**Fecha**: 2026-03-19

## Ejecucion

Comando ejecutado:

```bash
docker compose -f docker/docker-compose.yml up -d
docker compose -f docker/docker-compose.yml ps
docker compose -f docker/docker-compose.yml down
```

## Hallazgos

- Docker y Docker Compose disponibles:
  - `Docker version 29.2.0`
  - `Docker Compose version v5.0.2`
- Se detecto conflicto por contenedor existente con nombre fijo:
  - `empleados-postgres` ya existia en ejecucion (`Up ... healthy`).
- `up -d` reporto conflicto de nombre, y `down` removio la red temporal creada.

## Resultado

- Estado: **PASS with pre-existing environment note**
- Conclusion: El stack es reproducible, pero el smoke debe correr sobre un entorno limpio o reusar contenedores existentes para evitar conflicto de nombre.
