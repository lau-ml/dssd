import { Component, OnInit } from '@angular/core';
import { PaginatedResponseDTO } from '../../../../models/paginated-response.dto';
import { RecolectorDTO } from '../../../../models/recolector.dto';
import { PuntoDeRecoleccionService } from '../../../../services/punto-recoleccion.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-recolectores-punto',
  templateUrl: './recolectores-punto.component.html',
  styleUrl: './recolectores-punto.component.css'
})
export class RecolectoresPuntoComponent implements OnInit {
  recolectores: PaginatedResponseDTO<RecolectorDTO> = { content: [], totalPages: 0, page: 0, totalElements: 0, size: 0 };
  errorMessage: string | null = null;
  pageSize: number = 10;
  searchTerm: string = '';
  selectedRecolectorId: number | null = null;
  puntoId: number = 0;

  links = [
    { label: 'Puntos de Recolección', url: '/lista-puntos-recoleccion', icon: 'fa-solid fa-map-marker-alt' },
    { label: `Punto ${this.puntoId}`, url: `/puntos-recoleccion/${this.puntoId}/recolectores`, icon: 'fa-solid fa-map-marker-alt' },
    { label: 'Recolectores', url: '' }
  ];


  constructor(
    private puntoDeRecoleccionService: PuntoDeRecoleccionService,
    private snackBar: MatSnackBar,
    private router: Router,
    private route: ActivatedRoute,
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.puntoId = params['id'];
      if (this.puntoId) {
        this.cargarRecolectores(0, this.pageSize, this.puntoId);
        this.links = [
          { label: 'Puntos de Recolección', url: '/lista-puntos-recoleccion', icon: 'fa-solid fa-map-marker-alt' },
          { label: `Punto ${this.puntoId}`, url: `/puntos-recoleccion/${this.puntoId}/recolectores`, icon: 'fa-solid fa-map-marker-alt' },
          { label: 'Recolectores', url: '' }
        ];
      } else {
        this.errorMessage = 'No se proporcionó un ID de punto de recolección.';
      }
    });
  }

  cargarRecolectores(page: number, size: number, puntoId: number): void {
    this.puntoDeRecoleccionService.obtenerRecolectoresPorPunto(puntoId, page, size, this.searchTerm).subscribe(
      (data: PaginatedResponseDTO<RecolectorDTO>) => {
        this.recolectores = data;
      },
      (error) => {
        if (error.codigoError && error.codigoError === "INVALID_DATA") {
          this.router.navigate(['/asociar-recolector']);
        }
        console.error('Error al cargar los recolectores:', error);
        this.errorMessage = 'Hubo un error al cargar los recolectores. Intente más tarde.';
      }
    );
  }

  buscarRecolectores() {
    this.cargarRecolectores(0, this.pageSize, this.puntoId);
  }

  asociarNuevoRecolector() {
    this.router.navigate([`/puntos-recoleccion/${this.puntoId}/recolectores-no-asociados`]);
  }

  desvincularRecolector(id: number) {
    this.selectedRecolectorId = id;
  }

  confirmDesvincular(): void {
    if (this.selectedRecolectorId !== null) {
      this.puntoDeRecoleccionService.desvincularRecolectorDePunto(this.puntoId, this.selectedRecolectorId).subscribe(
        () => {
          this.snackBar.open('✅ Recolector desvinculado.', 'Cerrar', {
            duration: 4000,
            panelClass: ['success-snackbar'],
            verticalPosition: 'top',
            horizontalPosition: 'center'
          });
          this.selectedRecolectorId = null;
          this.cargarRecolectores(0, this.pageSize, this.puntoId);
        },
        (error) => {
          this.snackBar.open('⚠️ Error al desvincular el recolector: ' + error, 'Cerrar', {
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
    if (nuevaPagina >= 0 && nuevaPagina < (this.recolectores?.totalPages || 0)) {
      this.cargarRecolectores(this.puntoId, nuevaPagina, this.pageSize);
    }
  }
}