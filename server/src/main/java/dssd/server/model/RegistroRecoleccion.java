package dssd.server.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class RegistroRecoleccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_recolector")
    private Usuario recolector;

    @Column(nullable = false)
    private Long idCentroRecoleccion;

    @CreationTimestamp
    private Date fechaRecoleccion;

    @Column(nullable = false)
    private boolean completado = false;
    @Column(nullable = false)
    private boolean verificado = false;
    @OneToOne(mappedBy = "registroRecoleccion")
    private Pago pago;

    @OneToMany(mappedBy = "registroRecoleccion")
    private List<DetalleRegistro> detalleRegistros;

    public RegistroRecoleccion() {
    }

}
