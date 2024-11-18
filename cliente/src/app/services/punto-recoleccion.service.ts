import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { PuntoDeRecoleccion } from '../models/punto-recoleccion.dto';
import { PaginatedResponseDTO } from '../models/paginated-response.dto';

@Injectable({
  providedIn: 'root'
})
export class PuntoDeRecoleccionService {
  private apiUrl = 'location'

  constructor(private http: HttpClient) { }

  obtenerMisPuntosDeRecoleccion(): Observable<PuntoDeRecoleccion[]> {
    return this.http.get<PuntoDeRecoleccion[]>(`${environment.urlApi}${this.apiUrl}/my-points`);
  }

  obtenerMisPuntosDeRecoleccionPaginados(page: number, size: number, search: string): Observable<PaginatedResponseDTO<PuntoDeRecoleccion>> {
    return this.http.get<PaginatedResponseDTO<PuntoDeRecoleccion>>(`${environment.urlApi}${this.apiUrl}/my-points/paginated`, {
      params: {
        page: page.toString(),
        size: size.toString(),
        search: search
      }
    });
  }

  obtenerPuntosDeRecoleccionPaginados(page: number, size: number, search: string): Observable<PaginatedResponseDTO<PuntoDeRecoleccion>> {
    return this.http.get<PaginatedResponseDTO<PuntoDeRecoleccion>>(`${environment.urlApi}${this.apiUrl}/all-points/paginated`, {
      params: {
        page: page.toString(),
        size: size.toString(),
        search: search
      }
    });
  }

  obtenerPuntosDeRecoleccionNoVinculadosPaginados(page: number, size: number, search: string): Observable<PaginatedResponseDTO<PuntoDeRecoleccion>> {
    return this.http.get<PaginatedResponseDTO<PuntoDeRecoleccion>>(`${environment.urlApi}${this.apiUrl}/my-points/not-linked/paginated`, {
      params: {
        page: page.toString(),
        size: size.toString(),
        search: search
      }
    });
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
    return this.http.put<void>(`${environment.urlApi}/${id}`, punto);
  }
}
