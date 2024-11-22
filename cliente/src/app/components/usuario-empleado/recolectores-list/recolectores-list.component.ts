import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UsuarioService } from '../../../services/usuario.service';
import { RecolectorDTO } from '../../../models/recolector.dto';
import { PaginatedResponseDTO } from '../../../models/paginated-response.dto';

@Component({
  selector: 'app-recolectores-list',
  templateUrl: './recolectores-list.component.html',
  styleUrls: ['./recolectores-list.component.css']
})
export class RecolectoresListComponent implements OnInit {
  paginatedRecolectores: PaginatedResponseDTO<RecolectorDTO> = { content: [], totalPages: 0, page: 0, totalElements: 0, size: 0 };
  pageSize: number = 10;
  searchTerm: string = '';
  ordenColumna: string = 'dni';
  ordenAscendente: boolean = true;

  constructor(private usuarioService: UsuarioService, private router: Router) { }

  ngOnInit(): void {
    this.cargarRecolectores(0, this.pageSize);
  }

  cargarRecolectores(page: number, size: number): void {
    this.usuarioService.getRecolectoresPaginados(page,
      size,
      this.searchTerm,
      this.ordenColumna,
      this.ordenAscendente
    ).subscribe(
      (data: PaginatedResponseDTO<RecolectorDTO>) => {
        this.paginatedRecolectores = data;
      },
      (error) => {
        console.error('Error al obtener recolectores:', error);
      }
    );
  }

  generarFormulario(recolector: RecolectorDTO): void {
    if (recolector.tieneRegistroCompletoPendiente) {
      this.usuarioService.setRecolector(recolector);
      this.router.navigate([`formulario-material`]);
    }
  }
  buscar(): void {
    this.cargarRecolectores(0, this.pageSize);
  }

  cambiarPagina(nuevaPagina: number): void {
    if (nuevaPagina >= 0 && nuevaPagina < (this.paginatedRecolectores?.totalPages || 0)) {
      this.cargarRecolectores(nuevaPagina, this.pageSize);
    }
  }

  cambiarOrden(columna: string): void {
    if (this.ordenColumna === columna) {
      this.ordenAscendente = !this.ordenAscendente;
    } else {
      this.ordenColumna = columna;
      this.ordenAscendente = true;
    }
    this.cargarRecolectores(this.paginatedRecolectores.page, this.pageSize);
  }
}
