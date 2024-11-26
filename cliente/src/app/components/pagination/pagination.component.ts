import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrl: './pagination.component.css'
})
export class PaginationComponent {
  @Input() totalPages: number = 0;
  @Input() currentPage: number = 0;
  @Output() pageChanged: EventEmitter<number> = new EventEmitter<number>();

  cambiarPagina(nuevaPagina: number): void {
    if (nuevaPagina >= 0 && nuevaPagina < this.totalPages) {
      this.pageChanged.emit(nuevaPagina);
    }
  }
}
