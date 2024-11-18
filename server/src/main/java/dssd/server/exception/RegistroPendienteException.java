package dssd.server.exception;

public class RegistroPendienteException extends RuntimeException {
    private String codigoError;

    public RegistroPendienteException(String mensaje) {
        super(mensaje);
    }

    public RegistroPendienteException(String message, String codigoError) {
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
