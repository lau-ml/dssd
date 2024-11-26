import { RecolectorDTO } from "./recolector.admin.dto";

export interface PagoDTO {
    id: number;
    fechaEmision: string;
    fechaPago: string;
    monto: number;
    registroRecoleccionId: number;
    estado: string;
    recolector: RecolectorDTO;
}