import { Component, Input, OnInit } from '@angular/core';
import { PuntoDeRecoleccion } from '../../../../models/punto-recoleccion.dto';
import { PaginatedResponseDTO } from '../../../../models/paginated-response.dto';
import { PuntoDeRecoleccionService } from '../../../../services/punto-recoleccion.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-lista-puntos-recoleccion',
  templateUrl: './lista-puntos-recoleccion.component.html',
  styleUrl: './lista-puntos-recoleccion.component.css'
})
export class ListaPuntosRecoleccionComponent implements OnInit {

  puntosDeRecoleccion: PaginatedResponseDTO<PuntoDeRecoleccion> = {
    content: [],
    totalPages: 0,
    page: 0,
    totalElements: 0,
    size: 0
  };

  searchTerm: string = '';
  ordenColumna: string = 'nombreEstablecimiento';
  ordenAscendente: boolean = true;
  pageSize: number = 10;
  recolectorId: string = "0";
  selectedPuntoId: number | null = null;

  links = [
    { label: 'Recolectores', url: '/list-recolectores', icon: 'fas fa-users' },
    { label: `${this.recolectorId}`, url: `/recolector/${this.recolectorId}/administrar`, icon: 'fas fa-user' },
    { label: 'Puntos', url: '' }
  ];

  constructor(
    private puntoDeRecoleccionService: PuntoDeRecoleccionService,
    private snackBar: MatSnackBar,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.recolectorId = this.route.snapshot.paramMap.get('id') || "0";
    this.links = [
      { label: 'Recolectores', url: '/list-recolectores', icon: 'fas fa-users' },
      { label: `${this.recolectorId}`, url: `/recolector/${this.recolectorId}/administrar`, icon: 'fas fa-user' },
      { label: 'Puntos', url: '' }
    ];
    this.cargarPuntosDeRecoleccion(0, this.pageSize);
  }

  cargarPuntosDeRecoleccion(page: number, size: number): void {
    this.puntoDeRecoleccionService
      .obtenerPuntosDeRecoleccionPorUsuario(
        this.recolectorId,
        page,
        size,
        this.searchTerm,
        this.ordenColumna,
        this.ordenAscendente
      )
      .subscribe(
        (data) => {
          this.puntosDeRecoleccion = data;
        },
        (error) => {
          console.error('Error al cargar los puntos de recolección:', error);
          this.snackBar.open(
            '⚠️ Hubo un error al cargar los puntos de recolección. Intente más tarde.',
            'Cerrar',
            { duration: 5000 }
          );
        }
      );
  }

  buscarPuntosRecoleccion(): void {
    this.cargarPuntosDeRecoleccion(0, this.pageSize);
  }

  cambiarPagina(nuevaPagina: number): void {
    this.cargarPuntosDeRecoleccion(nuevaPagina, this.pageSize);
  }

  cambiarOrden(columna: string): void {
    if (this.ordenColumna === columna) {
      this.ordenAscendente = !this.ordenAscendente;
    } else {
      this.ordenColumna = columna;
      this.ordenAscendente = true;
    }
    this.cargarPuntosDeRecoleccion(this.puntosDeRecoleccion.page, this.pageSize);
  }

  deletePunto(id: number) {
    this.selectedPuntoId = id;
  }

  asociarPuntoRecoleccion() {
    this.router.navigate(['/solicitar-puntos-recoleccion']);
  }

  confirmDelete(): void {
    if (this.selectedPuntoId !== null) {
      this.puntoDeRecoleccionService.desvincularPuntoDeRecolector(this.recolectorId, this.selectedPuntoId).subscribe(
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
}