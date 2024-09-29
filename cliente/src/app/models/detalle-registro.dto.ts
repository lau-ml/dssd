import { Material } from "./material.dto";
import { Ubicacion } from "./ubicacion.dto";

export interface DetalleRegistro {
    idRegistroRecoleccion: number;
    cantidadRecolectada: number;
    material: Material;
    ubicacion: Ubicacion;
}