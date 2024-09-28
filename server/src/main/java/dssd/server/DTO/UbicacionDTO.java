package dssd.server.DTO;

import dssd.server.model.Ubicacion;

public class UbicacionDTO {
    private Long id;
    private String nombreEstablecimiento;

    public UbicacionDTO() {

    }

    public UbicacionDTO(Ubicacion ubicacion) {
        this.id = ubicacion.getId();
        this.nombreEstablecimiento = ubicacion.getNombreEstablecimiento();
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreEstablecimiento() {
        return nombreEstablecimiento;
    }

    public void setNombreEstablecimiento(String nombreEstablecimiento) {
        this.nombreEstablecimiento = nombreEstablecimiento;
    }
}
