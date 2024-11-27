import { Material } from "./material.dto";
import { PuntoDeRecoleccion } from "./punto-recoleccion.dto";

export interface DetalleRegistro {
  idUsuario: number;
  idRegistroRecoleccion: number;
  cantidadRecolectada: number;
  cantidadRecibida?: number;
  material: Material;
  puntoDeRecoleccion: PuntoDeRecoleccion;
}
