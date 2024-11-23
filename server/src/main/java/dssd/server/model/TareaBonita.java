package dssd.server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;

@Data
@Entity
public class TareaBonita {


    @Id
    private Long id;

    private String caseId;

    private String id_tarea_bonita;

    private String state;

    @ManyToOne
    private Usuario usuario;

    @OneToOne
    private RegistroRecoleccion registroRecoleccion;

    @CreationTimestamp
    private LocalDate fechaCreacion;

    @LastModifiedDate
    private LocalDate fechaModificacion;

    @ManyToOne
    private OrdenDeDistribucion ordenDeDistribucion;

}
