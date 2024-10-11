package dssd.apiecocycle.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

@Setter
@Getter
@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String materiales;
    @CreationTimestamp
    private LocalDate fecha;
    private int cantidad;

    @ManyToOne
    @JoinColumn(name = "deposito_global_id")
    private DepositoGlobal depositoGlobal;

    public Pedido() {
    }

    public Pedido(String materiales, int cantidad, DepositoGlobal depositoGlobal) {
        this.materiales = materiales;
        this.cantidad = cantidad;
        this.depositoGlobal = depositoGlobal;
    }

}
