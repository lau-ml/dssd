package dssd.server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@EqualsAndHashCode(of = "id")
public class DetalleRegistro {

    // Getters y setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cantidadRecolectada;

    private Integer cantidadRecibida;

    @ManyToOne
    @JoinColumn(name = "id_registro_recoleccion")
    private RegistroRecoleccion registroRecoleccion;

    @ManyToOne
    @JoinColumn(name = "id_punto_recoleccion")
    private PuntoDeRecoleccion puntoRecoleccion;

    @ManyToOne
    @JoinColumn(name = "id_material")
    private Material material;

}
