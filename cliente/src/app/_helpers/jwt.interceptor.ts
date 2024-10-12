import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';

import {AuthenticationService} from '../services/authentication.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(private authenticationService: AuthenticationService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let token: String = this.authenticationService.userToken;

    if (token !== '') {
      // Verificar si es una solicitud de tipo 'multipart/form-data'
      if (!(req.body instanceof FormData)) {
        req = req.clone({
          setHeaders: {
            'Content-Type': 'application/json; charset=utf-8',
            Accept: 'application/json',
            Authorization: `Bearer ${token}`,
          },
        });
      } else {
        // No configurar 'Content-Type' para solicitudes FormData
        req = req.clone({
          setHeaders: {
            Accept: 'application/json',
            Authorization: `Bearer ${token}`
          },
        });
      }
    }

    return next.handle(req);
  }

}
