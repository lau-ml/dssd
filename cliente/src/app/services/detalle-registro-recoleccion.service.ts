import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DetalleRegistro } from '../models/detalle-registro.dto';
import { Observable } from 'rxjs';
import { RegistroRecoleccion } from '../models/registro-recoleccion.dto';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DetalleRegistroRecoleccionService {
  private apiUrl = 'record-details';

  constructor(private http: HttpClient) { }

  addNewMaterial(newMaterial: DetalleRegistro): Observable<RegistroRecoleccion> {
    return this.http.post<RegistroRecoleccion>(`${environment.urlApi}${this.apiUrl}/add-new-material`, newMaterial);
  }

}
