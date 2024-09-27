package dssd.server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class DetalleRegistro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cantidadRecolectada;

    private Integer cantidadRecibida;

    @ManyToOne
    @JoinColumn(name = "id_registro_recoleccion")
    private RegistroRecoleccion registroRecoleccion;

    @ManyToOne
    @JoinColumn(name = "id_ubicacion")
    private Ubicacion ubicacion;

    @ManyToOne
    @JoinColumn(name = "id_material")
    private Material material;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidadRecolectada() {
        return cantidadRecolectada;
    }

    public void setCantidadRecolectada(Integer cantidadRecolectada) {
        this.cantidadRecolectada = cantidadRecolectada;
    }

    public Integer getCantidadRecibida() {
        return cantidadRecibida;
    }

    public void setCantidadRecibida(Integer cantidadRecibida) {
        this.cantidadRecibida = cantidadRecibida;
    }

    public RegistroRecoleccion getRegistroRecoleccion() {
        return registroRecoleccion;
    }

    public void setRegistroRecoleccion(RegistroRecoleccion registroRecoleccion) {
        this.registroRecoleccion = registroRecoleccion;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}