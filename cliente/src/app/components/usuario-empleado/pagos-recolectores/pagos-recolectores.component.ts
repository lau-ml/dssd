import { Component, OnInit } from '@angular/core';
import { PaginatedResponseDTO } from '../../../models/paginated-response.dto';
import { PagoDTO } from '../../../models/pago.dto';
import { PagosRecolectorService } from '../../../services/pagos-recolector.service';
import { ActivatedRoute } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-pagos-recolectores',
  templateUrl: './pagos-recolectores.component.html',
  styleUrl: './pagos-recolectores.component.css'
})
export class PagosRecolectoresComponent implements OnInit {
  pagos: PaginatedResponseDTO<PagoDTO> = { content: [], totalPages: 0, page: 0, totalElements: 0, size: 0 };
  errorMessage: string | null = null;
  pageSize: number = 10;
  estadoFiltro: string = 'PENDIENTE';
  columnaFecha: string = 'fechaEmision';
  fechaDesde: string | null = null;
  fechaHasta: string | null = null;
  ordenColumna: string = 'fechaEmision';
  ordenAscendente: boolean = false;
  recolectorId: string = "0";
  mostrarFiltros: boolean = false;
  usernameFiltro: string | null = null;

  constructor(private snackBar: MatSnackBar, private pagosRecolectorService: PagosRecolectorService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.recolectorId = this.route.snapshot.paramMap.get('id') || "0";
    this.cargarPagos(0, this.pageSize);
  }

  cargarPagos(page: number, size: number): void {
    this.pagosRecolectorService.obtenerPagosDelCentro(
      page,
      size,
      this.estadoFiltro,
      this.columnaFecha,
      this.fechaDesde,
      this.fechaHasta,
      this.ordenColumna,
      this.ordenAscendente,
      this.usernameFiltro,
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
    this.cargarPagos(0, this.pageSize);
  }

  borrarFiltros(): void {
    this.estadoFiltro = 'TODOS';
    this.columnaFecha = 'fechaEmision';
    this.fechaDesde = null;
    this.fechaHasta = null;
    this.usernameFiltro = null;
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

  redirigirRegistro(registroRecoleccionId: number): void {
    // this.router.navigate(['/registro-recoleccion', registroRecoleccionId]);
  }

  pagarPago(pagoId: number): void {
    this.pagosRecolectorService.pagarPago(pagoId).subscribe(
      () => {
        this.snackBar.open('✅ Pago realizado con éxito.', 'Cerrar', {
          duration: 4000,
          panelClass: ['success-snackbar'],
          verticalPosition: 'top',
          horizontalPosition: 'center'
        });
        this.cargarPagos(this.pagos.page, this.pageSize);
      },
      (error) => {
        console.error('Error al realizar el pago:', error);
        this.snackBar.open('❌ Hubo un error al realizar el pago. Por favor, inténtelo más tarde.', 'Cerrar', {
          duration: 4000,
          panelClass: ['error-snackbar'],
          verticalPosition: 'top',
          horizontalPosition: 'center'
        });
      }
    );
  }
}