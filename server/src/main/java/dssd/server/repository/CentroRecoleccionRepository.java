package dssd.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dssd.server.model.CentroRecoleccion;

public interface CentroRecoleccionRepository extends JpaRepository<CentroRecoleccion, Long> {

    CentroRecoleccion findByNombre(String string);

    CentroRecoleccion findByEmail(String centroRecoleccionEmail);
}
