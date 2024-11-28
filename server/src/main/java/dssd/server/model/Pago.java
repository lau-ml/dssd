package dssd.server.model;

import java.time.LocalDate;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Setter
@Getter
@Entity
public class Pago {

    // Getters y setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_registro_recoleccion")
    private RegistroRecoleccion registroRecoleccion;

    @Column(nullable = false)
    private Double monto;

    @CreationTimestamp
    private LocalDate fechaEmision;

    private LocalDate fechaPago;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoPago estado = EstadoPago.PENDIENTE;

    public enum EstadoPago {
        PENDIENTE,
        PAGADO
    }

    public void setEstado(EstadoPago nuevoEstado) {
        this.estado = nuevoEstado;
        if (nuevoEstado == EstadoPago.PAGADO) {
            this.fechaPago = LocalDate.now();
        }
    }
}
