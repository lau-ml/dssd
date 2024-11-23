export interface PagoDTO {
    id: number;
    fechaEmision: string;
    fechaPago: string;
    monto: number;
    registroRecoleccionId: number;
    estado: string;
}