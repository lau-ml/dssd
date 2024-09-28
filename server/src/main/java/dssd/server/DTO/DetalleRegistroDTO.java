package dssd.server.DTO;

import dssd.server.model.DetalleRegistro;

public class DetalleRegistroDTO {
    private Long id;
    private Long idRegistroRecoleccion;
    private int cantidadRecolectada;
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

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdRegistroRecoleccion() {
        return idRegistroRecoleccion;
    }

    public void setIdRegistroRecoleccion(Long idRegistroRecoleccion) {
        this.idRegistroRecoleccion = idRegistroRecoleccion;
    }

    public int getCantidadRecolectada() {
        return cantidadRecolectada;
    }

    public void setCantidadRecolectada(int cantidadRecolectada) {
        this.cantidadRecolectada = cantidadRecolectada;
    }

    public MaterialDTO getMaterial() {
        return material;
    }

    public void setMaterial(MaterialDTO material) {
        this.material = material;
    }

    public UbicacionDTO getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(UbicacionDTO ubicacion) {
        this.ubicacion = ubicacion;
    }
}
