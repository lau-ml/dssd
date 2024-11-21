import { Centro } from "./centro";

export interface RecolectorDTO {
    id: number;
    nombre: string;
    apellido: string;
    username: string;
    dni: string;
    email: string;
    activo: boolean;
    centroDeRecoleccion: Centro;
}