import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Material } from '../../models/material.dto';
import { MaterialesService } from '../../services/materiales.service';
import { UsuarioService } from '../../services/usuario.service';
import { RecolectorDTO } from '../../models/recolector.dto';

@Component({
  selector: 'app-formulario-material',
  templateUrl: './formulario-material.component.html',
  styleUrl: './formulario-material.component.css'
})
export class FormularioMaterialComponent implements OnInit {
  materiales: Material[] = [];
  recolectores: RecolectorDTO[] = [];
  recolectorSeleccionado?: RecolectorDTO;

  materialForm: FormGroup = new FormGroup({});

  constructor(private formBuilder: FormBuilder, private materialesService: MaterialesService, private usuarioService: UsuarioService) { }

  ngOnInit(): void {
    this.pedirMateriales();
    this.pedirRecolectores();
    this.materialForm = this.formBuilder.group({
      recolector: ['', Validators.required],
      nombre: ["", Validators.required],
      materiales: this.formBuilder.array([]),
    });
  }

  onSubmit() {
    if (this.materialForm.valid) {
      console.log('Formulario enviado', this.materialForm.value);
    }
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
  pedirRecolectores(): void {
    this.usuarioService.getRecolectores().subscribe({
      next: (data) => (this.recolectores = data),
      error: (error) => console.error('Error fetching recolectores', error),
    });
  }

  get f() {
    return this.materialForm.controls;
  }

  cancelar() {
    this.materialForm.reset();
    this.materialesArray.clear();
    this.recolectorSeleccionado = undefined;
  }

  get materialesArray() {
    return this.f['materiales'] as FormArray;
  }

  agregarMaterial() {
    this.materialesArray.push(this.formBuilder.group({
      nombre: ['', Validators.required],
      cantidad: ['', [Validators.required, Validators.min(1)]],
    }));
  }

  borrarMaterial(posicion: number) {
    this.materialesArray.removeAt(posicion);
    this.materialesArray.markAsDirty();
  }

  actualizarDatosRecolector() {
    const recolectorId = Number(this.materialForm.get('recolector')?.value);
    this.recolectorSeleccionado = this.recolectores.find(r => r.id === recolectorId);

  }
}