import {Injectable} from '@angular/core';
import {Router, ActivatedRouteSnapshot, RouterStateSnapshot} from '@angular/router';

import {AuthenticationService} from '../services/authentication.service';
import {map, Observable, take} from 'rxjs';

@Injectable({providedIn: 'root'})
export class AuthGuard {
  constructor(
    private router: Router,
    private authenticationService: AuthenticationService
  ) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | boolean {
    return this.authenticationService.currentUserValue.pipe(
      map(currentUser => {
        if (currentUser) {
          return true;
        } else {
          this.router.navigate(['/login'], {queryParams: {returnUrl: state.url}}).then(r => console.log(r));
          return false;
        }
      })
    );
  }
}
