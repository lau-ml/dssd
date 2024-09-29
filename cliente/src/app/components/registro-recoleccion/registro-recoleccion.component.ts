import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegistroRecoleccion } from '../../models/registro-recoleccion.dto';
import { RegistroRecoleccionService } from '../../services/registro-recoleccion.service';

@Component({
  selector: 'app-registro-recoleccion',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './registro-recoleccion.component.html',
  styleUrl: './registro-recoleccion.component.css'
})
export class RegistroRecoleccionComponent {
  registroRecoleccion: RegistroRecoleccion | null = null;

  constructor(private registroRecoleccionService: RegistroRecoleccionService) { }

  ngOnInit(): void {
    this.cargarRegistro();
  }

  cargarRegistro(): void {
    this.registroRecoleccionService.obtenerUltimoRegistro("1").subscribe(
      (registro) => this.registroRecoleccion = registro,
      (error) => console.error('Error al cargar el registro', error)
    );
  }
}
