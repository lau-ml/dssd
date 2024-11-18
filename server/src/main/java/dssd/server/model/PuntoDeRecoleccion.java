package dssd.server.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class PuntoDeRecoleccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreEstablecimiento;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String numeroContacto;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @ManyToMany(mappedBy = "puntosDeRecoleccion")
    private List<Usuario> usuarios = new ArrayList<>();

    public PuntoDeRecoleccion(String nombreEstablecimiento, String direccion, String numeroContacto) {
        this.nombreEstablecimiento = nombreEstablecimiento;
        this.direccion = direccion;
        this.numeroContacto = numeroContacto;
    }
}