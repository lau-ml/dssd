import { Component, OnInit } from '@angular/core';
import { PaginatedResponseDTO } from '../../../../models/paginated-response.dto';
import { RecolectorDTO } from '../../../../models/recolector.dto';
import { PuntoDeRecoleccionService } from '../../../../services/punto-recoleccion.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { catchError } from 'rxjs';

@Component({
  selector: 'app-recolectores-no-asociados-punto',
  templateUrl: './recolectores-no-asociados-punto.component.html',
  styleUrl: './recolectores-no-asociados-punto.component.css'
})
export class RecolectoresNoAsociadosPuntoComponent implements OnInit {
  recolectoresNoAsociados: PaginatedResponseDTO<RecolectorDTO> = { content: [], totalPages: 0, page: 0, totalElements: 0, size: 0 };
  errorMessage: string | null = null;
  pageSize: number = 10;
  searchTerm: string = '';
  puntoId: number = 0;

  links = [
    { label: 'Puntos de Recolección', url: '/lista-puntos-recoleccion', icon: 'fa-solid fa-map-marker-alt' },
    { label: `Punto ${this.puntoId}`, url: `/puntos-recoleccion/${this.puntoId}/recolectores`, icon: 'fa-solid fa-map-marker-alt' },
    { label: 'Recolectores', url: '' }
  ];

  constructor(
    private puntoDeRecoleccionService: PuntoDeRecoleccionService,
    private snackBar: MatSnackBar,
    private route: ActivatedRoute,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.puntoId = params['id'];
      if (this.puntoId) {
        this.cargarRecolectoresNoAsociados(0, this.pageSize);
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

  cargarRecolectoresNoAsociados(page: number, size: number): void {
    this.puntoDeRecoleccionService.obtenerRecolectoresNoAsociados(this.puntoId, page, size, this.searchTerm).subscribe(
      (data: PaginatedResponseDTO<RecolectorDTO>) => {
        this.recolectoresNoAsociados = data;
      },
      (error) => {
        console.error('Error al cargar los recolectores no asociados:', error);
        this.errorMessage = 'Hubo un error al cargar los recolectores. Intente más tarde.';
      }
    );
  }
  volverARecolectoresAsociados(): void {
    this.router.navigate([`/puntos-recoleccion/${this.puntoId}/recolectores`]);
  }

  buscarRecolectoresNoAsociados(): void {
    this.cargarRecolectoresNoAsociados(0, this.pageSize);
  }

  asociarRecolector(id: number): void {
    this.puntoDeRecoleccionService.vincularRecolectorAPunto(this.puntoId, id).subscribe(
      () => {
        this.snackBar.open('✅ Recolector asociado con éxito.', 'Cerrar', {
          duration: 4000,
          panelClass: ['success-snackbar'],
          verticalPosition: 'top',
          horizontalPosition: 'center'
        });
        this.cargarRecolectoresNoAsociados(0, this.pageSize);
      },
      (error) => {
        console.error('Error al asociar recolector:', error);
        this.snackBar.open('⚠️ Error al asociar recolector.', 'Cerrar', {
          duration: 5000,
          panelClass: ['error-snackbar'],
          verticalPosition: 'top',
          horizontalPosition: 'center'
        });
      }
    );
  }

  cambiarPagina(nuevaPagina: number): void {
    if (nuevaPagina >= 0 && nuevaPagina < (this.recolectoresNoAsociados?.totalPages || 0)) {
      this.cargarRecolectoresNoAsociados(nuevaPagina, this.pageSize);
    }
  }
}