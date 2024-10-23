package dssd.apiecocycle.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Material material;

    @CreationTimestamp
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    private EstadoOrden estado;

    private int cantidad;

    @ManyToOne
    @JoinColumn(name = "centro_de_recepcion_id")
    private CentroDeRecepcion centroDeRecepcion;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;


    private int cantidadAceptada;

    public Orden(Material material, EstadoOrden estado, int cantidad, CentroDeRecepcion centroDeRecepcion,
            Pedido pedido) {
        this.material = material;
        this.estado = estado;
        this.cantidad = cantidad;
        this.centroDeRecepcion = centroDeRecepcion;
        this.pedido = pedido;
    }


    public boolean is_pending() {
        return this.getEstado().equals(EstadoOrden.PENDIENTE);
    }

    public boolean is_accepted() {
        return this.getEstado().equals(EstadoOrden.ACEPTADO);
    }

    public boolean is_rejected() {
        return this.getEstado().equals(EstadoOrden.RECHAZADO);
    }

    public boolean is_delivered() {
        return this.getEstado().equals(EstadoOrden.ENTREGADO);
    }

    public boolean is_preparing() {
        return this.getEstado().equals(EstadoOrden.PREPARANDO);
    }
    public boolean is_prepared() {
        return this.getEstado().equals(EstadoOrden.PREPARADA);
    }

    public boolean is_sent() {
        return this.getEstado().equals(EstadoOrden.ENVIADA);
    }
}
