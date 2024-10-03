package dssd.server.helpers;

public enum EstadoPedidoRecoleccion {
    cargar,
    confirmar,
    cancelar;

    public String valor() {
        return name();
    }

    public String valorActual() {
        return this.name(); // Devuelve el nombre de la constante de enumeración como String
    }
}

