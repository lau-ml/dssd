package dssd.server.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TareaBonita {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String caseId;

    private String rootCaseId;

    private String id_tarea_bonita;

    private Long ordenDeDistribucionId;

    @Enumerated(EnumType.STRING)
    private OrdenDeDistribucion.EstadoOrden estado;

    private String id_user_bonita;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private RegistroRecoleccion registroRecoleccion;

    @CreationTimestamp
    private LocalDate fechaCreacion;

    @LastModifiedDate
    private LocalDate fechaModificacion;



}
