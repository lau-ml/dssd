import { DetalleRegistro } from "./detalle-registro.dto";
import { PagoDTO } from "./pago.dto";

export interface RegistroRecoleccion {
    id?: number;
    iUsuario?: number;
    idRecolector?: number;
    completado?: boolean;
    verificado?: boolean;
    detalleRegistros?: DetalleRegistro[];
    fechaRecoleccion: string;
    pago?: PagoDTO
}
