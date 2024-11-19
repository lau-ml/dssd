package dssd.server.exception;

public class PuntoDeRecoleccionException extends RuntimeException {
    private String codigoError;

    public PuntoDeRecoleccionException(String mensaje) {
        super(mensaje);
    }

    public PuntoDeRecoleccionException(String message, String codigoError) {
        super(message);
        this.codigoError = codigoError;
    }

    public String getCodigoError() {
        return codigoError;
    }

    public void setCodigoError(String codigoError) {
        this.codigoError = codigoError;
    }
}
