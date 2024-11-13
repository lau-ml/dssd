package dssd.apisorteokubernetes.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Getter
@Setter
public class InscripcionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long centro;

    @Column(nullable = false)
    private Long numeroInscripcionSorteo;

    @ManyToOne
    private SorteoModel sorteo;

    @CreationTimestamp
    private LocalDate fechaInscripcion;

}
