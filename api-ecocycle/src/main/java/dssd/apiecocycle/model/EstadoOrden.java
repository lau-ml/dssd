package dssd.apiecocycle.model;

import lombok.Getter;

@Getter
public enum EstadoOrden {
    PENDIENTE("Pendiente"),
    RECHAZADO("Rechazado"),
    ACEPTADO("Aceptado"),
    PREPARANDO("Preparando"),
    PREPARADA("Preparada"),
    ENVIADA("Enviada"),
    ENTREGADO("Entregado");


    private final String descripcion;

    EstadoOrden(String descripcion) {
        this.descripcion = descripcion;
    }

}
