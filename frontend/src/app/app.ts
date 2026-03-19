import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { firstValueFrom } from 'rxjs';

type LoginScope = 'empleados' | 'departamentos';

interface LoginForm {
  username: string;
  password: string;
}

interface DepartamentoResponse {
  clave: string;
  nombre: string;
  employeeCount: number;
}

interface DepartamentoPage {
  content: DepartamentoResponse[];
  number: number;
  size: number;
  totalElements: number;
}

interface EmpleadoResponse {
  clave: string;
  nombre: string;
  direccion: string;
  telefono: string;
  departamentoClave: string;
}

interface EmpleadoPage {
  content: EmpleadoResponse[];
  number: number;
  size: number;
  totalElements: number;
}

@Component({
  selector: 'app-root',
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  private readonly http = inject(HttpClient);
  private readonly apiBase = '';

  protected empleadosLogin: LoginForm = { username: 'admin', password: 'admin123' };
  protected departamentosLogin: LoginForm = { username: 'admin', password: 'admin123' };

  protected empleadosAutenticado = signal(false);
  protected departamentosAutenticado = signal(false);
  protected cargando = signal(false);
  protected mensaje = signal('');
  protected error = signal('');

  protected departamentos = signal<DepartamentoResponse[]>([]);
  protected page = signal(0);
  protected size = signal(10);
  protected total = signal(0);

  protected empleados = signal<EmpleadoResponse[]>([]);
  protected empleadosPage = signal(0);
  protected empleadosSize = signal(10);
  protected empleadosTotal = signal(0);

  protected nuevoDepartamento = { clave: '', nombre: '' };
  protected nuevoEmpleado = {
    nombre: '',
    direccion: '',
    telefono: '',
    departamentoClave: 'SIN_DEPTO'
  };

  protected editandoEmpleadoClave = signal<string | null>(null);
  protected empleadoEdicion = {
    nombre: '',
    direccion: '',
    telefono: '',
    departamentoClave: ''
  };

  private authHeaderEmpleados = '';
  private authHeaderDepartamentos = '';

  protected get loginCompleto(): boolean {
    return this.empleadosAutenticado() && this.departamentosAutenticado();
  }

  protected async autenticar(scope: LoginScope): Promise<void> {
    this.cargando.set(true);
    this.error.set('');
    this.mensaje.set('');

    const credenciales = scope === 'empleados' ? this.empleadosLogin : this.departamentosLogin;
    const endpoint = scope === 'empleados'
      ? `${this.apiBase}/api/v2/empleados?page=0&size=1`
      : `${this.apiBase}/api/v2/departamentos?page=0&size=1`;

    try {
      const headers = this.buildAuthHeaders(credenciales.username, credenciales.password);
      await firstValueFrom(this.http.get(endpoint, { headers }));

      if (scope === 'empleados') {
        this.empleadosAutenticado.set(true);
        this.authHeaderEmpleados = headers.get('Authorization') ?? '';
        this.mensaje.set('Login de Empleados correcto.');
        await this.cargarEmpleados();
      } else {
        this.departamentosAutenticado.set(true);
        this.authHeaderDepartamentos = headers.get('Authorization') ?? '';
        this.mensaje.set('Login de Departamentos correcto.');
        await this.cargarDepartamentos();
      }

      if (this.loginCompleto) {
        this.mensaje.set('Login secuencial completado. CRUD de Empleados y Departamentos habilitado.');
      }
    } catch (err: unknown) {
      this.error.set(this.mapError(scope, err));
    } finally {
      this.cargando.set(false);
    }
  }

  protected async crearDepartamento(): Promise<void> {
    if (!this.loginCompleto) {
      this.error.set('Primero debes completar ambos logins.');
      return;
    }

    this.cargando.set(true);
    this.error.set('');
    this.mensaje.set('');

    try {
      await firstValueFrom(this.http.post<DepartamentoResponse>(
        `${this.apiBase}/api/v2/departamentos`,
        {
          clave: this.nuevoDepartamento.clave,
          nombre: this.nuevoDepartamento.nombre
        },
        { headers: this.headersDepartamento() }
      ));

      this.mensaje.set(`Departamento ${this.nuevoDepartamento.clave.toUpperCase()} creado.`);
      this.nuevoDepartamento = { clave: '', nombre: '' };
      await this.cargarDepartamentos();
    } catch (err: unknown) {
      this.error.set(this.mapError('departamentos', err));
    } finally {
      this.cargando.set(false);
    }
  }

  protected async cargarDepartamentos(): Promise<void> {
    if (!this.loginCompleto) {
      return;
    }

    this.cargando.set(true);
    this.error.set('');

    try {
      const response = await firstValueFrom(this.http.get<DepartamentoPage>(
        `${this.apiBase}/api/v2/departamentos?page=${this.page()}&size=${this.size()}`,
        { headers: this.headersDepartamento() }
      ));

      this.departamentos.set(response.content ?? []);
      this.total.set(response.totalElements ?? 0);
      this.page.set(response.number ?? this.page());
      this.size.set(response.size ?? this.size());
    } catch (err: unknown) {
      this.error.set(this.mapError('departamentos', err));
    } finally {
      this.cargando.set(false);
    }
  }

  protected async crearEmpleado(): Promise<void> {
    if (!this.empleadosAutenticado()) {
      this.error.set('Primero debes autenticar Empleados.');
      return;
    }

    this.cargando.set(true);
    this.error.set('');
    this.mensaje.set('');

    try {
      await firstValueFrom(this.http.post<EmpleadoResponse>(
        `${this.apiBase}/api/v2/empleados`,
        {
          nombre: this.nuevoEmpleado.nombre,
          direccion: this.nuevoEmpleado.direccion,
          telefono: this.nuevoEmpleado.telefono,
          departamentoClave: this.nuevoEmpleado.departamentoClave
        },
        { headers: this.headersEmpleado() }
      ));

      this.mensaje.set('Empleado creado correctamente.');
      this.nuevoEmpleado = {
        nombre: '',
        direccion: '',
        telefono: '',
        departamentoClave: 'SIN_DEPTO'
      };
      await this.cargarEmpleados();
    } catch (err: unknown) {
      this.error.set(this.mapError('empleados', err));
    } finally {
      this.cargando.set(false);
    }
  }

  protected async cargarEmpleados(): Promise<void> {
    if (!this.empleadosAutenticado()) {
      return;
    }

    this.cargando.set(true);
    this.error.set('');

    try {
      const response = await firstValueFrom(this.http.get<EmpleadoPage>(
        `${this.apiBase}/api/v2/empleados?page=${this.empleadosPage()}&size=${this.empleadosSize()}`,
        { headers: this.headersEmpleado() }
      ));

      this.empleados.set(response.content ?? []);
      this.empleadosTotal.set(response.totalElements ?? 0);
      this.empleadosPage.set(response.number ?? this.empleadosPage());
      this.empleadosSize.set(response.size ?? this.empleadosSize());
    } catch (err: unknown) {
      this.error.set(this.mapError('empleados', err));
    } finally {
      this.cargando.set(false);
    }
  }

  protected iniciarEdicionEmpleado(empleado: EmpleadoResponse): void {
    this.editandoEmpleadoClave.set(empleado.clave);
    this.empleadoEdicion = {
      nombre: empleado.nombre,
      direccion: empleado.direccion,
      telefono: empleado.telefono,
      departamentoClave: empleado.departamentoClave
    };
  }

  protected cancelarEdicionEmpleado(): void {
    this.editandoEmpleadoClave.set(null);
    this.empleadoEdicion = {
      nombre: '',
      direccion: '',
      telefono: '',
      departamentoClave: ''
    };
  }

  protected async guardarEdicionEmpleado(clave: string): Promise<void> {
    if (!this.empleadosAutenticado()) {
      this.error.set('Primero debes autenticar Empleados.');
      return;
    }

    this.cargando.set(true);
    this.error.set('');
    this.mensaje.set('');

    try {
      await firstValueFrom(this.http.put<EmpleadoResponse>(
        `${this.apiBase}/api/v2/empleados/${clave}`,
        {
          nombre: this.empleadoEdicion.nombre,
          direccion: this.empleadoEdicion.direccion,
          telefono: this.empleadoEdicion.telefono,
          departamentoClave: this.empleadoEdicion.departamentoClave
        },
        { headers: this.headersEmpleado() }
      ));

      this.mensaje.set(`Empleado ${clave} actualizado.`);
      this.cancelarEdicionEmpleado();
      await this.cargarEmpleados();
    } catch (err: unknown) {
      this.error.set(this.mapError('empleados', err));
    } finally {
      this.cargando.set(false);
    }
  }

  protected async eliminarEmpleado(clave: string): Promise<void> {
    if (!this.empleadosAutenticado()) {
      this.error.set('Primero debes autenticar Empleados.');
      return;
    }

    this.cargando.set(true);
    this.error.set('');
    this.mensaje.set('');

    try {
      await firstValueFrom(this.http.delete<void>(
        `${this.apiBase}/api/v2/empleados/${clave}`,
        { headers: this.headersEmpleado() }
      ));

      this.mensaje.set(`Empleado ${clave} eliminado.`);
      if (this.editandoEmpleadoClave() === clave) {
        this.cancelarEdicionEmpleado();
      }
      await this.cargarEmpleados();
    } catch (err: unknown) {
      this.error.set(this.mapError('empleados', err));
    } finally {
      this.cargando.set(false);
    }
  }

  protected cerrarSesion(): void {
    this.empleadosAutenticado.set(false);
    this.departamentosAutenticado.set(false);
    this.authHeaderEmpleados = '';
    this.authHeaderDepartamentos = '';
    this.empleados.set([]);
    this.empleadosTotal.set(0);
    this.empleadosPage.set(0);
    this.cancelarEdicionEmpleado();
    this.departamentos.set([]);
    this.total.set(0);
    this.page.set(0);
    this.mensaje.set('Sesion cerrada.');
    this.error.set('');
  }

  private buildAuthHeaders(username: string, password: string): HttpHeaders {
    return new HttpHeaders({
      Authorization: `Basic ${btoa(`${username}:${password}`)}`
    });
  }

  private headersDepartamento(): HttpHeaders {
    return new HttpHeaders({
      Authorization: this.authHeaderDepartamentos
    });
  }

  private headersEmpleado(): HttpHeaders {
    return new HttpHeaders({
      Authorization: this.authHeaderEmpleados
    });
  }

  private mapError(scope: LoginScope, err: unknown): string {
    const errorObj = err as { status?: number; error?: { message?: string } };
    const msg = errorObj.error?.message;
    if (msg) {
      return msg;
    }

    if (errorObj.status === 401) {
      return `Credenciales invalidas para ${scope}.`;
    }

    return `No fue posible conectar con ${scope}. Verifica que el backend este activo en localhost:8081.`;
  }
}
