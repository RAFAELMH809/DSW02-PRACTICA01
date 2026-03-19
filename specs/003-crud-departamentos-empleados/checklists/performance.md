# Performance Evidence - SC-002

**Fecha**: 2026-03-19
**Prueba**: `DepartamentosPerformanceIntegrationTest`

## Ejecucion

Comando:

```bash
mvn "-Dtest=DepartamentosPerformanceIntegrationTest" test
```

Evidencia:

- `target/surefire-reports/TEST-com.example.empleados.integration.DepartamentosPerformanceIntegrationTest.xml:121`
- `PERF_EVIDENCE p95_ms=104 samples_ms=[81, 83, 84, 86, 86, 88, 89, 90, 90, 91, 92, 93, 94, 94, 95, 95, 99, 101, 104, 141]`

## Resultado

- p95 observado: **104 ms**
- Umbral objetivo: **< 2000 ms**
- Estado: **PASS**
