import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PuntoDeRecoleccionService } from '../../../services/punto-recoleccion.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-punto-create',
  templateUrl: './punto-create.component.html',
  styleUrl: './punto-create.component.css'
})
export class PuntoCreateComponent implements OnInit {
  puntoForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private puntoDeRecoleccionService: PuntoDeRecoleccionService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.puntoForm = this.fb.group({
      nombreEstablecimiento: ['', [Validators.required, Validators.minLength(3)]],
      direccion: ['', [Validators.required, Validators.minLength(5)]],
      numeroContacto: ['', [Validators.required, Validators.pattern(/^[0-9-]+$/)]]
    });
  }

  ngOnInit(): void { }

  savePunto(): void {
    if (this.puntoForm.valid) {
      const newPunto = this.puntoForm.value;
      this.puntoDeRecoleccionService.crearPunto(newPunto).subscribe(
        () => {
          this.snackBar.open('✅ Punto de recolección creado con éxito.', 'Cerrar', {
            duration: 4000,
            panelClass: ['success-snackbar'],
            verticalPosition: 'top',
            horizontalPosition: 'center'
          });
          this.router.navigate(['/lista-puntos-recoleccion']);
        },
        (error) => {
          if (error.codigoError && error.codigoError === "INVALID_DATA") {
            this.snackBar.open('⚠️ Error: ' + error.mensaje, 'Cerrar', {
              duration: 10000,
              panelClass: ['error-snackbar'],
              verticalPosition: 'top',
              horizontalPosition: 'center'
            });
          } else {
            this.snackBar.open('⚠️ Error inesperado al crear el punto de recolección', 'Cerrar', {
              duration: 5000,
              panelClass: ['error-snackbar'],
              verticalPosition: 'top',
              horizontalPosition: 'center'
            });
          }
        }
      );
    }
  }

  cancel(): void {
    this.router.navigate(['/lista-puntos-recoleccion']);
  }
}