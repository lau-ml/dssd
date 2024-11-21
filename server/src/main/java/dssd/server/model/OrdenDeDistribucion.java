package dssd.server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class OrdenDeDistribucion {
    // Getters y setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deposito;

    private String proceso;

    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "centro_recoleccion_id")
    private CentroRecoleccion centroRecoleccion;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;

    @Enumerated(EnumType.STRING)
    private EstadoOrden estado;

    public enum EstadoOrden {
        PENDIENTE_DE_ACEPTAR,
        ACEPTADA,
        RECHAZADO,
        EN_PREPARACION,
        PREPARADO,
        ENVIADO
    }

    public OrdenDeDistribucion() {
    }

    public OrdenDeDistribucion(String deposito, String proceso, Integer cantidad, CentroRecoleccion centroRecoleccion,
            Material material, EstadoOrden estado) {
        this.deposito = deposito;
        this.proceso = proceso;
        this.cantidad = cantidad;
        this.centroRecoleccion = centroRecoleccion;
        this.material = material;
        this.estado = estado;
    }

}
