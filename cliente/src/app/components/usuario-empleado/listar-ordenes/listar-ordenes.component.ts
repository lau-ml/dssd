import { Component, OnInit } from '@angular/core';
import { OrdenDeDistribucion } from '../../../models/orden-de-distribucion.dto';
import { OrdenDeDistribucionService } from '../../../services/orden-de-distribucion.service';
import { PaginatedResponseDTO } from '../../../models/paginated-response.dto';

@Component({
  selector: 'app-listar-ordenes',
  templateUrl: './listar-ordenes.component.html',
  styleUrl: './listar-ordenes.component.css'
})
export class ListarOrdenesComponent implements OnInit {
  paginatedOrdenes: PaginatedResponseDTO<OrdenDeDistribucion> = { content: [], totalPages: 0, page: 0, totalElements: 0, size: 0 };
  pageSize: number = 10;
  searchTerm: string = '';
  errorMessage: string | null = null;
  estadoFiltro: string = '';
  ordenColumna: string = 'id';
  ordenAscendente: boolean = true;

  constructor(private ordenService: OrdenDeDistribucionService) { }

  ngOnInit(): void {
    this.cargarOrdenes(0, this.pageSize);
  }

  cargarOrdenes(page: number, size: number): void {
    this.ordenService.obtenerOrdenesPaginadas(page, size, this.searchTerm, this.estadoFiltro, this.ordenColumna, this.ordenAscendente).subscribe(
      (data: PaginatedResponseDTO<OrdenDeDistribucion>) => {
        this.paginatedOrdenes = data;
      },
      (error) => {
        console.error('Error al cargar las órdenes:', error);
        this.errorMessage = 'Hubo un error al cargar las órdenes.';
      }
    );
  }

  cambiarOrden(columna: string): void {
    if (this.ordenColumna === columna) {
      this.ordenAscendente = !this.ordenAscendente;
    } else {
      this.ordenColumna = columna;
      this.ordenAscendente = true;
    }
    this.cargarOrdenes(this.paginatedOrdenes.page, this.pageSize);
  }

  filtrarPorEstado(estado: string): void {
    this.estadoFiltro = estado;
    this.cargarOrdenes(0, this.pageSize);
  }

  cambiarPagina(nuevaPagina: number): void {
    if (nuevaPagina >= 0 && nuevaPagina < (this.paginatedOrdenes?.totalPages || 0)) {
      this.cargarOrdenes(nuevaPagina, this.pageSize);
    }
  }

  getEstadoTexto(estado: string): string {
    switch (estado) {
      case 'PENDIENTE_DE_ACEPTAR': return 'Pendiente de Aceptar';
      case 'ACEPTADA': return 'Aceptada';
      case 'RECHAZADO': return 'Rechazado';
      case 'EN_PREPARACION': return 'En preparación';
      case 'PREPARADO': return 'Preparado';
      case 'ENVIADO': return 'Enviado';
      case "ENTREGADA": return 'Entregada';
      default: return 'Desconocido';
    }
  }

  cambiarEstado(id: number, nuevoEstado: string): void {
    this.ordenService.cambiarEstado(id, nuevoEstado).subscribe(
      () => {
        this.cargarOrdenes(this.paginatedOrdenes.page, this.pageSize);
      },
      (error) => {
        console.error(`Error al cambiar el estado de la orden ${id}:`, error);
        this.errorMessage = 'Hubo un error al cambiar el estado de la orden.';
      }
    );
  }
}
