package dssd.apiecocycle.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
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

    private Long cantidad;

    @ManyToOne
    @JoinColumn(name = "centro_de_recepcion_id")
    private CentroDeRecepcion centroDeRecepcion;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @LastModifiedDate
    private LocalDate lastUpdate;

    @LastModifiedBy
    private String lastModifiedBy;

    private Long cantidadAceptada;

    public Orden(Material material, EstadoOrden estado, Long cantidad, CentroDeRecepcion centroDeRecepcion,
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
        return this.getEstado().equals(EstadoOrden.ACEPTADA);
    }

    public boolean is_rejected() {
        return this.getEstado().equals(EstadoOrden.RECHAZADA);
    }

    public boolean is_delivered() {
        return this.getEstado().equals(EstadoOrden.ENTREGADA);
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
