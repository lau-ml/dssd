import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class CentrosService {

  constructor(private http: HttpClient) {
  }

  getCentrosByZona(idZona: number) {
    return this.http.get<any>(environment.urlApi + "centers/get-centers-by-zone/" + idZona);
  }
}
