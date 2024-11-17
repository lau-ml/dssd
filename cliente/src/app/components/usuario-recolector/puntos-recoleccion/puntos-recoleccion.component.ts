import { Component, OnInit } from '@angular/core';
import { PuntoDeRecoleccionService } from '../../../services/punto-recoleccion.service';
import { PuntoDeRecoleccion } from '../../../models/punto-recoleccion.dto';
import { PaginatedResponseDTO } from '../../../models/paginated-response.dto';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';

@Component({
  selector: 'app-puntos-recoleccion',
  templateUrl: './puntos-recoleccion.component.html',
  styleUrl: './puntos-recoleccion.component.css'
})
export class PuntosRecoleccionComponent implements OnInit {
  puntosDeRecoleccion: PaginatedResponseDTO<PuntoDeRecoleccion> | null = null;
  errorMessage: string | null = null;
  pageSize: number = 10;
  searchTerm: string = '';
  selectedPuntoId: number | null = null;

  constructor(private puntoDeRecoleccionService: PuntoDeRecoleccionService, private snackBar: MatSnackBar, private router: Router) { }

  ngOnInit(): void {
    this.cargarPuntosDeRecoleccion(0, this.pageSize);
  }

  cargarPuntosDeRecoleccion(page: number, size: number): void {
    this.puntoDeRecoleccionService.obtenerMisPuntosDeRecoleccionPaginados(page, size, this.searchTerm).subscribe(
      (data: PaginatedResponseDTO<PuntoDeRecoleccion>) => {
        this.puntosDeRecoleccion = data;
      },
      (error) => {
        console.error('Error al cargar los puntos de recolección:', error);
        this.errorMessage = 'Hubo un error al cargar los puntos de recolección. Intente más tarde.';
      }
    );
  }

  buscarPuntosRecoleccion() {
    this.cargarPuntosDeRecoleccion(0, this.pageSize);
  }

  createPuntoRecoleccion() {
    this.router.navigate(['/solicitar-puntos-recoleccion']);
  }

  deletePunto(id: number) {
    this.selectedPuntoId = id;
  }

  confirmDelete(): void {
    if (this.selectedPuntoId !== null) {
      this.puntoDeRecoleccionService.desvincularPuntoDeRecoleccion(this.selectedPuntoId).subscribe(
        () => {
          this.snackBar.open('✅ Punto de recolección desvinculado.', 'Cerrar', {
            duration: 4000,
            panelClass: ['success-snackbar'],
            verticalPosition: 'top',
            horizontalPosition: 'center'
          });
          this.cargarPuntosDeRecoleccion(0, this.pageSize);
          this.selectedPuntoId = null;
        },
        (error) => {
          this.snackBar.open('⚠️ Error al desvincular el punto de recolección: ' + error, 'Cerrar', {
            duration: 5000,
            panelClass: ['error-snackbar'],
            verticalPosition: 'top',
            horizontalPosition: 'center'
          });
        }
      );
    }
  }

  cambiarPagina(nuevaPagina: number): void {
    if (nuevaPagina >= 0 && nuevaPagina < (this.puntosDeRecoleccion?.totalPages || 0)) {
      this.cargarPuntosDeRecoleccion(nuevaPagina, this.pageSize);
    }
  }
}