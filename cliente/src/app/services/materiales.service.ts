import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { Material } from '../models/material.dto';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { PaginatedResponseDTO } from '../models/paginated-response.dto';

@Injectable({
  providedIn: 'root'
})
export class MaterialesService {
  private apiUrl = 'material'

  constructor(private http: HttpClient) { }

  obtenerMateriales(): Observable<Material[]> {
    return this.http.get<Material[]>(`${environment.urlApi}${this.apiUrl}/get-materials`);
  }

  obtenerMaterialPorId(id: number): Observable<Material> {
    return this.http.get<Material>(`${environment.urlApi}${this.apiUrl}/get-material/${id}`);
  }

  editarMaterial(id: number, material: Material): Observable<Material> {
    return this.http.put<Material>(`${environment.urlApi}${this.apiUrl}/edit-material/${id}`, material);
  }

  obtenerMaterialesPaginados(page: number, size: number, search: string): Observable<PaginatedResponseDTO<Material>> {
    return this.http.get<PaginatedResponseDTO<Material>>(`${environment.urlApi}${this.apiUrl}/get-materials-paginated`, {
      params: {
        page: page.toString(),
        size: size.toString(),
        search: search
      }
    });
  }

  eliminarMaterial(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<HttpResponse<any>>(`${environment.urlApi}${this.apiUrl}/delete-material/${id}`);
  }
}
