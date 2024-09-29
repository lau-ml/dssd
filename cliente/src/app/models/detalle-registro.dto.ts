import { Material } from "./material.dto";
import { Ubicacion } from "./ubicacion.dto";

export interface DetalleRegistro {
    cantidadRecolectada: number;
    material: Material;
    ubicacion: Ubicacion;
}