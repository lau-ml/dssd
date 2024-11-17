import { Component, OnInit } from '@angular/core';
import { PaginatedResponseDTO } from '../../../models/paginated-response.dto';
import { PuntoDeRecoleccion } from '../../../models/punto-recoleccion.dto';
import { PuntoDeRecoleccionService } from '../../../services/punto-recoleccion.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-solicitud-punto-recoleccion',
  templateUrl: './solicitud-punto-recoleccion.component.html',
  styleUrl: './solicitud-punto-recoleccion.component.css'
})
export class SolicitudPuntoRecoleccionComponent implements OnInit {
  puntosDeRecoleccion: PaginatedResponseDTO<PuntoDeRecoleccion> | null = null;
  errorMessage: string | null = null;
  pageSize: number = 10;
  searchTerm: string = '';
  selectedPuntoId: number | null = null;

  constructor(private puntoDeRecoleccionService: PuntoDeRecoleccionService, private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.cargarPuntosDeRecoleccion(0, this.pageSize);
  }

  cargarPuntosDeRecoleccion(page: number, size: number): void {
    this.puntoDeRecoleccionService.obtenerPuntosDeRecoleccionNoVinculadosPaginados(page, size, this.searchTerm).subscribe(
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

  solicitarVinculacion(puntoId: number): void {
    this.puntoDeRecoleccionService.crearSolicitudVinculacion(puntoId).subscribe(
      () => {
        this.snackBar.open('✅ Solicitud de vinculación realizada con éxito.', 'Cerrar', {
          duration: 4000,
          panelClass: ['success-snackbar'],
          verticalPosition: 'top',
          horizontalPosition: 'center'
        });
        this.cargarPuntosDeRecoleccion(0, this.pageSize);
      },
      (error) => {
        if (error.codigoError && (error.codigoError === "REQUEST_ALREADY_EXISTS" || error.codigoError === "INVALID_DATA")) {
          this.snackBar.open('⚠️ Error: ' + error.mensaje, 'Cerrar', {
            duration: 10000,
            panelClass: ['error-snackbar'],
            verticalPosition: 'top',
            horizontalPosition: 'center'
          });
        } else {
          console.error('Error al solicitar la vinculación:', error);
        }
      }
    );
  }

  cambiarPagina(nuevaPagina: number): void {
    if (nuevaPagina >= 0 && nuevaPagina < (this.puntosDeRecoleccion?.totalPages || 0)) {
      this.cargarPuntosDeRecoleccion(nuevaPagina, this.pageSize);
    }
  }
}
