import { Material } from "./material.dto";

export interface OrdenDeDistribucion {
    id: number;
    deposito: string;
    cantidad: number;
    material: Material;
    estado: string;
}
