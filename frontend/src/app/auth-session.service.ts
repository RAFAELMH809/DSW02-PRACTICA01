import { Injectable } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthSessionService {
  private static readonly STORAGE_KEY = 'ops_auth_header';

  setCredentials(username: string, password: string): void {
    const token = btoa(`${username}:${password}`);
    sessionStorage.setItem(AuthSessionService.STORAGE_KEY, `Basic ${token}`);
  }

  clear(): void {
    sessionStorage.removeItem(AuthSessionService.STORAGE_KEY);
  }

  isAuthenticated(): boolean {
    return Boolean(sessionStorage.getItem(AuthSessionService.STORAGE_KEY));
  }

  authHeaders(): HttpHeaders {
    const auth = sessionStorage.getItem(AuthSessionService.STORAGE_KEY) ?? '';
    return new HttpHeaders({ Authorization: auth });
  }
}
