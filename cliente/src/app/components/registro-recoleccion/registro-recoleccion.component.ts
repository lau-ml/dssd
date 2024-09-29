import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegistroRecoleccion } from '../../models/registro-recoleccion.dto';
import { RegistroRecoleccionService } from '../../services/registro-recoleccion.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-registro-recoleccion',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './registro-recoleccion.component.html',
  styleUrl: './registro-recoleccion.component.css'
})
export class RegistroRecoleccionComponent {
  registroRecoleccion: RegistroRecoleccion | null = null;
  id_temporal: Number = 1;

  constructor(private router: Router, private registroRecoleccionService: RegistroRecoleccionService) { }

  ngOnInit(): void {
    this.cargarRegistro();
  }

  cargarRegistro(): void {
    this.registroRecoleccionService.obtenerUltimoRegistro(this.id_temporal).subscribe(
      (data) => {
        this.registroRecoleccion = data;
      },
      (error) => {
        console.error('Error al obtener el registro:', error);
      }
    );
  }

  nuevoMaterial(): void {
    this.router.navigate(['/cargar-material']);
  }
}
