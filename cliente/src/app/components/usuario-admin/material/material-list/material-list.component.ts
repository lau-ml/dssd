import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Material } from '../../../../models/material.dto';
import { PaginatedResponseDTO } from '../../../../models/paginated-response.dto';
import { MaterialesService } from '../../../../services/materiales.service';

@Component({
  selector: 'app-material-list',
  templateUrl: './material-list.component.html',
  styleUrl: './material-list.component.css'
})
export class MaterialListComponent {
  paginatedMaterials: PaginatedResponseDTO<Material> = { content: [], totalPages: 0, page: 0, totalElements: 0, size: 0 };

  pageSize: number = 10;
  searchTerm: string = '';
  selectedMaterialId: number | null = null;

  constructor(private materialesService: MaterialesService, private router: Router, private snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    this.pedirMateriales(0, this.pageSize);
  }
  pedirMateriales(page: number, size: number): void {

    this.materialesService.obtenerMaterialesPaginados(page, size, this.searchTerm).subscribe(
      (data: PaginatedResponseDTO<Material>) => {
        this.paginatedMaterials = data;
      },
      (error) => {
        console.error('Error al obtener materiales:', error);
      }
    );
  }

  buscarMateriales(): void {
    this.pedirMateriales(0, this.pageSize);
  }

  cambiarPagina(nuevaPagina: number): void {
    if (nuevaPagina >= 0 && nuevaPagina < (this.paginatedMaterials?.totalPages || 0)) {
      this.pedirMateriales(nuevaPagina, this.pageSize);
    }
  }

  createMaterial() {
    this.router.navigate([`/material-new`]);
  }

  editMaterial(id: number) {
    this.router.navigate([`/material-edit/${id}`]);
  }

  deleteMaterial(id: number) {
    this.selectedMaterialId = id;
  }

  confirmDelete(): void {

    if (this.selectedMaterialId !== null) {

      this.materialesService.eliminarMaterial(this.selectedMaterialId).subscribe(
        () => {
          this.snackBar.open('✅ Material eliminado.', 'Cerrar', {
            duration: 4000,
            panelClass: ['success-snackbar'],
            verticalPosition: 'top',
            horizontalPosition: 'center'
          });
          this.pedirMateriales(0, this.pageSize);
          this.selectedMaterialId = null;
        },
        (error) => {
          this.snackBar.open('⚠️ Error al eliminar material: ' + error, 'Cerrar', {
            duration: 5000,
            panelClass: ['error-snackbar'],
            verticalPosition: 'top',
            horizontalPosition: 'center'
          });
        }
      );
    }
  }
}
