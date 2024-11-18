package dssd.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dssd.server.model.CentroRecoleccion;

import java.util.List;

public interface CentroRecoleccionRepository extends JpaRepository<CentroRecoleccion, Long> {

}
