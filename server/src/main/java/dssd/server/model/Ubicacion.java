package dssd.server.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Ubicacion {
    // Getters y setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreEstablecimiento;

    @ManyToOne
    private Zona zona;

    public Ubicacion(String nombreEstablecimiento) {
        this.nombreEstablecimiento = nombreEstablecimiento;
    }

}
