import { DetalleRegistro } from "./detalle-registro.dto";

export interface RegistroRecoleccion {
    id?: number;
    iUsuario?: number;
    idRecolector?: number;
    completado?: boolean;
    verificado?: boolean;
    detalleRegistros?: DetalleRegistro[];
}
