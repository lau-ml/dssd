package dssd.server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Empleado extends Usuario{
    @Column(nullable = false)
    private String sector;
    public Empleado() {
    }

}
