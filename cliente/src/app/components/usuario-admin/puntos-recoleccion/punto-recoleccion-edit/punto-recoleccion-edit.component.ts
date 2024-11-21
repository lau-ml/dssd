import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { PuntoDeRecoleccionService } from '../../../../services/punto-recoleccion.service';
import { PuntoDeRecoleccion } from '../../../../models/punto-recoleccion.dto';

@Component({
  selector: 'app-punto-recoleccion-edit',
  templateUrl: './punto-recoleccion-edit.component.html',
  styleUrl: './punto-recoleccion-edit.component.css'
})
export class PuntoRecoleccionEditComponent implements OnInit {
  puntoForm: FormGroup;
  punto: PuntoDeRecoleccion | null = null;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private puntosRecoleccionService: PuntoDeRecoleccionService,
    private snackBar: MatSnackBar
  ) {
    this.puntoForm = this.fb.group({
      nombreEstablecimiento: ['', [Validators.required, Validators.minLength(3)]],
      direccion: ['', [Validators.required, Validators.minLength(5)]],
      numeroContacto: ['', [Validators.required, Validators.pattern(/^[0-9-]+$/)]]
    });
  }

  ngOnInit(): void {
    const puntoId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadPunto(puntoId);
  }

  loadPunto(id: number): void {
    this.puntosRecoleccionService.obtenerPuntoPorId(id).subscribe(
      (punto: PuntoDeRecoleccion) => {
        this.punto = punto;
        this.puntoForm.patchValue({
          nombreEstablecimiento: this.punto.nombreEstablecimiento,
          direccion: this.punto.direccion,
          numeroContacto: this.punto.numeroContacto
        });
      },
      error => {
        console.error('Error al obtener el punto de recolección:', error);
      }
    );
  }

  savePunto(): void {
    if (this.puntoForm.valid && this.punto) {
      const updatedPunto: PuntoDeRecoleccion = { ...this.punto, ...this.puntoForm.value };
      this.puntosRecoleccionService.editarPunto(this.punto.id, updatedPunto).subscribe(
        () => {
          this.snackBar.open('✅ Punto de recolección actualizado.', 'Cerrar', {
            duration: 4000,
            panelClass: ['success-snackbar'],
            verticalPosition: 'top',
            horizontalPosition: 'center'
          });
          this.router.navigate(['/lista-puntos-recoleccion']);
        },
        error => {
          if (error.codigoError && error.codigoError === "INVALID_DATA") {
            this.snackBar.open('⚠️ Error: ' + error.mensaje, 'Cerrar', {
              duration: 10000,
              panelClass: ['error-snackbar'],
              verticalPosition: 'top',
              horizontalPosition: 'center'
            });
          } else {
            this.snackBar.open('⚠️ Ocurrio un error ', 'Cerrar', {
              duration: 10000,
              panelClass: ['error-snackbar'],
              verticalPosition: 'top',
              horizontalPosition: 'center'
            });
          }
          console.error("Error al actualizar el punto de recolección: ", error)
        }
      );
    }
  }

  cancel(): void {
    this.router.navigate(['/lista-puntos-recoleccion']);
  }
}