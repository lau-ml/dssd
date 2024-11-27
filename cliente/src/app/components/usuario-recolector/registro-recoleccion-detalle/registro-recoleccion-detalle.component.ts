import { Component, OnInit } from '@angular/core';
import { RegistroRecoleccion } from '../../../models/registro-recoleccion.dto';
import { ActivatedRoute } from '@angular/router';
import { RegistroRecoleccionService } from '../../../services/registro-recoleccion.service';

@Component({
  selector: 'app-registro-recoleccion-detalle',
  templateUrl: './registro-recoleccion-detalle.component.html',
  styleUrl: './registro-recoleccion-detalle.component.css'
})
export class RegistroRecoleccionDetalleComponent implements OnInit {
  registroRecoleccion: RegistroRecoleccion | null = null;
  errorMessage: string | null = null;

  links = [
    { label: 'Mis pagos', url: '/mis-pagos', icon: 'fas fa-wallet' },
    { label: 'Registro', url: '' }
  ];
  activeLabel = `Registro 0`;

  constructor(
    private route: ActivatedRoute,
    private registroRecoleccionService: RegistroRecoleccionService
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.activeLabel = `Registro ${id}`;
    this.links = [
      { label: 'Mis pagos', url: '/mis-pagos', icon: 'fas fa-wallet' },
      { label: 'Registro', url: '' }
    ];
    if (id) {
      this.registroRecoleccionService.getRegistroRecoleccion(+id).subscribe(
        (data) => {
          this.registroRecoleccion = data;
        },
        (error) => {
          console.error('Error al cargar el registro de recolección', error);
          this.errorMessage = 'No se pudo cargar el registro de recolección.';
        }
      );
    }
  }
}
