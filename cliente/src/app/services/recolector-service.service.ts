import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RecolectorDTO } from '../models/recolector.dto';
import { PaginatedResponseDTO } from '../models/paginated-response.dto';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RecolectorServiceService {
  private apiUrl = 'collector';

  constructor(private http: HttpClient) { }

  obtenerRecolectores(page: number, size: number, searchTerm: string = ''): Observable<PaginatedResponseDTO<RecolectorDTO>> {
    const params = { page: `${page}`, size: `${size}`, search: searchTerm };
    return this.http.get<PaginatedResponseDTO<RecolectorDTO>>(`${environment.urlApi}${this.apiUrl}/all-collectors`, { params });
  }
}
