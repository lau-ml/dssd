package dssd.server.exception;

public class MaterialYaExistenteException extends RuntimeException {
    public MaterialYaExistenteException(String nombre) {
        super("El material con el nombre '" + nombre + "' ya existe.");
    }
}
