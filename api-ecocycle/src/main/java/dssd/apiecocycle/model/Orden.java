package dssd.apiecocycle.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

@Setter
@Getter
@Entity
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String materiales;
    @CreationTimestamp
    private LocalDate fecha;
    private String estado;
    private int cantidad;

    @ManyToOne
    @JoinColumn(name = "centro_de_recepcion_id")
    private CentroDeRecepcion centroDeRecepcion;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    public Orden() {

    }

    public Orden(String materiales, String estado, int cantidad, CentroDeRecepcion centroDeRecepcion, Pedido pedido) {
        this.materiales = materiales;
        this.estado = estado;
        this.cantidad = cantidad;
        this.centroDeRecepcion = centroDeRecepcion;
        this.pedido = pedido;
    }

}
