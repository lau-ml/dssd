package dssd.apiecocycle.DTO;

import dssd.apiecocycle.model.Material;
import lombok.Getter;

@Getter
public class MaterialDTO {
    // Getters y setters
    private Long id;
    private String nombre;
    private String descripcion;

    public MaterialDTO() {

    }

    public MaterialDTO(Material material) {
        this.id = material.getId();
        this.nombre = material.getNombre();
        this.descripcion = material.getDescripcion();
    }

}
