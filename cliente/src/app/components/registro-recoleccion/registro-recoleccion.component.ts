import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegistroRecoleccion } from '../../models/registro-recoleccion.dto';
import { RegistroRecoleccionService } from '../../services/registro-recoleccion.service';
import { Router } from '@angular/router';
import { CargarMaterialesComponent } from "../cargar-materiales/cargar-materiales.component";
import {SweetalertService} from "../../services/sweetalert.service";

@Component({
  selector: 'app-registro-recoleccion',
  templateUrl: './registro-recoleccion.component.html',
  styleUrl: './registro-recoleccion.component.css'
})
export class RegistroRecoleccionComponent {
  registroRecoleccion: RegistroRecoleccion | null = null;
  id_temporal: Number = 1;
  errorMessage: string | null = null;

  constructor(private router: Router, private registroRecoleccionService: RegistroRecoleccionService,
              private sweetAlertService:SweetalertService) { }

  ngOnInit(): void {
    this.cargarRegistro();
  }
//Revisar creaciòn de registro de recolecciòn
  cargarRegistro(): void {
    this.registroRecoleccionService.obtenerUltimoRegistro(this.id_temporal).subscribe(
      (data) => {
        this.registroRecoleccion = data;
        this.errorMessage = null;
      },
      (error) => {
        console.error('Error al obtener el registro:', error);
        console.log(error.error)
        if (error.error == 'Tiene un registro pendiente de validación.') {
          this.errorMessage = error.error;
        }
      }
    );
  }

  completarRegistro(): void {
    if (this.registroRecoleccion) {
      this.registroRecoleccionService.completarRegistro(this.registroRecoleccion?.id ?? 0).subscribe(
        (response) => {
          console.log('Registro completado con éxito:', response);
          this.sweetAlertService.showAlert('success', 'Material agregado', 'El material ha sido agregado exitosamente');

          this.cargarRegistro();
        },
        (error) => {
          console.error('Error al completar el registro:', error);
        }
      );
    }
  }

  nuevoMaterial(): void {
    this.router.navigate(['/cargar-material']);
  }

  protected readonly CargarMaterialComponent = CargarMaterialesComponent;
}
