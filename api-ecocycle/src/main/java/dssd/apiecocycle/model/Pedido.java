package dssd.apiecocycle.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String materiales;
    private LocalDate fecha;
    private int cantidad;

    @ManyToOne
    @JoinColumn(name = "deposito_global_id")
    private DepositoGlobal depositoGlobal;

    // Getters and Setters
}
