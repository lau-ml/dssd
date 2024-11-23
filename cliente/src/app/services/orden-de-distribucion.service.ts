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

  obtenerOrdenesPaginadas(
    page: number,
    size: number,
    searchTerm: string,
    estadoFiltro: string,
    ordenColumna: string,
    ordenAscendente: boolean
  ): Observable<PaginatedResponseDTO<OrdenDeDistribucion>> {
    const params = {
      page: page.toString(),
      size: size.toString(),
      search: searchTerm || '',
      estado: estadoFiltro || '',
      ordenColumna: ordenColumna ? `${ordenColumna},${ordenAscendente ? 'asc' : 'desc'}` : ''
    };
    return this.http.get<PaginatedResponseDTO<OrdenDeDistribucion>>(`${environment.urlApi}${this.apiUrl}`, { params });
  }

  cambiarEstado(id: number, nuevoEstado: string): Observable<void> {
    return this.http.put<void>(`${environment.urlApi}${this.apiUrl}/${id}/estado`, { estado: nuevoEstado });
  }
}
