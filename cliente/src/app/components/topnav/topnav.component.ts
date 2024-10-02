import {Component, OnInit} from '@angular/core';

import {Router} from "@angular/router";
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
  ngOnInit(): void {
  }
  /*
  userLoginOn: boolean = false;
  user: UserResponse = {} as UserResponse;
  solicitudesPendientes: SolicitudGrupoResponse[] = [];
  itemsPerPage: number = 0;
  totalItems: number = 0;
  totalPages: number = 0;
  currentPage: number = 0;
  currentUrl = this.router.url;
  constructor(private loginService: AuthenticationService, private router: Router,
              private usuarioService: UsuarioService,
              private grupoService: GrupoService,
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
            })
            this.obtenerSolicitudes().subscribe();
          }
        }
      }
    )

  }

  obtenerSolicitudes() {
    return this.grupoService.getSolicitudesPendientes().pipe(
      tap((data) => {
        this.solicitudesPendientes = data.solicitudes;
        this.itemsPerPage = data.itemsPerPage;
        this.totalItems = data.totalItems;
        this.totalPages = data.totalPages;
        this.currentPage = data.currentPage;
      })
    );
  }

  aceptarSolicitud(id: number) {
    this.grupoService.aceptarSolicitud(id).pipe(
      concatMap(() => this.obtenerSolicitudes())
    ).subscribe(
      () => {
        this.sweetAlertService.showAlert('success', 'Solicitud aceptada', 'La solicitud ha sido aceptada');
      },
      (error) => {
        console.error('Error al aceptar la solicitud:', error);
      }
    );
  }
  rechazarSolicitud(id: number) {
    this.grupoService.rechazarSolicitud(id).pipe(
      concatMap(() => this.obtenerSolicitudes())
    ).subscribe
    (() => {
      this.sweetAlertService.showAlert('success', 'Solicitud rechazada', 'La solicitud ha sido rechazada');
    });
  }
  logout() {
    this.loginService.logout();
    this.router.navigateByUrl("/").then(r => console.log(r));
  }
  */

}
