package dssd.apiecocycle.model;

import lombok.Getter;

@Getter
public enum EstadoOrden {
    PENDIENTE("Pendiente"),
    RECHAZADA("Rechazada"),
    ACEPTADA("Aceptada"),
    PREPARANDO("Preparando"),
    PREPARADA("Preparada"),
    ENVIADA("Enviada"),
    ENTREGADA("Entregada");


    private final String descripcion;

    EstadoOrden(String descripcion) {
        this.descripcion = descripcion;
    }

}
