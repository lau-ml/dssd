import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Material } from '../../../../models/material.dto';
import { MaterialesService } from '../../../../services/materiales.service';

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
      precio: ['', [Validators.required, Validators.min(0)]]
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
          precio: this.material.precio
        });
      },
      error => {
        console.error('Error al obtener el material:', error);
      }
    );
  }

  saveMaterial(): void {
    const precio = this.materialForm.get('precio')?.value;

    if (precio == null || isNaN(precio) || precio < 0) {
      this.snackBar.open('⚠️ El precio debe ser un número válido y no puede ser negativo.', 'Cerrar', {
        duration: 5000,
        panelClass: ['error-snackbar'],
        verticalPosition: 'top',
        horizontalPosition: 'center'
      });
      return;
    }
    if (this.materialForm.valid && this.material) {
      const updatedMaterial: Material = {
        ...this.material,
        precio: this.materialForm.get('precio')?.value
      };

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
          this.snackBar.open('⚠️ Error al actualizar el material: ' + error, 'Cerrar', {
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
}
