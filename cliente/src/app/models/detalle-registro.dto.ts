import { Material } from "./material.dto";
import { Ubicacion } from "./ubicacion.dto";

export interface DetalleRegistro {
  idUsuario: number;
    idRegistroRecoleccion: number;
    cantidadRecolectada: number;
    material: Material;
    ubicacion: Ubicacion;
}
