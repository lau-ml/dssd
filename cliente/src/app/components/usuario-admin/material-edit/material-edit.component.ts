import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { MaterialesService } from '../../../services/materiales.service';
import { Material } from '../../../models/material.dto';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-material-edit',
  templateUrl: './material-edit.component.html',
  styleUrls: ['./material-edit.component.css']
})
export class MaterialEditComponent implements OnInit {
  materialForm: FormGroup;
  material: Material | null = null;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private materialesService: MaterialesService,
    private snackBar: MatSnackBar
  ) {
    this.materialForm = this.fb.group({
      nombre: ['', [Validators.required, this.nombreSinCaracteresEspeciales()]],
      descripcion: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    const materialId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadMaterial(materialId);
  }

  loadMaterial(id: number): void {
    this.materialesService.obtenerMaterialPorId(id).subscribe(
      (material: Material) => {
        this.material = material;
        this.materialForm.patchValue({
          nombre: this.material.nombre,
          descripcion: this.material.descripcion
        });
      },
      error => {
        console.error('Error al obtener el material:', error);
      }
    );
  }

  saveMaterial(): void {
    if (this.materialForm.valid && this.material) {
      const updatedMaterial: Material = { ...this.material, ...this.materialForm.value };
      this.materialesService.editarMaterial(this.material.id, updatedMaterial).subscribe(
        () => {
          this.snackBar.open('✅ Material actualizado.', 'Cerrar', {
            duration: 4000,
            panelClass: ['success-snackbar'],
            verticalPosition: 'top',
            horizontalPosition: 'center'
          });
          this.router.navigate(['/lista-materiales']);
        },
        error => {
          this.snackBar.open('⚠️ Error al actualizar el material:' + error, 'Cerrar', {
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
    this.router.navigate(['/material-list']);
  }

  private nombreSinCaracteresEspeciales(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const forbidden = /[!@#$%^&*(),.?":{}|<>]/.test(control.value);
      return forbidden ? { 'caracteresEspeciales': { value: control.value } } : null;
    };
  }
}
