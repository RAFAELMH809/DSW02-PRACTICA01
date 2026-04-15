function loginToMenu(): void {
  cy.intercept('GET', '**/api/v2/empleados?page=0&size=1', {
    statusCode: 200,
    body: { content: [], number: 0, size: 1, totalElements: 0 }
  }).as('loginProbe');

  cy.visit('/login');
  cy.get('[data-testid="login-user"]').clear().type('admin');
  cy.get('[data-testid="login-pass"]').clear().type('admin123');
  cy.get('[data-testid="login-btn"]').click();
  cy.wait('@loginProbe');
  cy.url().should('include', '/menu');
}

describe('Validaciones E2E de login y modulos CRUD', () => {
  it('valida login exitoso y login fallido', () => {
    cy.intercept('GET', '**/api/v2/empleados?page=0&size=1', {
      statusCode: 401,
      body: { message: 'Unauthorized' }
    }).as('loginFail');

    cy.visit('/login');
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
    cy.get('[data-testid="go-departamentos"]').should('be.visible');
  });

  it('valida flujo CRUD completo de departamentos (alta, consulta/listado, actualizacion y eliminacion)', () => {
    let departamentos = [
      { clave: 'TI', nombre: 'Tecnologia', employeeCount: 3 }
    ];

    loginToMenu();

    cy.intercept('GET', '**/api/v2/departamentos?page=0&size=10', (req) => {
      req.reply({
        statusCode: 200,
        body: {
          content: departamentos,
          number: 0,
          size: 10,
          totalElements: departamentos.length
        }
      });
    }).as('listarDepartamentos');

    cy.intercept('POST', '**/api/v2/departamentos', (req) => {
      const body = req.body as { clave: string; nombre: string };
      const nuevo = {
        clave: body.clave.toUpperCase(),
        nombre: body.nombre,
        employeeCount: 0
      };
      departamentos = [nuevo, ...departamentos];
      req.reply({ statusCode: 201, body: nuevo });
    }).as('crearDepartamento');

    cy.intercept('PUT', '**/api/v2/departamentos/*', (req) => {
      const clave = req.url.split('/').pop() ?? '';
      const body = req.body as { nombre: string };
      departamentos = departamentos.map((d) => d.clave === clave ? { ...d, nombre: body.nombre } : d);
      const actualizado = departamentos.find((d) => d.clave === clave);
      req.reply({ statusCode: 200, body: actualizado });
    }).as('actualizarDepartamento');

    cy.intercept('DELETE', '**/api/v2/departamentos/*', (req) => {
      const clave = req.url.split('/').pop() ?? '';
      departamentos = departamentos.filter((d) => d.clave !== clave);
      req.reply({ statusCode: 204, body: '' });
    }).as('eliminarDepartamento');

    cy.get('[data-testid="go-departamentos"]').click();
    cy.wait('@listarDepartamentos');
    cy.get('[data-testid="dept-list"]').should('contain.text', 'TI');

    cy.get('[data-testid="dept-new-clave"]').type('rh');
    cy.get('[data-testid="dept-new-nombre"]').type('Recursos Humanos');
    cy.get('[data-testid="crear-departamento-btn"]').click();

    cy.wait('@crearDepartamento');
    cy.wait('@listarDepartamentos');
    cy.get('[data-testid="dept-ok-msg"]').should('contain.text', 'Departamento RH creado');
    cy.get('[data-testid="dept-item-RH"]').should('contain.text', 'Recursos Humanos');

    cy.get('[data-testid="editar-departamento-RH"]').click();
    cy.get('[data-testid="dept-edit-nombre"]').clear().type('Gestion Humana');
    cy.get('[data-testid="guardar-departamento-RH"]').click();
    cy.wait('@actualizarDepartamento');
    cy.wait('@listarDepartamentos');
    cy.get('[data-testid="dept-ok-msg"]').should('contain.text', 'Departamento RH actualizado');
    cy.get('[data-testid="dept-item-RH"]').should('contain.text', 'Gestion Humana');

    cy.get('[data-testid="eliminar-departamento-RH"]').click();
    cy.wait('@eliminarDepartamento');
    cy.wait('@listarDepartamentos');
    cy.get('[data-testid="dept-ok-msg"]').should('contain.text', 'Departamento RH eliminado');
    cy.get('[data-testid="dept-list"]').should('not.contain.text', 'RH');
  });

  it('valida flujo CRUD completo de empleados (alta, consulta, actualizacion y eliminacion)', () => {
    let empleados = [
      {
        clave: 'EMP-001',
        nombre: 'Juan Perez',
        direccion: 'Av. 1',
        telefono: '5551111111',
        departamentoClave: 'TI'
      }
    ];
    let nextId = 2;

    loginToMenu();

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
        clave: `EMP-00${nextId++}`,
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
      empleados = empleados.map((e) => e.clave === clave ? { ...e, ...body } : e);
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
