import { Routes } from '@angular/router';
import { DepartamentosPageComponent } from './departamentos-page.component';
import { EmpleadosPageComponent } from './empleados-page.component';
import { LoginPageComponent } from './login-page.component';
import { MenuPageComponent } from './menu-page.component';

export const routes: Routes = [
	{ path: '', pathMatch: 'full', redirectTo: 'login' },
	{ path: 'login', component: LoginPageComponent },
	{ path: 'menu', component: MenuPageComponent },
	{ path: 'empleados', component: EmpleadosPageComponent },
	{ path: 'departamentos', component: DepartamentosPageComponent },
	{ path: '**', redirectTo: 'login' }
];
