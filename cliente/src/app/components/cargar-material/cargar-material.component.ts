import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MaterialesService } from '../../services/materiales.service';
import { UbicacionesService } from '../../services/ubicaciones.service';
import { Ubicacion } from '../../models/ubicacion.dto';
import { Material } from '../../models/material.dto';
import { CommonModule } from '@angular/common';
import { DetalleRegistroRecoleccionService } from '../../services/detalle-registro-recoleccion.service';
import { DetalleRegistro } from '../../models/detalle-registro.dto';

@Component({
  selector: 'app-cargar-material',
  templateUrl: './cargar-material.component.html',
  styleUrl: './cargar-material.component.css'
})
export class CargarMaterialComponent {
  ubicaciones: Ubicacion[] | null = null;
  materiales: Material[] | null = null;
  nuevoMaterial = {
    nombre: '',
    cantidadRecolectada: 0,
    ubicacion: ''
  };

  constructor(private router: Router, private materialesService: MaterialesService, private ubicacionesService: UbicacionesService, private detalleRegistroRecoleccionService: DetalleRegistroRecoleccionService) { }

  ngOnInit(): void {
    this.pedirMateriales();
    this.pedirUbicaciones();
  }

  pedirMateriales(): void {
    this.materialesService.obtenerMateriales().subscribe(
      (data) => {
        this.materiales = data;
      },
      (error) => {
        console.error('Error al obtener materiales:', error);
      }
    )
  }

  pedirUbicaciones(): void {
    this.ubicacionesService.obtenerUbicaciones().subscribe(
      (data) => {
        this.ubicaciones = data;
      },
      (error) => {
        console.error('Error al obtener ubicaciones:', error);
      }
    )
  }

  onSubmit() {
    const detalleRegistro: DetalleRegistro = {
      idRegistroRecoleccion: 1, // ESTO HAY QUE CAMBIAR; POR AHORA PUSE QUE SIEMPRE MODIFIQUE EL 1
      cantidadRecolectada: this.nuevoMaterial.cantidadRecolectada,
      material: {
        id: this.materiales?.find(material => material.nombre === this.nuevoMaterial.nombre)?.id || 0,
      },
      ubicacion: {
        id: this.ubicaciones?.find(ubicacion => ubicacion.nombreEstablecimiento === this.nuevoMaterial.ubicacion)?.id || 0,
      },
    };
    this.detalleRegistroRecoleccionService.addNewMaterial(detalleRegistro).subscribe(
      (response) => {
        console.log('Material agregado exitosamente:', response);
        this.router.navigate(['/']); // Por ahora que vuelva al HOME donde esta el Registro (hasta que se cambie de lugar)
      },
      (error) => {
        console.error('Error al agregar material:', error);
      }
    );

  }

  cancelar() {
    this.router.navigate(['/']); // Por ahora que vuelva al HOME donde esta el Registro (hasta que se cambie de lugar)
  }
}
