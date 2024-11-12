import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {RegisterService} from "../../services/register.service";
import {RegisterRequest} from "../../_requests/registerRequest";
import {SweetalertService} from "../../services/sweetalert.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit{
  submitted = false;
  errorMail = false;
  errorUser = false;
  registerForm!: FormGroup;
  errorDNI: boolean = false;


  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private registerService: RegisterService,
              private sweetAlertService: SweetalertService
  ) {
  }

  get f() {
    return this.registerForm.controls;
  }

  onSubmit(): void {
    this.submitted = true;
    this.errorMail = false;
    this.errorUser = false;
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
    } else {
      this.sweetAlertService.showLoadingAlert();
      this.registerService.register(this.registerForm.value as RegisterRequest).subscribe(
        {
          next: (response) => {
            this.sweetAlertService.showAlert('success', '¡Éxito!', 'La cuenta ha sido registrada. ' +
              'Recuerde que debe verificar su cuenta siguiendo el enlace enviado a su correo.')
            this.router.navigate(['/login']);
          },
          error: (errorData) => {
            if (errorData.message.includes("mail")) {
              this.errorMail = true;
            }
            if (errorData.message.includes("usuario")) {
              this.errorUser = true;
            }
            if (errorData.message.includes("dni")) {
              this.errorDNI = true;
            }
            this.sweetAlertService.showAlert('error', '¡Error!', 'Los datos ingresados se encuentran en uso.');
          }
        }
      )
    }
  }

  ngOnInit(): void {
    this.registerForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(100)]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(20), Validators.pattern("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#+_\\-\\/\\\\|:;\\.,])[A-Za-z\\d@$!%*?&#+_\\-\\/\\\\|:;\\.,]{8,}$")]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(150)]],
      dni: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(10), Validators.pattern("^[0-9]*$")]],
      firstName: ['', [Validators.required, Validators.maxLength(50)]],
      lastName: ['', [Validators.required, Validators.maxLength(50)]],
      confirmPassword: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(20), Validators.pattern("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#+_\\-\\/\\\\|:;\\.,])[A-Za-z\\d@$!%*?&#+_\\-\\/\\\\|:;\\.,]{8,}$")]],
    })
  }
}
