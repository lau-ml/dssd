import { DetalleRegistro } from "./detalle-registro.dto";

export interface RegistroRecoleccion {
    id: number;
    idRecolector: number;
    fechaRecoleccion: Date;
    completado: boolean;
    detalleRegistros: DetalleRegistro[];
}