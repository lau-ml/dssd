import { Component, OnInit } from '@angular/core';
import { RegistroRecoleccion } from '../../../../models/registro-recoleccion.dto';
import { ActivatedRoute } from '@angular/router';
import { RegistroRecoleccionService } from '../../../../services/registro-recoleccion.service';

@Component({
  selector: 'app-registro',
  templateUrl: './registro.component.html',
  styleUrl: './registro.component.css'
})
export class RegistroComponent implements OnInit {
  registroRecoleccion: RegistroRecoleccion | null = null;
  errorMessage: string | null = null;

  links = [
    { label: 'Recolectores', url: '/list-recolectores', icon: 'fas fa-users' },
    { label: 'Pagos', url: '' }
  ];
  activeLabel = `Registro 0`;

  constructor(
    private route: ActivatedRoute,
    private registroRecoleccionService: RegistroRecoleccionService
  ) { }

  ngOnInit(): void {
    const recolectorId = this.route.snapshot.paramMap.get('id');
    const id = this.route.snapshot.paramMap.get('idRegistro');
    this.activeLabel = `Registro ${id}`;
    this.links = [
      { label: 'Recolectores', url: '/list-recolectores', icon: 'fas fa-users' },
      { label: `Recolector ${recolectorId}`, url: `/recolector/${recolectorId}/administrar`, icon: 'fas fa-user' },
      { label: `Pagos`, url: `/recolector/${recolectorId}/pagos`, icon: 'fas fa-wallet' },
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
