import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegistroRecoleccion } from '../../models/registro-recoleccion.dto';
import { RegistroRecoleccionService } from '../../services/registro-recoleccion.service';
import { Router } from '@angular/router';
import { CargarMaterialesComponent } from "../cargar-materiales/cargar-materiales.component";
import { SweetalertService } from "../../services/sweetalert.service";

@Component({
  selector: 'app-registro-recoleccion',
  templateUrl: './registro-recoleccion.component.html',
  styleUrl: './registro-recoleccion.component.css'
})
export class RegistroRecoleccionComponent {
  registroRecoleccion: RegistroRecoleccion | null = null;
  id_temporal: Number = 1;
  errorMessage: string | null = null;

  constructor(private router: Router,
    private sweetAlertService: SweetalertService,
    private registroRecoleccionService: RegistroRecoleccionService) { }

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
        if (error && error.codigoError && error.codigoError === "REGISTRO_PENDIENTE") {
          this.errorMessage = `${error.mensaje}`;
        } else {
          this.errorMessage = 'Ha ocurrido un error inesperado. Por favor, intenta nuevamente.';
        }
      }
    );
  }

  completarRegistro(): void {
    if (this.registroRecoleccion) {
      this.registroRecoleccionService.completarRegistro(this.registroRecoleccion?.id ?? 0).subscribe(
        (response) => {
          console.log('Registro completado con éxito:', response);
          this.cargarRegistro();
          this.sweetAlertService.showAlert('success', 'Pedido de recolección completado', 'El pedido de recolección se ha completado con éxito');
        },
        (error) => {
          this.sweetAlertService.showAlert('error', 'Error al completar el registro', 'Ha ocurrido un error al completar el registro');
          console.error('Error al completar el registro:', error);
        }
      );
    }
  }

  nuevoMaterial(): void {
    this.router.navigate(['/cargar-material']);
  }

  protected readonly CargarMaterialComponent = CargarMaterialesComponent;

  cancelarRegistro() {
    this.registroRecoleccionService.cancelarRegistro(this.registroRecoleccion?.id ?? 0).subscribe(
      (response) => {
        console.log('Registro cancelado con éxito:', response);
        this.cargarRegistro();
        this.registroRecoleccion = null;
        this.sweetAlertService.showAlert('success', 'Pedido de recolección cancelado', 'El pedido de recolección se ha cancelado con éxito');
      },
      (error) => {
        this.sweetAlertService.showAlert('error', 'Error al cancelar el registro', 'Ha ocurrido un error al cancelar el registro');
        console.error('Error al cancelar el registro:', error);
      }
    )
  }
}
