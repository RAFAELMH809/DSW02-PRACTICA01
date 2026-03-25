import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { AuthSessionService } from './auth-session.service';

@Component({
  selector: 'app-login-page',
  imports: [CommonModule, FormsModule],
  templateUrl: './login-page.component.html'
})
export class LoginPageComponent {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly auth = inject(AuthSessionService);

  protected username = 'admin';
  protected password = 'admin123';
  protected cargando = signal(false);
  protected error = signal('');

  constructor() {
    if (this.auth.isAuthenticated()) {
      this.router.navigate(['/menu']);
    }
  }

  protected async login(): Promise<void> {
    this.cargando.set(true);
    this.error.set('');

    try {
      this.auth.setCredentials(this.username, this.password);
      await firstValueFrom(this.http.get('/api/v2/empleados?page=0&size=1', {
        headers: this.auth.authHeaders()
      }));
      await this.router.navigate(['/menu']);
    } catch {
      this.auth.clear();
      this.error.set('Credenciales invalidas o backend no disponible en localhost:8081.');
    } finally {
      this.cargando.set(false);
    }
  }
}
