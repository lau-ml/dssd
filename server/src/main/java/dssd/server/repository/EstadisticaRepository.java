package dssd.server.repository;

import dssd.server.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import dssd.server.model.Estadistica;

public interface EstadisticaRepository extends JpaRepository<Estadistica, Long> {

    Estadistica findByRecolector(Usuario usuario);
}
