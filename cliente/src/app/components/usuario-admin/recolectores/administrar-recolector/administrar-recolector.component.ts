import { Component, OnInit } from '@angular/core';
import { PuntoDeRecoleccion } from '../../../../models/punto-recoleccion.dto';
import { RecolectorServiceService } from '../../../../services/recolector-service.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { PagoDTO } from '../../../../models/pago.dto';
import { RecolectorDTO } from '../../../../models/recolector.admin.dto';

@Component({
  selector: 'app-administrar-recolector',
  templateUrl: './administrar-recolector.component.html',
  styleUrl: './administrar-recolector.component.css'
})
export class AdministrarRecolectorComponent implements OnInit {
  recolector: RecolectorDTO | null = null;
  pagos: PagoDTO[] = [];
  puntosDeRecoleccion: PuntoDeRecoleccion[] = [];
  errorMessage: string | null = null;

  constructor(
    private recolectorService: RecolectorServiceService,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar,
    private router: Router
  ) { }

  ngOnInit(): void {
    const recolectorId = this.route.snapshot.paramMap.get('id');
    if (recolectorId) {
      this.obtenerRecolector(recolectorId);
      this.obtenerPagos(recolectorId);
      this.obtenerPuntosDeRecoleccion(recolectorId);
    }
  }

  obtenerRecolector(id: string): void {
    this.recolectorService.obtenerRecolectorPorId(id).subscribe(
      (data: RecolectorDTO) => {
        this.recolector = data;
      },
      (error) => {
        console.error('Error al obtener el recolector:', error);
        this.errorMessage = 'Hubo un error al cargar los datos del recolector.';
      }
    );
  }

  obtenerPagos(id: string): void {

  }

  obtenerPuntosDeRecoleccion(id: string): void {

  }

  cambiarEstadoRecolector(activar: boolean): void {
    if (this.recolector) {
      console.log("aca estoy");

      const solicitud = activar
        ? this.recolectorService.activarRecolector(this.recolector.id)
        : this.recolectorService.desactivarRecolector(this.recolector.id);

      solicitud.subscribe(
        (data) => {
          this.recolector!.activo = activar;
          this.snackBar.open(
            `Recolector ${activar ? 'habilitado' : 'deshabilitado'} correctamente.`,
            'Cerrar',
            { duration: 3000 }
          );
        },
        (error) => {
          console.error(
            `Error al ${activar ? 'habilitar' : 'deshabilitar'} el recolector:`,
            error
          );
          this.snackBar.open(
            `Hubo un error al ${activar ? 'habilitar' : 'deshabilitar'} al recolector.`,
            'Cerrar',
            { duration: 3000 }
          );
        }
      );
    }
  }
}