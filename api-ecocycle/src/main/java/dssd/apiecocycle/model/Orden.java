package dssd.apiecocycle.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String materiales;
    private LocalDate fecha;
    private String estado;
    private int cantidad;

    @ManyToOne
    @JoinColumn(name = "centro_de_recepcion_id")
    private CentroDeRecepcion centroDeRecepcion;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    // Getters and Setters
}
