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

    @ManyToOne
    private Material material;
    @CreationTimestamp
    private LocalDate fecha;
    private int cantidad;

    @ManyToOne
    @JoinColumn(name = "deposito_global_id")
    private DepositoGlobal depositoGlobal;

    public Pedido() {
    }

    public Pedido(Material material, int cantidad, DepositoGlobal depositoGlobal) {
        this.material = material;
        this.cantidad = cantidad;
        this.depositoGlobal = depositoGlobal;
    }

}
