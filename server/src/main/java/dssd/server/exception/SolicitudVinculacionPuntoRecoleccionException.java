package dssd.server.exception;

public class SolicitudVinculacionPuntoRecoleccionException extends RuntimeException {
    private String codigoError;

    public SolicitudVinculacionPuntoRecoleccionException(String mensaje) {
        super(mensaje);
    }

    public SolicitudVinculacionPuntoRecoleccionException(String message, String codigoError) {
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
