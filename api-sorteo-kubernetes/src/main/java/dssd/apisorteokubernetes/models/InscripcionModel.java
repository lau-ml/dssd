package dssd.apisorteokubernetes.models;

import jakarta.persistence.*;
import lombok.*;

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
    private Long numeroSorteo;

    @ManyToOne
    private SorteoModel sorteo;

}
