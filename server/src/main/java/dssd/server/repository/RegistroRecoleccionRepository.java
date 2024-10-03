package dssd.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dssd.server.model.Recolector;
import dssd.server.model.RegistroRecoleccion;

public interface RegistroRecoleccionRepository extends JpaRepository<RegistroRecoleccion, Long> {
    // Buscar el último registro
    Optional<RegistroRecoleccion> findTopByRecolectorOrderByFechaRecoleccionDesc(Recolector recolector);

    // Buscar el último registro no completado
    Optional<RegistroRecoleccion> findTopByRecolectorAndCompletadoFalseOrderByFechaRecoleccionDesc(
            Recolector recolector);

    Optional<RegistroRecoleccion> findByRecolectorAndCompletadoFalse(Recolector recolector);

    Optional<RegistroRecoleccion> findByRecolectorAndCompletadoTrueAndVerificadoFalse(Recolector recolector);
}
