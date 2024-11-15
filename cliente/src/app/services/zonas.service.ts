import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Zona} from "../models/zona";

@Injectable({
  providedIn: 'root'
})

export class ZonasService {
  constructor(private http: HttpClient) {
  }

  getZonas() {
    return this.http.get<Zona[]>(environment.urlApi + "zones/get-all");
  }
}
