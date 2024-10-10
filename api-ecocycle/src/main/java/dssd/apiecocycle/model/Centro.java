package dssd.apiecocycle.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_centro", discriminatorType = DiscriminatorType.STRING)
public abstract class Centro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String telefono;
    private String direccion;

    // Getters and Setters
}
