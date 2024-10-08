package dssd.server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Ubicacion {
    // Getters y setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreEstablecimiento;

    public Ubicacion() {

    }

    public Ubicacion(String nombreEstablecimiento) {
        this.nombreEstablecimiento = nombreEstablecimiento;
    }

}
