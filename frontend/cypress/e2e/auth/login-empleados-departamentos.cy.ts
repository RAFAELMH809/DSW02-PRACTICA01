describe('Login secuencial empleados y departamentos', () => {
  it('habilita el panel de departamentos despues de ambos logins', () => {
    cy.intercept('GET', '**/api/v2/empleados?page=0&size=1', {
      statusCode: 200,
      body: { content: [], number: 0, size: 1, totalElements: 0 }
    }).as('loginEmpleados');

    cy.intercept('GET', '**/api/v2/departamentos?page=0&size=1', {
      statusCode: 200,
      body: { content: [], number: 0, size: 1, totalElements: 0 }
    }).as('loginDepartamentos');

    cy.intercept('GET', '**/api/v2/departamentos?page=0&size=10', {
      statusCode: 200,
      body: {
        content: [
          { clave: 'TI', nombre: 'Tecnologia', employeeCount: 3 }
        ],
        number: 0,
        size: 10,
        totalElements: 1
      }
    }).as('listarDepartamentos');

    cy.intercept('POST', '**/api/v2/departamentos', {
      statusCode: 201,
      body: { clave: 'RH', nombre: 'Recursos Humanos', employeeCount: 0 }
    }).as('crearDepartamento');

    cy.visit('/');

    cy.get('[data-testid="empleados-user"]').clear({ force: true }).type('admin', { force: true });
    cy.get('[data-testid="empleados-pass"]').clear({ force: true }).type('admin123', { force: true });
    cy.get('[data-testid="empleados-login-btn"]').click({ force: true });

    cy.wait('@loginEmpleados');
    cy.get('[data-testid="empleados-status"]').should('contain.text', 'Autenticado');

    cy.get('[data-testid="departamentos-user"]').clear({ force: true }).type('admin', { force: true });
    cy.get('[data-testid="departamentos-pass"]').clear({ force: true }).type('admin123', { force: true });
    cy.get('[data-testid="departamentos-login-btn"]').click({ force: true });

    cy.wait('@loginDepartamentos');
    cy.wait('@listarDepartamentos');

    cy.get('[data-testid="ok-msg"]').should('contain.text', 'Login secuencial completado');
    cy.get('[data-testid="dept-list"]').should('contain.text', 'TI');

    cy.get('[data-testid="new-clave"]').type('RH', { force: true });
    cy.get('[data-testid="new-nombre"]').type('Recursos Humanos', { force: true });
    cy.get('[data-testid="crear-departamento-btn"]').click({ force: true });

    cy.wait('@crearDepartamento');
    cy.wait('@listarDepartamentos');
    cy.get('[data-testid="ok-msg"]').should('contain.text', 'Departamento RH creado');
  });
});
