import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { AuthSessionService } from './auth-session.service';
import { DepartamentoPage, DepartamentoRequest, DepartamentoResponse } from './models';

@Component({
  selector: 'app-departamentos-page',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './departamentos-page.component.html'
})
export class DepartamentosPageComponent {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly auth = inject(AuthSessionService);

  protected cargando = signal(false);
  protected mensaje = signal('');
  protected error = signal('');

  protected departamentos = signal<DepartamentoResponse[]>([]);
  protected page = signal(0);
  protected size = signal(10);
  protected total = signal(0);

  protected nuevoDepartamento: DepartamentoRequest = { clave: '', nombre: '' };

  constructor() {
    if (!this.auth.isAuthenticated()) {
      this.router.navigate(['/login']);
      return;
    }
    this.cargarDepartamentos();
  }

  protected async crearDepartamento(): Promise<void> {
    this.cargando.set(true);
    this.error.set('');
    this.mensaje.set('');

    try {
      await firstValueFrom(this.http.post<DepartamentoResponse>('/api/v2/departamentos', this.nuevoDepartamento, {
        headers: this.auth.authHeaders()
      }));

      this.mensaje.set(`Departamento ${this.nuevoDepartamento.clave.toUpperCase()} creado.`);
      this.nuevoDepartamento = { clave: '', nombre: '' };
      await this.cargarDepartamentos();
    } catch {
      this.error.set('No fue posible crear el departamento.');
    } finally {
      this.cargando.set(false);
    }
  }

  protected async cargarDepartamentos(): Promise<void> {
    this.cargando.set(true);
    this.error.set('');

    try {
      const response = await firstValueFrom(this.http.get<DepartamentoPage>(
        `/api/v2/departamentos?page=${this.page()}&size=${this.size()}`,
        { headers: this.auth.authHeaders() }
      ));

      this.departamentos.set(response.content ?? []);
      this.total.set(response.totalElements ?? 0);
    } catch {
      this.error.set('No fue posible consultar departamentos.');
    } finally {
      this.cargando.set(false);
    }
  }
}
