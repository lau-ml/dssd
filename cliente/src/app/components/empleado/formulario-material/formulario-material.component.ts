import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Material } from '../../../models/material.dto';
import { MaterialesService } from '../../../services/materiales.service';
import { UsuarioService } from '../../../services/usuario.service';
import { RecolectorDTO } from '../../../models/recolector.dto';
import { Router } from '@angular/router';

@Component({
    selector: 'app-formulario-material',
    templateUrl: './formulario-material.component.html',
    styleUrls: ['./formulario-material.component.css']
})
export class FormularioMaterialComponent implements OnInit {
    recolectorPreseleccionado?: RecolectorDTO | null;
    mostrarBotonVolver: boolean = false;
    materiales: Material[] = [];
    recolectores: RecolectorDTO[] = [];
    recolectorSeleccionado?: RecolectorDTO;

    materialForm: FormGroup;

    constructor(private formBuilder: FormBuilder, private materialesService: MaterialesService, private usuarioService: UsuarioService, private router: Router) {
        this.materialForm = this.formBuilder.group({
            recolector: ['', Validators.required],
            materiales: this.formBuilder.array([], Validators.required),
        });
    }

    ngOnInit(): void {
        this.pedirMateriales();

        if (this.usuarioService.hasRecolectorSeleccionado()) {
            this.recolectorPreseleccionado = this.usuarioService.getRecolectorSeleccionado();
            this.mostrarBotonVolver = true;

            this.materialForm.patchValue({
                recolector: this.recolectorPreseleccionado?.id
            });
            this.usuarioService.clearRecolector();
        } else {
            this.pedirRecolectores();
        }
    }

    onSubmit() {
        if (this.materialForm.valid && this.materialesArray.length > 0) {
            const submittedData = this.materialesArray.value.map((m: { id: string; cantidad: number }) => ({
                id: m.id,
                cantidad: m.cantidad
            }));
            console.log('Formulario enviado', { recolector: this.materialForm.value.recolector, materiales: submittedData });
            const dto = {
                idRecolector: this.materialForm.value.recolector,
                detalleRegistros: this.materialesArray.value.map((m: { nombre: string; cantidad: number }) => ({
                    cantidadRecolectada: m.cantidad,
                    material: {
                        id: m.nombre
                    }
                }))
            };
            console.log("DTO: ", dto);

        } else {
            if (this.materialesArray.length === 0) {
                alert('Debe agregar al menos un material.');
            }
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
        );
    }

    pedirRecolectores(): void {
        this.usuarioService.getRecolectores().subscribe({
            next: (data) => this.recolectores = data,
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

    volver(): void {
        this.router.navigate(['/recolectores']);
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
