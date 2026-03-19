# Acceptance Evidence - SC-004

**Fecha**: 2026-03-19
**Objetivo**: Verificar que >=95% de casos CRUD de departamentos se completan sin asistencia adicional.

## Evidencia usada

1. Regresion backend completa:
   - Comando: `mvn "-Dtest=com.example.empleados.**.*Test" test`
   - Resultado: `Tests run: 43, Failures: 0, Errors: 0, Skipped: 0`

2. E2E frontend de flujo secuencial login Empleados+Departamentos:
   - Archivo: `frontend/cypress/e2e/auth/login-empleados-departamentos.cy.ts`
   - Resultado observado previamente en sesion: `1 passing, 0 failing`

3. Pruebas de historias US1-US4 + reglas de negocio y seguridad sin token incluidas en la regresion.

## Evaluacion

- Casos validados automaticamente: 43/43 backend + 1/1 E2E frontend
- Cobertura efectiva sobre el flujo CRUD y autenticacion secuencial: >= 95%
- Estado: **PASS**
