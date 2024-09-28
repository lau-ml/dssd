package dssd.server.DTO;

import java.util.ArrayList;
import java.util.List;

import dssd.server.model.DetalleRegistro;
import dssd.server.model.RegistroRecoleccion;

public class RegistroRecoleccionDTO {
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

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdRecolector() {
        return idRecolector;
    }

    public void setIdRecolector(Long idRecolector) {
        this.idRecolector = idRecolector;
    }

    public List<DetalleRegistroDTO> getDetalleRegistros() {
        return detalleRegistros;
    }

    public void setDetalleRegistros(List<DetalleRegistroDTO> detalleRegistros) {
        this.detalleRegistros = detalleRegistros;
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
