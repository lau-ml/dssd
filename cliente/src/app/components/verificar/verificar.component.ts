import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {AuthenticationService} from "../../services/authentication.service";
import {SweetalertService} from "../../services/sweetalert.service";

@Component({
    selector: 'app-verificar',
    templateUrl: './verificar.component.html',
    styleUrl: './verificar.component.css'
})
export class VerificarComponent implements OnInit {

    private code: string = "";

    constructor(private route: ActivatedRoute,
                private authenticationService: AuthenticationService,
                private sweetAlertService: SweetalertService,
                private router: Router) {

    }

    ngOnInit(): void {
        this.route.queryParams.subscribe(params => {
            this.code = params['code'];
        });
        this.authenticationService.verifyCode(this.code).subscribe({
            next: () => {
                this.sweetAlertService.showAlert("success", "Verificación", "La verificación se realizó con éxito.");
            },
            error: (error) => {
                this.sweetAlertService.showAlert("error", "Verificación", "No se pudo verificar el código.");
                this.router.navigate(["/login"]);
            },
            complete: () => {
                console.log("complete");
                this.router.navigate(["/login"]).then(r => console.log("navegando"));

            }
        });
    }

}

