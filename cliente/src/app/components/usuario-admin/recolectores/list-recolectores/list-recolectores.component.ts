import { Component, OnInit } from '@angular/core';
import { PaginatedResponseDTO } from '../../../../models/paginated-response.dto';
import { RecolectorDTO } from '../../../../models/recolector.dto';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { RecolectorServiceService } from '../../../../services/recolector-service.service';

@Component({
  selector: 'app-list-recolectores',
  templateUrl: './list-recolectores.component.html',
  styleUrl: './list-recolectores.component.css'
})
export class ListRecolectoresComponent implements OnInit {
  recolectores: PaginatedResponseDTO<RecolectorDTO> = { content: [], totalPages: 0, page: 0, totalElements: 0, size: 0 };
  searchTerm: string = '';
  pageSize: number = 10;
  errorMessage: string | null = null;

  constructor(
    private recolectorService: RecolectorServiceService,
    private snackBar: MatSnackBar,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.cargarRecolectores(0, this.pageSize);
  }

  cargarRecolectores(page: number, size: number): void {
    this.recolectorService.obtenerRecolectores(page, size, this.searchTerm).subscribe(
      (data: PaginatedResponseDTO<RecolectorDTO>) => {
        this.recolectores = data;
      },
      (error) => {
        console.error('Error al cargar los recolectores:', error);
        this.errorMessage = 'Hubo un error al cargar los recolectores. Intente más tarde.';
      }
    );
  }

  buscarRecolectores(): void {
    this.cargarRecolectores(0, this.pageSize);
  }

  cambiarPagina(nuevaPagina: number): void {
    if (nuevaPagina >= 0 && nuevaPagina < (this.recolectores.totalPages || 0)) {
      this.cargarRecolectores(nuevaPagina, this.pageSize);
    }
  }

  // Redirige a la página de administración de un recolector
  administrarRecolector(recolectorId: number): void {
    this.router.navigate([`/administrar-recolector/${recolectorId}`]);
  }
}
