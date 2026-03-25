export interface EmpleadoResponse {
  clave: string;
  nombre: string;
  direccion: string;
  telefono: string;
  departamentoClave: string;
}

export interface EmpleadoPage {
  content: EmpleadoResponse[];
  number: number;
  size: number;
  totalElements: number;
}

export interface EmpleadoRequest {
  nombre: string;
  direccion: string;
  telefono: string;
  departamentoClave: string;
}

export interface DepartamentoResponse {
  clave: string;
  nombre: string;
  employeeCount: number;
}

export interface DepartamentoPage {
  content: DepartamentoResponse[];
  number: number;
  size: number;
  totalElements: number;
}

export interface DepartamentoRequest {
  clave: string;
  nombre: string;
}
