package dssd.apiecocycle.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Setter
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
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

    @LastModifiedDate
    private LocalDate lastUpdate;

    @LastModifiedBy
    private String lastModifiedBy;

    public Pedido() {
    }

    public Pedido(Material material, int cantidad, DepositoGlobal depositoGlobal) {
        this.material = material;
        this.cantidad = cantidad;
        this.depositoGlobal = depositoGlobal;
    }

    public boolean isAbastecido() {
        return this.abastecido;
    }

    public void addCantidadAbastecida(int cantidad) {
        this.cantidadAbastecida += cantidad;
    }
}
