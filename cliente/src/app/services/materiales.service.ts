import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Material } from '../models/material.dto';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MaterialesService {
  private apiUrl = 'material'

  constructor(private http: HttpClient) { }

  obtenerMateriales(): Observable<Material[]> {
    return this.http.get<Material[]>(`${environment.urlApi}${this.apiUrl}/get-materials`);
  }
}
