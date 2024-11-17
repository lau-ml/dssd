import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MaterialesService } from '../../services/materiales.service';
import { PuntoDeRecoleccionService } from '../../services/punto-recoleccion.service';
import { PuntoDeRecoleccion } from '../../models/punto-recoleccion.dto';
import { Material } from '../../models/material.dto';
import { DetalleRegistroRecoleccionService } from '../../services/detalle-registro-recoleccion.service';
import { DetalleRegistro } from '../../models/detalle-registro.dto';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { SweetalertService } from "../../services/sweetalert.service";
import { RegistroRecoleccionService } from "../../services/registro-recoleccion.service";
import { RegistroRecoleccion } from "../../models/registro-recoleccion.dto";


@Component({
  selector: 'app-cargar-material',
  templateUrl: './cargar-materiales.component.html',
  styleUrl: './cargar-materiales.component.css'
})
export class CargarMaterialesComponent {
  puntosRecoleccion: PuntoDeRecoleccion[] = [];
  materiales: Material[] = [];
  id_temporal: number = 1;
  formulario: FormGroup = new FormGroup({})
  registroRecoleccion: RegistroRecoleccion | null = null;
  errorMessage: string | null = null;
  constructor(private router: Router, private materialesService: MaterialesService, private puntoDeRecoleccionService: PuntoDeRecoleccionService, private detalleRegistroRecoleccionService: DetalleRegistroRecoleccionService
    , private sweetAlertService: SweetalertService, private formBuilder: FormBuilder,
    private registroRecoleccionService: RegistroRecoleccionService) {
  }

  ngOnInit(): void {
    this.pedirMateriales();
    this.pedirPuntosDeRecolecciones();
    this.formulario = this.formBuilder.group({
      nombre: ["", Validators.required],
      cantidadRecolectada: ["", [Validators.required, Validators.min(1)]],
      puntoDeRecoleccion: ["", Validators.required]
    })
    this.cargarRegistro()
  }

  cargarRegistro(): void {
    this.registroRecoleccionService.obtenerUltimoRegistro(this.id_temporal).subscribe(
      (data) => {
        this.registroRecoleccion = data;
        this.errorMessage = null;
      },
      (error) => {
        if (error && error.codigoError && error.codigoError === "REGISTRO_PENDIENTE") {
          this.errorMessage = `${error.mensaje}`;
        } else {
          this.errorMessage = 'Ha ocurrido un error inesperado. Por favor, intenta nuevamente.';
        }
      }
    );
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

  pedirPuntosDeRecolecciones(): void {
    this.puntoDeRecoleccionService.obtenerPuntosDeRecoleccion().subscribe(
      (data) => {
        this.puntosRecoleccion = data;
      },
      (error) => {
        console.error('Error al obtener puntos de recolecciones:', error);
      }
    )
  }

  onSubmit() {

    const detalleRegistro: DetalleRegistro = {

      idUsuario: this.id_temporal,
      idRegistroRecoleccion: 1, // ESTO HAY QUE CAMBIAR; POR AHORA PUSE QUE SIEMPRE MODIFIQUE EL 1
      cantidadRecolectada: this.formulario.get('cantidadRecolectada')?.value,
      material: {
        id: this.materiales?.find(material => material.nombre == this.formulario.get('nombre')?.value)?.id || 0,

      },
      puntoDeRecoleccion: {
        id: this.puntosRecoleccion?.find(punto => punto.nombreEstablecimiento == this.formulario.get('puntoDeRecoleccion')?.value)?.id || 0,
      },
    };

    this.detalleRegistroRecoleccionService.addNewMaterial(detalleRegistro).subscribe(
      (response) => {
        console.log('Material agregado exitosamente:', response);
        this.sweetAlertService.showAlert('success', 'Material agregado', 'El material ha sido agregado exitosamente');
        this.formulario.reset();
      },

      (error) => {
        if (error == 'Error: Ya tiene un registro completado sin verificar') {
          this.sweetAlertService.showAlert('error', 'Registro pendiente', "Ya tienes un registro pendiente de verificar, por favor acércate a tu centro de recolección asignado.");
        } else {
          console.log("fue nuevamente por el else");

          this.sweetAlertService.showAlert('error', 'Error al agregar material', 'Ha ocurrido un error desconocido.');
        }
        console.error('Error al agregar material:', error);
      }
    );

  }


  get f() {
    return this.formulario.controls;
  }

  cancelar() {
    this.formulario.reset();

  }


}
