import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Material } from '../../../models/material.dto';
import { MaterialesService } from '../../../services/materiales.service';
import { UsuarioService } from '../../../services/usuario.service';
import { RecolectorDTO } from '../../../models/recolector.dto';
import { ActivatedRoute, Router } from '@angular/router';
import { RegistroRecoleccionService } from '../../../services/registro-recoleccion.service';
import { MatSnackBar } from '@angular/material/snack-bar';

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

    constructor(private snackBar: MatSnackBar, private formBuilder: FormBuilder, private materialesService: MaterialesService, private usuarioService: UsuarioService, private registroRecoleccionService: RegistroRecoleccionService, private router: Router, private route: ActivatedRoute) {
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
            const dto = {
                idRecolector: this.materialForm.value.recolector,
                detalleRegistros: this.materialesArray.value.map((m: { nombre: string; cantidad: number }) => ({
                    cantidadRecibida: m.cantidad,
                    material: {
                        id: m.nombre
                    }
                }))
            };

            this.registroRecoleccionService.materialesEntregadosDelRecolector(dto)
                .subscribe(
                    response => {
                        this.snackBar.open('✅ Registro actualizado correctamente. Materiales entregados exitosamente.', 'Cerrar', {
                            duration: 4000,
                            panelClass: ['success-snackbar'],
                            verticalPosition: 'top',
                            horizontalPosition: 'center'
                        });


                        if (this.recolectorPreseleccionado) {
                            this.router.navigate(['/recolectores']);
                        } else {
                            this.cancelar();
                        }
                    },
                    error => {
                        let errorMessage = '⚠️ Error al actualizar registro';

                        if (error && error.codigoError) {
                            errorMessage = `⚠️ ${error.mensaje}`;
                        }

                        this.snackBar.open(errorMessage, 'Cerrar', {
                            panelClass: ['error-snackbar'],
                            verticalPosition: 'top',
                            horizontalPosition: 'center'
                        });
                        console.error('Error al actualizar registro:', error);
                    }
                );

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
