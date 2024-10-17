package dssd.apiecocycle.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

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
    private int cantidadAbastecida = 0;
    private Boolean abastecido = false;

    @OneToMany(mappedBy = "pedido")
    private List<Orden> ordenes;

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
