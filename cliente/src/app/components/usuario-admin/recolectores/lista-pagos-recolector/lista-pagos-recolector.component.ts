import { Component, OnInit } from '@angular/core';
import { PaginatedResponseDTO } from '../../../../models/paginated-response.dto';
import { PagoDTO } from '../../../../models/pago.dto';
import { PagosRecolectorService } from '../../../../services/pagos-recolector.service';
import { ActivatedRoute, Router } from '@angular/router';
import { PaginationComponent } from '../../../pagination/pagination.component';

@Component({
  selector: 'app-lista-pagos-recolector',
  templateUrl: './lista-pagos-recolector.component.html',
  styleUrl: './lista-pagos-recolector.component.css'
})
export class ListaPagosRecolectorComponent implements OnInit {
  pagos: PaginatedResponseDTO<PagoDTO> = { content: [], totalPages: 0, page: 0, totalElements: 0, size: 0 };
  errorMessage: string | null = null;
  pageSize: number = 10;
  estadoFiltro: string = 'TODOS';
  columnaFecha: string = 'fechaEmision';
  fechaDesde: string | null = null;
  fechaHasta: string | null = null;
  ordenColumna: string = 'fechaEmision';
  ordenAscendente: boolean = true;
  recolectorId: string = "0";
  mostrarFiltros: boolean = false;

  links = [
    { label: 'Recolectores', url: '/list-recolectores', icon: 'fas fa-users' },
    { label: `${this.recolectorId}`, url: `/recolector/${this.recolectorId}/administrar`, icon: 'fas fa-user' },
    { label: 'Pagos', url: '' }
  ];

  constructor(
    private pagosRecolectorService: PagosRecolectorService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.recolectorId = this.route.snapshot.paramMap.get('id') || "0";
    this.links = [
      { label: 'Recolectores', url: '/list-recolectores', icon: 'fas fa-users' },
      { label: `${this.recolectorId}`, url: `/recolector/${this.recolectorId}/administrar`, icon: 'fas fa-user' },
      { label: 'Pagos', url: '' }
    ];
    this.cargarPagos(0, this.pageSize, this.estadoFiltro, this.columnaFecha, this.fechaDesde, this.fechaHasta);
  }

  cargarPagos(page: number, size: number, estadoFiltro: string, columnaFecha: string, fechaDesde: string | null, fechaHasta: string | null,): void {
    this.pagosRecolectorService.obtenerPagosRecolector(
      page,
      size,
      estadoFiltro,
      columnaFecha,
      fechaDesde,
      fechaHasta,
      this.ordenColumna,
      this.ordenAscendente,
      this.recolectorId
    ).subscribe(
      (data: PaginatedResponseDTO<PagoDTO>) => {
        this.pagos = data;
      },
      (error) => {
        console.error('Error al cargar los pagos:', error);
        this.errorMessage = 'Hubo un error al cargar los pagos. Intente más tarde.';
      }
    );
  }

  aplicarFiltros(): void {
    this.cargarPagos(
      0,
      this.pageSize,
      this.estadoFiltro,
      this.columnaFecha,
      this.fechaDesde,
      this.fechaHasta,
    );
  }

  borrarFiltros(): void {
    this.estadoFiltro = 'TODOS';
    this.columnaFecha = 'fechaEmision';
    this.fechaDesde = null;
    this.fechaHasta = null;
    this.cargarPagos(0, this.pageSize,
      this.estadoFiltro,
      this.columnaFecha,
      this.fechaDesde,
      this.fechaHasta);
  }

  cambiarPagina(nuevaPagina: number): void {
    if (nuevaPagina >= 0 && nuevaPagina < (this.pagos?.totalPages || 0)) {
      this.cargarPagos(nuevaPagina, this.pageSize, this.estadoFiltro, this.columnaFecha, this.fechaDesde, this.fechaHasta);
    }
  }

  cambiarOrden(columna: string): void {
    if (this.ordenColumna === columna) {
      this.ordenAscendente = !this.ordenAscendente;
    } else {
      this.ordenColumna = columna;
      this.ordenAscendente = true;
    }
    this.cargarPagos(this.pagos.page, this.pageSize, this.estadoFiltro, columna, this.fechaDesde, this.fechaHasta);
  }

  redirigirRegistro(registroRecoleccionId: number): void {
    this.router.navigate([`/recolector/${this.recolectorId}/registro/${registroRecoleccionId}`]);
  }
}