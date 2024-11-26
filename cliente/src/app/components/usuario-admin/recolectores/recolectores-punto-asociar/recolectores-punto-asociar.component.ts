import { Component, OnInit } from '@angular/core';
import { PaginatedResponseDTO } from '../../../../models/paginated-response.dto';
import { PuntoDeRecoleccion } from '../../../../models/punto-recoleccion.dto';
import { ActivatedRoute } from '@angular/router';
import { PuntoDeRecoleccionService } from '../../../../services/punto-recoleccion.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-recolectores-punto-asociar',
  templateUrl: './recolectores-punto-asociar.component.html',
  styleUrl: './recolectores-punto-asociar.component.css'
})
export class RecolectoresPuntoAsociarComponent implements OnInit {
  puntosDeRecoleccion: PaginatedResponseDTO<PuntoDeRecoleccion> = { content: [], totalPages: 0, page: 0, totalElements: 0, size: 0 };
  recolectorId: string = "0";
  searchTerm: string = '';
  ordenColumna: string = 'nombreEstablecimiento';
  ordenAscendente: boolean = true;

  links = [
    { label: 'Recolectores', url: '/list-recolectores', icon: 'fas fa-users' },
    { label: `${this.recolectorId}`, url: `/recolector/${this.recolectorId}/administrar`, icon: 'fas fa-user' },
    { label: `Puntos`, url: `/recolector/${this.recolectorId}/puntos-de-recoleccion`, icon: 'fas fa-user' },
    { label: 'Puntos', url: '' }
  ];

  constructor(
    private route: ActivatedRoute,
    private puntoDeRecoleccionService: PuntoDeRecoleccionService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.recolectorId = this.route.snapshot.paramMap.get('id') || "0";
    this.links = [
      { label: 'Recolectores', url: '/list-recolectores', icon: 'fas fa-users' },
      { label: `${this.recolectorId}`, url: `/recolector/${this.recolectorId}/administrar`, icon: 'fas fa-user' },
      { label: `Puntos`, url: `/recolector/${this.recolectorId}/puntos-de-recoleccion`, icon: 'fas fa-map-marker-alt' },
      { label: 'Puntos', url: '' }
    ];
    this.cargarPuntosDeRecoleccion(0, 10);
  }

  cargarPuntosDeRecoleccion(page: number, size: number): void {
    this.puntoDeRecoleccionService.obtenerPuntosDeRecoleccionNoVinculadosRecolector(
      this.recolectorId,
      page,
      size,
      this.searchTerm,
      this.ordenColumna,
      this.ordenAscendente
    ).subscribe(
      (data: PaginatedResponseDTO<PuntoDeRecoleccion>) => {
        this.puntosDeRecoleccion = data;
      },
      (error) => console.error('Error al cargar los puntos de recolección:', error)
    );
  }

  vincularPuntoRecoleccion(puntoId: number): void {
    if (this.recolectorId) {
      this.puntoDeRecoleccionService.vincularRecolectorAPunto(puntoId, Number(this.recolectorId)).subscribe(
        () => {
          this.snackBar.open('✅ Punto vinculado con éxito.', 'Cerrar', {
            duration: 4000,
            panelClass: ['success-snackbar']
          });
          this.cargarPuntosDeRecoleccion(0, 10);
        },
        (error) => console.error('Error al vincular el punto:', error)
      );
    }
  }

  buscarPuntosRecoleccion(): void {
    this.cargarPuntosDeRecoleccion(0, 10);
  }

  cambiarPagina(page: number): void {
    this.cargarPuntosDeRecoleccion(page, 10);
  }

  cambiarOrden(columna: string): void {
    this.ordenColumna = columna;
    this.ordenAscendente = !this.ordenAscendente;
    this.cargarPuntosDeRecoleccion(this.puntosDeRecoleccion.page, 10);
  }
}