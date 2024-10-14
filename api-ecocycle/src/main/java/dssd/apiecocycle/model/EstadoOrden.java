package dssd.apiecocycle.model;

public enum EstadoOrden {
    PENDIENTE("Pendiente"),
    RECHAZADO("Rechazado"),
    ACEPTADO("Aceptado"),
    ENTREGADO("Entregado");

    private final String descripcion;

    EstadoOrden(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
