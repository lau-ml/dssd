import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {RegisterRequest} from "../_requests/registerRequest";
import {catchError, map} from "rxjs/operators";
import {throwError} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  constructor(private http: HttpClient) {

  }

  register(register: RegisterRequest) {
    return this.http.post<any>(environment.urlHost+"auth/register", register).pipe(
      map((userData) => userData.message)
    )

  }



}
