import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';

import { AuthSessionService } from './auth-session.service';

@Component({
  selector: 'app-menu-page',
  imports: [CommonModule, RouterLink],
  templateUrl: './menu-page.component.html'
})
export class MenuPageComponent {
  private readonly router = inject(Router);
  private readonly auth = inject(AuthSessionService);

  constructor() {
    if (!this.auth.isAuthenticated()) {
      this.router.navigate(['/login']);
    }
  }

  protected async logout(): Promise<void> {
    this.auth.clear();
    await this.router.navigate(['/login']);
  }
}
