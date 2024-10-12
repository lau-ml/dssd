package dssd.apiecocycle.model;

public enum EstadoOrden {
    PEDNDIENTE("Pendiente"),
    RECHAZADO("Rechazado"),
    ENTREGADO("Entregado");

    private final String descripcion;

    EstadoOrden(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}