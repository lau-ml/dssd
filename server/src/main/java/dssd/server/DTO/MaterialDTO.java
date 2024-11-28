package dssd.server.DTO;

import dssd.server.model.Material;
import lombok.Getter;

@Getter
public class MaterialDTO {
    // Getters y setters
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;

    public MaterialDTO() {

    }

    public MaterialDTO(Material material) {
        this.id = material.getId();
        this.nombre = material.getNombre();
        this.descripcion = material.getDescripcion();
        this.precio = material.getPrecio();
    }

}
