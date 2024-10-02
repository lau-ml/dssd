import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {MaterialesService} from '../../services/materiales.service';
import {UbicacionesService} from '../../services/ubicaciones.service';
import {Ubicacion} from '../../models/ubicacion.dto';
import {Material} from '../../models/material.dto';
import {DetalleRegistroRecoleccionService} from '../../services/detalle-registro-recoleccion.service';
import {DetalleRegistro} from '../../models/detalle-registro.dto';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SweetalertService} from "../../services/sweetalert.service";


@Component({
  selector: 'app-cargar-material',
  templateUrl: './cargar-materiales.component.html',
  styleUrl: './cargar-materiales.component.css'
})
export class CargarMaterialesComponent {
  ubicaciones: Ubicacion[] = [];
  materiales: Material[] = [];
  nuevoMaterial = {
    nombre: '',
    cantidadRecolectada: 0,
    ubicacion: ''
  };
  formulario : FormGroup = new FormGroup({})

  constructor(private router: Router, private materialesService: MaterialesService, private ubicacionesService: UbicacionesService, private detalleRegistroRecoleccionService: DetalleRegistroRecoleccionService
    , private sweetAlertService: SweetalertService, private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.pedirMateriales();
    this.pedirUbicaciones();
    this.formulario = this.formBuilder.group({
      nombre : ["", Validators.required],
      cantidadRecolectada: ["", [Validators.required, Validators.min(1)]],
      ubicacion:  ["", Validators.required]
    })

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
      cantidadRecolectada: this.formulario.get('cantidadRecolectada')?.value,
      material: {
        id: this.materiales?.find(material => material.nombre == this.formulario.get('nombre')?.value)?.id || 0,

      },
      ubicacion: {
        id: this.ubicaciones?.find(ubicacion => ubicacion.nombreEstablecimiento == this.formulario.get('ubicacion')?.value)?.id || 0,
      },
    };

    this.detalleRegistroRecoleccionService.addNewMaterial(detalleRegistro).subscribe(
      (response) => {
        console.log('Material agregado exitosamente:', response);
        this.sweetAlertService.showAlert('success', 'Material agregado', 'El material ha sido agregado exitosamente');
        this.formulario.reset();
        },

      (error) => {
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
