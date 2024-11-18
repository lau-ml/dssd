import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { MaterialesService } from '../../../services/materiales.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-material-create',
  templateUrl: './material-create.component.html',
  styleUrls: ['./material-create.component.css']
})
export class MaterialCreateComponent implements OnInit {
  materialForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private materialesService: MaterialesService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.materialForm = this.fb.group({
      nombre: ['', [Validators.required, this.nombreSinCaracteresEspeciales()]],
      descripcion: ['', Validators.required]
    });
  }

  ngOnInit(): void { }

  saveMaterial(): void {
    if (this.materialForm.valid) {
      const newMaterial = this.materialForm.value;
      this.materialesService.crearMaterial(newMaterial).subscribe(
        () => {
          this.snackBar.open('✅ Material creado con éxito.', 'Cerrar', {
            duration: 4000,
            panelClass: ['success-snackbar'],
            verticalPosition: 'top',
            horizontalPosition: 'center'
          });
          this.router.navigate(['/lista-materiales']);
        },
        (error) => {
          this.snackBar.open('⚠️ Error al crear el material:' + error, 'Cerrar', {
            duration: 5000,
            panelClass: ['error-snackbar'],
            verticalPosition: 'top',
            horizontalPosition: 'center'
          });
        }
      );
    }
  }

  cancel(): void {
    this.router.navigate(['/lista-materiales']);
  }

  private nombreSinCaracteresEspeciales(): ValidatorFn {
    return (control): { [key: string]: any } | null => {
      const forbidden = /[!@#$%^&*(),.?":{}|<>]/.test(control.value);
      return forbidden ? { 'caracteresEspeciales': { value: control.value } } : null;
    };
  }
}
