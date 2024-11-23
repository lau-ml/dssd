import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PaginatedResponseDTO } from '../models/paginated-response.dto';
import { PagoDTO } from '../models/pago.dto';
import { HttpClient, HttpParams } from '@angular/common/http';
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
    estado: string,
    columnaFecha: string,
    fechaDesde: string | null,
    fechaHasta: string | null,
    orden: string,
    asc: boolean,
    recolectorId: string
  ): Observable<PaginatedResponseDTO<PagoDTO>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('estado', estado)
      .set('columnaFecha', columnaFecha)
      .set('fechaDesde', fechaDesde || '')
      .set('fechaHasta', fechaHasta || '')
      .set('orden', orden)
      .set('asc', asc.toString())
      .set('recolectorId', recolectorId);
    return this.http.get<PaginatedResponseDTO<PagoDTO>>(`${environment.urlApi}${this.apiUrl}/get-collector/paginated`, { params });
  }
}
