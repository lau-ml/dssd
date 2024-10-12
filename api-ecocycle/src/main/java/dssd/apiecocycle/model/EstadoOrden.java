package dssd.apiecocycle.model;

public enum EstadoOrden {
    EN_ESPERA("En Espera"),
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