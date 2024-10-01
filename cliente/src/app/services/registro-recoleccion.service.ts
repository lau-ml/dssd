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

  obtenerUltimoRegistro(collectorId: Number): Observable<RegistroRecoleccion> {
    return this.http.get<RegistroRecoleccion>(`${environment.urlApi}${this.apiUrl}/collector/${collectorId}`);
  }

  completarRegistro(id: Number): Observable<any> {
    return this.http.put(`${environment.urlApi}${this.apiUrl}/${id}/complete`, null);
  }
}
