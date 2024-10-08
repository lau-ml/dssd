import {Component, OnInit} from '@angular/core';

import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/authentication.service";
import {UsuarioService} from "../../services/usuario.service";
import {SweetalertService} from "../../services/sweetalert.service";
import {UserResponse} from "../../_responses/userResponse";
/*
import {AuthenticationService} from "../_services";
import {concatMap} from "rxjs";
import {tap} from "rxjs/operators";
import {SweetalertService} from "../_services/sweetalert.service";
*/


@Component({
  selector: 'app-topnav',
  templateUrl: './topnav.component.html',
  styleUrl: './topnav.component.css'
})
export class TopnavComponent implements OnInit {


  userLoginOn: boolean = false;
  user: UserResponse = {} as UserResponse;
  constructor(private loginService: AuthenticationService, private router: Router,

              private usuarioService: UsuarioService,
              private sweetAlertService: SweetalertService) {
  }

  ngOnInit(): void {
    this.loginService.isLoggedValue.subscribe(
      {
        next: (userLoginOn) => {
          this.userLoginOn = userLoginOn;
          if (userLoginOn) {
            this.usuarioService.getUsuario().subscribe({
              next: (data) => {
                this.user = data as UserResponse;
              }
            })}
        }
      }
    )

  }


  logout() {
    this.loginService.logout();
    this.router.navigateByUrl("/").then(r => console.log(r));
  }


}
