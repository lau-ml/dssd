package dssd.server.repository;

import dssd.server.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import dssd.server.model.Estadistica;

import java.util.List;
import java.util.Optional;

public interface EstadisticaRepository extends JpaRepository<Estadistica, Long> {

    Estadistica findByRecolector(Usuario usuario);

    Optional<Estadistica> findFirstByRecolectorOrderByFechaCreacionDesc(Usuario usuario);
}
