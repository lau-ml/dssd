import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { OrdenDeDistribucion } from '../models/orden-de-distribucion.dto';
import { environment } from '../../environments/environment';
import { PaginatedResponseDTO } from '../models/paginated-response.dto';

@Injectable({
  providedIn: 'root'
})
export class OrdenDeDistribucionService {
  private apiUrl = 'ordenes'

  constructor(private http: HttpClient) { }

  obtenerOrdenesPaginadas(page: number, size: number, search: string = ''): Observable<PaginatedResponseDTO<OrdenDeDistribucion>> {
    return this.http.get<PaginatedResponseDTO<OrdenDeDistribucion>>(`${environment.urlApi}${this.apiUrl}`, {
      params: {
        page: page.toString(),
        size: size.toString(),
        search: search
      }
    });
  }

  cambiarEstado(id: number, nuevoEstado: string): Observable<void> {
    return this.http.put<void>(`${environment.urlApi}${this.apiUrl}/${id}/estado`, { estado: nuevoEstado });
  }
}
