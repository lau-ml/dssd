package dssd.server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class OrdenDeDistribucion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deposito;

    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "centro_recoleccion_id")
    private CentroRecoleccion centroRecoleccion;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeposito() {
        return deposito;
    }

    public void setDeposito(String deposito) {
        this.deposito = deposito;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public CentroRecoleccion getCentroRecoleccion() {
        return centroRecoleccion;
    }

    public void setCentroRecoleccion(CentroRecoleccion centroRecoleccion) {
        this.centroRecoleccion = centroRecoleccion;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}