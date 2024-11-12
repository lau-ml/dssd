package dssd.server.DTO;

import dssd.server.model.DetalleRegistro;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DetalleRegistroDTO {
    // Getters y setters
    private Long id;
    private Long idUsuario;
    private Long idRegistroRecoleccion;
    private int cantidadRecolectada;
    private int cantidadRecibida;
    private MaterialDTO material;
    private UbicacionDTO ubicacion;

    public DetalleRegistroDTO() {

    }

    public DetalleRegistroDTO(DetalleRegistro detalleRegistro) {
        this.id = detalleRegistro.getId();
        this.idRegistroRecoleccion = detalleRegistro.getRegistroRecoleccion().getId();
        this.cantidadRecolectada = detalleRegistro.getCantidadRecolectada();
        this.material = new MaterialDTO(detalleRegistro.getMaterial());
        this.ubicacion = new UbicacionDTO(detalleRegistro.getUbicacion());
    }

    public boolean validar() {
        return this.cantidadRecolectada > 0 && this.material != null && this.ubicacion != null;
    }
}
