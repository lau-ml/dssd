import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { PuntoDeRecoleccion } from '../models/punto-recoleccion.dto';

@Injectable({
  providedIn: 'root'
})
export class PuntoDeRecoleccionService {
  private apiUrl = 'location'

  constructor(private http: HttpClient) { }

  obtenerPuntosDeRecoleccion(): Observable<PuntoDeRecoleccion[]> {
    return this.http.get<PuntoDeRecoleccion[]>(`${environment.urlApi}${this.apiUrl}/get-locations`);
  }
}
