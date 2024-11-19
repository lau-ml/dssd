import { Component } from '@angular/core';
import { PaginatedResponseDTO } from '../../../models/paginated-response.dto';
import { PuntoDeRecoleccion } from '../../../models/punto-recoleccion.dto';
import { PuntoDeRecoleccionService } from '../../../services/punto-recoleccion.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-list-puntos-recoleccion',
  templateUrl: './list-puntos-recoleccion.component.html',
  styleUrl: './list-puntos-recoleccion.component.css'
})
export class ListPuntosRecoleccionComponent {
  paginatedPuntos: PaginatedResponseDTO<PuntoDeRecoleccion> = { content: [], totalPages: 0, page: 0, totalElements: 0, size: 0 };
  pageSize: number = 10;
  searchTerm: string = '';
  selectedPuntoId: number | null = null;

  constructor(
    private puntosService: PuntoDeRecoleccionService,
    private router: Router,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.pedirPuntos(0, this.pageSize);
  }

  pedirPuntos(page: number, size: number): void {
    this.puntosService.obtenerPuntosDeRecoleccionPaginados(page, size, this.searchTerm).subscribe(
      (data: PaginatedResponseDTO<PuntoDeRecoleccion>) => {
        this.paginatedPuntos = data;
      },
      (error) => {
        console.error('Error al obtener puntos de recolección:', error);
      }
    );
  }

  buscarPuntos(): void {
    this.pedirPuntos(0, this.pageSize);
  }

  cambiarPagina(nuevaPagina: number): void {
    if (nuevaPagina >= 0 && nuevaPagina < (this.paginatedPuntos?.totalPages || 0)) {
      this.pedirPuntos(nuevaPagina, this.pageSize);
    }
  }

  createPunto() {
    this.router.navigate([`/puntos-recoleccion/create`]);
  }

  editPunto(id: number) {
    this.router.navigate([`/puntos-recoleccion/edit/${id}`]);
  }

  deletePunto(id: number) {
    this.selectedPuntoId = id;
  }

  confirmDelete(): void {
    if (this.selectedPuntoId !== null) {
      this.puntosService.deletePunto(this.selectedPuntoId).subscribe(
        () => {
          this.snackBar.open('✅ Punto de recolección eliminado.', 'Cerrar', {
            duration: 4000,
            panelClass: ['success-snackbar'],
            verticalPosition: 'top',
            horizontalPosition: 'center'
          });
          this.pedirPuntos(0, this.pageSize);
          this.selectedPuntoId = null;
        },
        (error) => {
          this.snackBar.open('⚠️ Error al eliminar punto: ' + error, 'Cerrar', {
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
