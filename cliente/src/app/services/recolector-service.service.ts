import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RecolectorDTO } from '../models/recolector.dto';
import { RecolectorDTO as recolectorAdminDTO } from '../models/recolector.admin.dto';
import { PaginatedResponseDTO } from '../models/paginated-response.dto';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RecolectorServiceService {
  private apiUrl = 'collector';

  constructor(private http: HttpClient) { }

  obtenerRecolectores(
    page: number,
    size: number,
    searchTerm: string = '',
    ordenColumna: string,
    ordenAscendente: boolean
  ): Observable<PaginatedResponseDTO<recolectorAdminDTO>> {
    const params = {
      page: page.toString(),
      size: size.toString(),
      search: searchTerm || '',
      ordenColumna: ordenColumna ? `${ordenColumna},${ordenAscendente ? 'asc' : 'desc'}` : ''
    };
    return this.http.get<PaginatedResponseDTO<recolectorAdminDTO>>(`${environment.urlApi}${this.apiUrl}/all-collectors`, { params });
  }

  obtenerRecolectorPorId(id: string): Observable<recolectorAdminDTO> {
    return this.http.get<recolectorAdminDTO>(`${environment.urlApi}${this.apiUrl}/${id}`);
  }

  activarRecolector(id: number): Observable<string> {
    return this.http.patch<string>(`${environment.urlApi}${this.apiUrl}/${id}/activar`, {});
  }

  desactivarRecolector(id: number): Observable<string> {
    return this.http.patch<string>(`${environment.urlApi}${this.apiUrl}/${id}/desactivar`, {});
  }
}
