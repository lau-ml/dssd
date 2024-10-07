import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SweetalertService} from "../../../../../../dssd/cliente/src/app/services/sweetalert.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthenticationService} from "../../services/authentication.service";
import {PasswordRequest} from "../../_requests/passwordRequest";

@Component({
    selector: 'app-recuperar-contra',
    templateUrl: './recuperar-contra.component.html',
    styleUrl: './recuperar-contra.component.css'
})
export class RecuperarContraComponent implements OnInit {

    private code: string = "";
    recuperarForm! : FormGroup;


    constructor(private formBuilder: FormBuilder,
                private route: ActivatedRoute,
                private router: Router,
                private authService: AuthenticationService,
                private sweetAlertService: SweetalertService) {
    }

  ngOnInit(): void {
    this.recuperarForm = this.formBuilder.group({
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(20), Validators.pattern("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#+_\\-\\/\\\\|:;\\.,])[A-Za-z\\d@$!%*?&#+_\\-\\/\\\\|:;\\.,]{8,}$")]],
      confirmPassword: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(20), Validators.pattern("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#+_\\-\\/\\\\|:;\\.,])[A-Za-z\\d@$!%*?&#+_\\-\\/\\\\|:;\\.,]{8,}$")]],
    })
    this.route.queryParams.subscribe(params => {
      this.code = params['code'];
    });
  }
    get f() {
        return this.recuperarForm.controls;
    }

    onSubmit(): void {
        if (this.recuperarForm.invalid) {
            this.recuperarForm.markAllAsTouched();
            return;
        }
        if (this.recuperarForm.value.password != this.recuperarForm.value.confirmPassword) {
            this.recuperarForm.controls["confirmPassword"].setErrors({notSame: true});
            this.recuperarForm.controls["password"].setErrors({notSame: true});
            this.recuperarForm.markAllAsTouched();
            return;
        }
        let passWordRequest = this.recuperarForm.value as PasswordRequest;
        passWordRequest.code = this.code;
        this.authService.resetPassword(passWordRequest).subscribe(
            {
                next: (response) => {
                    this.sweetAlertService.showAlert('success', '¡Éxito!', 'La contraseña ha sido cambiada.')

                },
                error: (errorData) => {
                    this.sweetAlertService.showAlert('error', '¡Error!', 'No se ha podido modificar la contraseña');
                },
                complete: () => {
                    this.router.navigate(["/login"]);
                }
            });
    }



}
