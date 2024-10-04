package dssd.server.model;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@DiscriminatorValue("recolector")
public class Recolector extends Usuario {

    @OneToMany(mappedBy = "recolector")
    private List<Notificacion> notificaciones = new ArrayList<>();

    @OneToMany(mappedBy = "recolector")
    private List<Estadistica> estadisticas;

    @OneToMany(mappedBy = "recolector")
    private List<RegistroRecoleccion> registroRecolecciones;

    public Recolector() {
    }



}
