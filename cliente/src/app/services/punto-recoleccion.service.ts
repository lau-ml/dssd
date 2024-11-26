import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { PuntoDeRecoleccion } from '../models/punto-recoleccion.dto';
import { PaginatedResponseDTO } from '../models/paginated-response.dto';
import { RecolectorDTO } from '../models/recolector.dto';

@Injectable({
  providedIn: 'root'
})
export class PuntoDeRecoleccionService {
  private apiUrl = 'location'

  constructor(private http: HttpClient) { }

  obtenerMisPuntosDeRecoleccion(): Observable<PuntoDeRecoleccion[]> {
    return this.http.get<PuntoDeRecoleccion[]>(`${environment.urlApi}${this.apiUrl}/my-points`);
  }

  obtenerMisPuntosDeRecoleccionPaginados(
    page: number,
    size: number,
    search: string,
    ordenColumna: string,
    ordenAscendente: boolean
  ): Observable<PaginatedResponseDTO<PuntoDeRecoleccion>> {
    const params = {
      page: page.toString(),
      size: size.toString(),
      search: search || '',
      ordenColumna: ordenColumna ? `${ordenColumna},${ordenAscendente ? 'asc' : 'desc'}` : ''
    };
    return this.http.get<PaginatedResponseDTO<PuntoDeRecoleccion>>(`${environment.urlApi}${this.apiUrl}/my-points/paginated`, { params });
  }

  obtenerPuntosDeRecoleccionPaginados(
    page: number,
    size: number,
    search: string,
    ordenColumna: string,
    ordenAscendente: boolean
  ): Observable<PaginatedResponseDTO<PuntoDeRecoleccion>> {
    const params = {
      page: page.toString(),
      size: size.toString(),
      search: search || '',
      ordenColumna: ordenColumna ? `${ordenColumna},${ordenAscendente ? 'asc' : 'desc'}` : ''
    };
    return this.http.get<PaginatedResponseDTO<PuntoDeRecoleccion>>(`${environment.urlApi}${this.apiUrl}/all-points/paginated`, { params });
  }

  obtenerPuntosDeRecoleccionNoVinculadosPaginados(
    page: number,
    size: number,
    search: string,
    ordenColumna: string,
    ordenAscendente: boolean
  ): Observable<PaginatedResponseDTO<PuntoDeRecoleccion>> {
    const params = {
      page: page.toString(),
      size: size.toString(),
      search: search || '',
      ordenColumna: ordenColumna ? `${ordenColumna},${ordenAscendente ? 'asc' : 'desc'}` : ''
    };
    return this.http.get<PaginatedResponseDTO<PuntoDeRecoleccion>>(`${environment.urlApi}${this.apiUrl}/my-points/not-linked/paginated`, { params });
  }

  desvincularPuntoDeRecoleccion(id: number): Observable<void> {
    return this.http.delete<void>(`${environment.urlApi}${this.apiUrl}/my-points/${id}`);
  }

  crearSolicitudVinculacion(puntoDeRecoleccionId: number): Observable<any> {
    return this.http.post<any>(`${environment.urlApi}solicitudes-vinculacion`, null, {
      params: {
        puntoDeRecoleccionId: puntoDeRecoleccionId.toString()
      }
    });
  }

  obtenerPuntoPorId(id: number): Observable<PuntoDeRecoleccion> {
    return this.http.get<PuntoDeRecoleccion>(`${environment.urlApi}${this.apiUrl}/${id}`);
  }

  editarPunto(id: number, punto: PuntoDeRecoleccion): Observable<void> {
    return this.http.put<void>(`${environment.urlApi}${this.apiUrl}/${id}`, punto);
  }

  crearPunto(punto: PuntoDeRecoleccion): Observable<void> {
    return this.http.post<void>(`${environment.urlApi}${this.apiUrl}/create-point`, punto);
  }

  deletePunto(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<HttpResponse<any>>(`${environment.urlApi}${this.apiUrl}/delete-point/${id}`);
  }

  obtenerRecolectoresPorPunto(puntoId: number, page: number, size: number, search: string): Observable<PaginatedResponseDTO<RecolectorDTO>> {
    const params = { page, size, search };
    return this.http.get<PaginatedResponseDTO<RecolectorDTO>>(`${environment.urlApi}${this.apiUrl}/${puntoId}/recolectores`, { params });
  }

  obtenerRecolectoresNoAsociados(puntoId: number, page: number, size: number, searchTerm: string = '') {
    const params = { page: `${page}`, size: `${size}`, search: searchTerm };
    return this.http.get<PaginatedResponseDTO<RecolectorDTO>>(`${environment.urlApi}${this.apiUrl}/${puntoId}/recolectores-no-asociados`, { params });
  }

  vincularRecolectorAPunto(puntoId: number, recolectorId: number): Observable<any> {
    return this.http.post<any>(`${environment.urlApi}${this.apiUrl}/${puntoId}/vincular-recolector`, { recolectorId });
  }

  desvincularRecolectorDePunto(puntoId: number, recolectorId: number): Observable<void> {
    return this.http.delete<void>(`${environment.urlApi}${this.apiUrl}/${puntoId}/desvincular-recolector/${recolectorId}`);
  }

  obtenerPuntosDeRecoleccionPorUsuario(
    recolectorId: string,
    page: number,
    size: number,
    searchTerm: string,
    sortColumn: string,
    asc: boolean
  ): Observable<PaginatedResponseDTO<PuntoDeRecoleccion>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('search', searchTerm)
      .set('ordenColumna', sortColumn)
      .set('ordenAscendente', asc.toString())
      .set('recolectorId', recolectorId);
    return this.http.get<PaginatedResponseDTO<PuntoDeRecoleccion>>(`${environment.urlApi}${this.apiUrl}/all-collection-points`, { params });
  }

  desvincularPuntoDeRecolector(recolectorId: string, puntoId: number): Observable<void> {
    return this.http.delete<void>(`${environment.urlApi}${this.apiUrl}/recolector/${recolectorId}/puntos/${puntoId}`);
  }

  obtenerPuntosDeRecoleccionNoVinculadosRecolector(
    recolectorId: string,
    page: number,
    size: number,
    searchTerm: string,
    sortColumn: string,
    asc: boolean,
  ): Observable<PaginatedResponseDTO<PuntoDeRecoleccion>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('search', searchTerm)
      .set('ordenColumna', sortColumn)
      .set('ordenAscendente', asc.toString());
    return this.http.get<PaginatedResponseDTO<PuntoDeRecoleccion>>(`${environment.urlApi}${this.apiUrl}/recolector/${recolectorId}/puntos-no-asociados`, { params });
  }

}
