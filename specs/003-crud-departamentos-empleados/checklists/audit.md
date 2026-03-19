# Audit Evidence - CRUD Departamentos

**Fecha**: 2026-03-19
**Objetivo**: Verificar SC-005 (100% de escrituras generan log con operacion verificable).

## Evidencia en reportes de pruebas

- Creacion:
  - `target/surefire-reports/TEST-com.example.empleados.integration.CrearDepartamentoIntegrationTest.xml:77`
  - `AUDIT departamento_creado clave=RH`

- Actualizacion:
  - `target/surefire-reports/TEST-com.example.empleados.integration.ActualizarDepartamentoIntegrationTest.xml:78`
  - `AUDIT departamento_actualizado clave=FIN`

- Eliminacion:
  - `target/surefire-reports/TEST-com.example.empleados.integration.EliminarDepartamentoIntegrationTest.xml:78`
  - `AUDIT departamento_eliminado clave=LEGAL`

## Resultado

- Estado: **PASS**
- Conclusion: Se verifican entradas de auditoria para las tres operaciones de escritura (crear, actualizar, eliminar).
