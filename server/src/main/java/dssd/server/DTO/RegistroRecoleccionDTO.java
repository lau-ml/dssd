package dssd.server.DTO;

import java.util.ArrayList;
import java.util.List;

import dssd.server.model.DetalleRegistro;
import dssd.server.model.RegistroRecoleccion;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegistroRecoleccionDTO {
    // Getters y setters
    private Long id;
    private Long idRecolector;
    private List<DetalleRegistroDTO> detalleRegistros;

    public RegistroRecoleccionDTO() {
    }

    public RegistroRecoleccionDTO(RegistroRecoleccion registroRecoleccion) {
        this.id = registroRecoleccion.getId();
        this.idRecolector = registroRecoleccion.getRecolector().getId();
        this.detalleRegistros = obtenerDetalleRegistrosDTO(registroRecoleccion.getDetalleRegistros());
    }

    private List<DetalleRegistroDTO> obtenerDetalleRegistrosDTO(List<DetalleRegistro> detalleRegistros) {
        List<DetalleRegistroDTO> detalleRegistrosDTO = new ArrayList<>();
        if (detalleRegistros != null) {
            for (DetalleRegistro detalleRegistro : detalleRegistros) {
                DetalleRegistroDTO detalleRegistroDTO = new DetalleRegistroDTO(detalleRegistro);
                detalleRegistrosDTO.add(detalleRegistroDTO);
            }
        }
        return detalleRegistrosDTO;

    }
}
