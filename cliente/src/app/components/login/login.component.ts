import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';


import {AuthenticationService} from '../../services/authentication.service';

import {LoginRequest} from "../../_requests/loginRequest";
import {SweetalertService} from "../../../../../../dssd/cliente/src/app/services/sweetalert.service";
import {UsuarioService} from "../../services/usuario.service";

@Component({templateUrl: 'login.component.html', styleUrl: "./login.css"})
export class LoginComponent implements OnInit {
  loading = false;
  submitted = false;
  error = '';
  errorReenviar = '';

  isResendEmail: boolean = false;
  isRecoverPass: boolean = false;
  loginForm!: FormGroup;
  reenviarForm!: FormGroup;
  recoverForm!: FormGroup;


  errorRecuperar: boolean = false;


  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authenticationService: AuthenticationService,
    private sweetAlertService: SweetalertService,
    private userService: UsuarioService
  ) {
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    })

    this.reenviarForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
    })

    this.recoverForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
    })
    this.authenticationService.isResendEmailValue.subscribe(
      {
        next: (isResendEmail) => {
          this.isResendEmail = isResendEmail;
        }
      }
    )
    this.authenticationService.isRecoverPassValue.subscribe(
      {
        next: (isRecoverPass) => {
          this.isRecoverPass = isRecoverPass;
        }
      }
    )
  }


  get f() {
    return this.loginForm.controls;
  }

  get reenviarf() {
    return this.reenviarForm.controls;
  }

  get recoverf() {
    return this.recoverForm.controls;
  }

  onSubmit() {
    this.submitted = true;
    this.loading = true;
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();

    } else {

      this.authenticationService.login(this.loginForm.value as LoginRequest).subscribe({
        next: () => {
          this.router.navigateByUrl('/home').then(r => console.log(r));
          this.loginForm.reset();
        },
        error: (errorData) => {
          this.error = "Usuario o contraseña incorrectos";
        },
        complete: () => {
        }
      })
    }
    this.loading = false;
  }


  onSubmitReenviar() {
    if (this.reenviarForm.invalid) {
      this.reenviarForm.markAllAsTouched();
    } else {
      this.sweetAlertService.showLoadingAlert();

      this.authenticationService.resendEmail(this.reenviarForm.value.email as string).subscribe({
        next: () => this.sweetAlertService.showAlert('success', '¡Éxito!', 'Envío de correo exitoso.'),
        error: (errorData) => {
          this.sweetAlertService.showAlert('error', '¡Error!', 'El usuario ya se encuentra activo o no existe');
          this.errorReenviar = errorData.message;
        },
        complete: () => {
          this.router.navigateByUrl('/login').then(r => console.log(r));
          this.reenviarForm.reset();
        },
      });
    }
  }

  onSubmitRecover() {
    if (this.recoverForm.invalid) {
      this.recoverForm.markAllAsTouched();
    } else {
      this.sweetAlertService.showLoadingAlert();

      this.authenticationService.recoverPassword(this.recoverForm.value.email as string).subscribe({
        next: () => {
          this.sweetAlertService.showAlert('success', '¡Éxito!', 'Envio de correo exitoso.');

          // Navigate to login on success
          this.router.navigateByUrl('/login').then(r => console.log(r));

          // Reset the form
          this.recoverForm.reset();
        },
        error: (errorData) => {

          this.sweetAlertService.showAlert('error', '¡Error!', 'El usuario no se encuentra activo o no existe');
          this.errorRecuperar = errorData.message;
        },
      });
    }
  }
}
