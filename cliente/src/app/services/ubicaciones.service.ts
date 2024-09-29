import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Ubicacion } from '../models/ubicacion.dto';

@Injectable({
  providedIn: 'root'
})
export class UbicacionesService {
  private apiUrl = 'location'

  constructor(private http: HttpClient) { }

  obtenerUbicaciones(): Observable<Ubicacion[]> {
    return this.http.get<Ubicacion[]>(`${environment.urlApi}${this.apiUrl}/get-locations`);
  }
}
