import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { AuthSessionService } from './auth-session.service';
import { DepartamentoPage, EmpleadoPage, EmpleadoRequest, EmpleadoResponse } from './models';

@Component({
  selector: 'app-empleados-page',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './empleados-page.component.html'
})
export class EmpleadosPageComponent {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly auth = inject(AuthSessionService);

  protected cargando = signal(false);
  protected mensaje = signal('');
  protected error = signal('');

  protected empleados = signal<EmpleadoResponse[]>([]);
  protected departamentosDisponibles = signal<string[]>(['SIN_DEPTO']);
  protected empleadosPage = signal(0);
  protected empleadosSize = signal(10);
  protected empleadosTotal = signal(0);

  protected nuevoEmpleado: EmpleadoRequest = {
    nombre: '',
    direccion: '',
    telefono: '',
    departamentoClave: 'SIN_DEPTO'
  };

  protected editandoEmpleadoClave = signal<string | null>(null);
  protected empleadoEdicion: EmpleadoRequest = {
    nombre: '',
    direccion: '',
    telefono: '',
    departamentoClave: ''
  };

  constructor() {
    if (!this.auth.isAuthenticated()) {
      this.router.navigate(['/login']);
      return;
    }
    this.cargarDepartamentosDisponibles();
    this.cargarEmpleados();
  }

  private async cargarDepartamentosDisponibles(): Promise<void> {
    try {
      const response = await firstValueFrom(this.http.get<DepartamentoPage>(
        '/api/v2/departamentos?page=0&size=100',
        { headers: this.auth.authHeaders() }
      ));

      const fromApi = (response.content ?? []).map((d) => d.clave).filter((clave) => Boolean(clave));
      const all = Array.from(new Set(['SIN_DEPTO', ...fromApi]));
      this.departamentosDisponibles.set(all);
    } catch {
      this.departamentosDisponibles.set(['SIN_DEPTO']);
    }
  }

  protected async cargarEmpleados(): Promise<void> {
    this.cargando.set(true);
    this.error.set('');

    try {
      const response = await firstValueFrom(this.http.get<EmpleadoPage>(
        `/api/v2/empleados?page=${this.empleadosPage()}&size=${this.empleadosSize()}`,
        { headers: this.auth.authHeaders() }
      ));

      this.empleados.set(response.content ?? []);
      this.empleadosTotal.set(response.totalElements ?? 0);
    } catch {
      this.error.set('No fue posible consultar empleados.');
    } finally {
      this.cargando.set(false);
    }
  }

  protected async crearEmpleado(): Promise<void> {
    this.cargando.set(true);
    this.error.set('');
    this.mensaje.set('');

    try {
      await firstValueFrom(this.http.post<EmpleadoResponse>('/api/v2/empleados', this.nuevoEmpleado, {
        headers: this.auth.authHeaders()
      }));

      this.mensaje.set('Empleado creado correctamente.');
      this.nuevoEmpleado = { nombre: '', direccion: '', telefono: '', departamentoClave: 'SIN_DEPTO' };
      await this.cargarEmpleados();
    } catch {
      this.error.set('No fue posible crear el empleado.');
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
    this.empleadoEdicion = { nombre: '', direccion: '', telefono: '', departamentoClave: '' };
  }

  protected async guardarEdicionEmpleado(clave: string): Promise<void> {
    this.cargando.set(true);
    this.error.set('');
    this.mensaje.set('');

    try {
      await firstValueFrom(this.http.put<EmpleadoResponse>(`/api/v2/empleados/${clave}`, this.empleadoEdicion, {
        headers: this.auth.authHeaders()
      }));
      this.mensaje.set(`Empleado ${clave} actualizado.`);
      this.cancelarEdicionEmpleado();
      await this.cargarEmpleados();
    } catch {
      this.error.set('No fue posible actualizar el empleado.');
    } finally {
      this.cargando.set(false);
    }
  }

  protected async eliminarEmpleado(clave: string): Promise<void> {
    this.cargando.set(true);
    this.error.set('');
    this.mensaje.set('');

    try {
      await firstValueFrom(this.http.delete<void>(`/api/v2/empleados/${clave}`, {
        headers: this.auth.authHeaders()
      }));
      this.mensaje.set(`Empleado ${clave} eliminado.`);
      if (this.editandoEmpleadoClave() === clave) {
        this.cancelarEdicionEmpleado();
      }
      await this.cargarEmpleados();
    } catch {
      this.error.set('No fue posible eliminar el empleado.');
    } finally {
      this.cargando.set(false);
    }
  }
}
