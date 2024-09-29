import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegistroRecoleccion } from '../models/registro-recoleccion.dto';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RegistroRecoleccionService {
  private apiUrl = 'collection-record';

  constructor(private http: HttpClient) { }

  // Obtener el último registro de recolección
  obtenerUltimoRegistro(id_user: string): Observable<RegistroRecoleccion> {
    return this.http.get<RegistroRecoleccion>(`${environment.urlApi}${this.apiUrl}/collector/${id_user}`);
  }

}
