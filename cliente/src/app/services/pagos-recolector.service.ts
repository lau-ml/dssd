import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PaginatedResponseDTO } from '../models/paginated-response.dto';
import { PagoDTO } from '../models/pago.dto';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PagosRecolectorService {
  private apiUrl = 'pagos'

  constructor(private http: HttpClient) { }

  obtenerPagosRecolector(
    page: number,
    size: number,
    search: string,
    ordenColumna: string,
    ordenAscendente: boolean,
    recolectorId: string
  ): Observable<PaginatedResponseDTO<PagoDTO>> {
    const params = {
      page: page.toString(),
      size: size.toString(),
      search: search || '',
      ordenColumna: ordenColumna ? `${ordenColumna},${ordenAscendente ? 'asc' : 'desc'}` : ''
    };
    return this.http.get<PaginatedResponseDTO<PagoDTO>>(`${environment.urlApi}${this.apiUrl}/get-collector/paginated?recolectorId=${recolectorId}`, { params });
  }
}
