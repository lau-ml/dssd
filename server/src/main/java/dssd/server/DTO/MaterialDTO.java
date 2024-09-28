package dssd.server.DTO;

import dssd.server.model.Material;

public class MaterialDTO {
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

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
