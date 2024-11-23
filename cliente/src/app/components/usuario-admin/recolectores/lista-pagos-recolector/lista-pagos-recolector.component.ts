import { Component, OnInit } from '@angular/core';
import { PaginatedResponseDTO } from '../../../../models/paginated-response.dto';
import { PagoDTO } from '../../../../models/pago.dto';
import { PagosRecolectorService } from '../../../../services/pagos-recolector.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-lista-pagos-recolector',
  templateUrl: './lista-pagos-recolector.component.html',
  styleUrl: './lista-pagos-recolector.component.css'
})
export class ListaPagosRecolectorComponent implements OnInit {
  pagos: PaginatedResponseDTO<PagoDTO> = { content: [], totalPages: 0, page: 0, totalElements: 0, size: 0 };
  errorMessage: string | null = null;
  pageSize: number = 10;
  searchTerm: string = '';
  ordenColumna: string = 'fechaPago';
  ordenAscendente: boolean = true;
  recolectorId: string = "0";

  constructor(private pagosRecolectorService: PagosRecolectorService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.recolectorId = this.route.snapshot.paramMap.get('id') || "0";
    this.cargarPagos(0, this.pageSize);
  }

  cargarPagos(page: number, size: number): void {
    this.pagosRecolectorService.obtenerPagosRecolector(
      page,
      size,
      this.searchTerm,
      this.ordenColumna,
      this.ordenAscendente,
      this.recolectorId,
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

  buscarPagos(): void {
    this.cargarPagos(0, this.pageSize);
  }

  cambiarPagina(nuevaPagina: number): void {
    if (nuevaPagina >= 0 && nuevaPagina < (this.pagos?.totalPages || 0)) {
      this.cargarPagos(nuevaPagina, this.pageSize);
    }
  }

  cambiarOrden(columna: string): void {
    if (this.ordenColumna === columna) {
      this.ordenAscendente = !this.ordenAscendente;
    } else {
      this.ordenColumna = columna;
      this.ordenAscendente = true;
    }
    this.cargarPagos(this.pagos.page, this.pageSize);
  }
}