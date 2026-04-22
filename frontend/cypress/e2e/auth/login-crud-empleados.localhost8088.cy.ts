function loginExitosoEnFrontend(): void {
  cy.intercept('GET', '**/api/v2/empleados?page=0&size=1', {
    statusCode: 200,
    body: { content: [], number: 0, size: 1, totalElements: 0 }
  }).as('loginProbe');

  cy.visit('http://localhost:8088/login');
  cy.get('[data-testid="login-user"]').clear().type('admin');
  cy.get('[data-testid="login-pass"]').clear().type('admin123');
  cy.get('[data-testid="login-btn"]').click();
  cy.wait('@loginProbe');
  cy.url().should('include', '/menu');
}

describe('Frontend localhost:8088 - login y CRUD empleados', () => {
  it('valida login fallido y exitoso', () => {
    cy.intercept('GET', '**/api/v2/empleados?page=0&size=1', {
      statusCode: 401,
      body: { message: 'Unauthorized' }
    }).as('loginFail');

    cy.visit('http://localhost:8088/login');
    cy.get('[data-testid="login-user"]').clear().type('bad-user');
    cy.get('[data-testid="login-pass"]').clear().type('bad-pass');
    cy.get('[data-testid="login-btn"]').click();
    cy.wait('@loginFail');
    cy.get('[data-testid="login-error"]').should('contain.text', 'Credenciales invalidas');

    cy.intercept('GET', '**/api/v2/empleados?page=0&size=1', {
      statusCode: 200,
      body: { content: [], number: 0, size: 1, totalElements: 0 }
    }).as('loginOk');

    cy.get('[data-testid="login-user"]').clear().type('admin');
    cy.get('[data-testid="login-pass"]').clear().type('admin123');
    cy.get('[data-testid="login-btn"]').click();
    cy.wait('@loginOk');
    cy.url().should('include', '/menu');
    cy.get('[data-testid="go-empleados"]').should('be.visible');
  });

  it('valida CRUD de empleados en frontend', () => {
    let empleados = [
      {
        clave: 'EMP-001',
        nombre: 'Juan Perez',
        direccion: 'Av. 1',
        telefono: '5551111111',
        departamentoClave: 'TI'
      }
    ];

    loginExitosoEnFrontend();

    cy.intercept('GET', '**/api/v2/departamentos?page=0&size=100', {
      statusCode: 200,
      body: {
        content: [
          { clave: 'TI', nombre: 'Tecnologia', employeeCount: 1 },
          { clave: 'RH', nombre: 'Recursos Humanos', employeeCount: 0 }
        ],
        number: 0,
        size: 100,
        totalElements: 2
      }
    }).as('listarDepartamentosDisponibles');

    cy.intercept('GET', '**/api/v2/empleados?page=0&size=10', (req) => {
      req.reply({
        statusCode: 200,
        body: {
          content: empleados,
          number: 0,
          size: 10,
          totalElements: empleados.length
        }
      });
    }).as('listarEmpleados');

    cy.intercept('POST', '**/api/v2/empleados', (req) => {
      const body = req.body as {
        nombre: string;
        direccion: string;
        telefono: string;
        departamentoClave: string;
      };

      const creado = {
        clave: 'EMP-002',
        nombre: body.nombre,
        direccion: body.direccion,
        telefono: body.telefono,
        departamentoClave: body.departamentoClave
      };

      empleados = [...empleados, creado];
      req.reply({ statusCode: 201, body: creado });
    }).as('crearEmpleado');

    cy.intercept('PUT', '**/api/v2/empleados/*', (req) => {
      const clave = req.url.split('/').pop() ?? '';
      const body = req.body as {
        nombre: string;
        direccion: string;
        telefono: string;
        departamentoClave: string;
      };

      empleados = empleados.map((e) => (e.clave === clave ? { ...e, ...body } : e));
      const actualizado = empleados.find((e) => e.clave === clave);
      req.reply({ statusCode: 200, body: actualizado });
    }).as('actualizarEmpleado');

    cy.intercept('DELETE', '**/api/v2/empleados/*', (req) => {
      const clave = req.url.split('/').pop() ?? '';
      empleados = empleados.filter((e) => e.clave !== clave);
      req.reply({ statusCode: 204, body: '' });
    }).as('eliminarEmpleado');

    cy.get('[data-testid="go-empleados"]').click();
    cy.wait('@listarDepartamentosDisponibles');
    cy.wait('@listarEmpleados');
    cy.get('[data-testid="emp-list"]').should('contain.text', 'EMP-001');

    cy.get('[data-testid="emp-new-nombre"]').type('Ana QA');
    cy.get('[data-testid="emp-new-direccion"]').type('Calle 2');
    cy.get('[data-testid="emp-new-telefono"]').type('5552222222');
    cy.get('[data-testid="emp-new-departamento"]').select('RH');
    cy.get('[data-testid="crear-empleado-btn"]').click();

    cy.wait('@crearEmpleado');
    cy.wait('@listarEmpleados');
    cy.get('[data-testid="emp-ok-msg"]').should('contain.text', 'Empleado creado correctamente');
    cy.get('[data-testid="emp-list"]').should('contain.text', 'Ana QA');

    cy.get('[data-testid="editar-empleado-EMP-001"]').click();
    cy.get('[data-testid="emp-edit-telefono"]').clear().type('5559999999');
    cy.get('[data-testid="guardar-empleado-EMP-001"]').click();

    cy.wait('@actualizarEmpleado');
    cy.wait('@listarEmpleados');
    cy.get('[data-testid="emp-ok-msg"]').should('contain.text', 'Empleado EMP-001 actualizado');
    cy.get('[data-testid="emp-item-EMP-001"]').should('contain.text', '5559999999');

    cy.get('[data-testid="eliminar-empleado-EMP-001"]').click();
    cy.wait('@eliminarEmpleado');
    cy.wait('@listarEmpleados');
    cy.get('[data-testid="emp-ok-msg"]').should('contain.text', 'Empleado EMP-001 eliminado');
    cy.get('[data-testid="emp-list"]').should('not.contain.text', 'EMP-001');
  });
});
