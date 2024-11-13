package dssd.apisorteokubernetes.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class SorteoModel {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDate fechaCreacion;


    private LocalDate fechaSorteo;

    @OneToOne
    private InscripcionModel inscripcionGanadora;

    @OneToMany
    private List<InscripcionModel> inscripciones;

}
