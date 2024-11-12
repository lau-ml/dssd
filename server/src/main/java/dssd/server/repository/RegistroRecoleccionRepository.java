package dssd.server.repository;

import java.util.Optional;

import dssd.server.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import dssd.server.model.RegistroRecoleccion;

public interface RegistroRecoleccionRepository extends JpaRepository<RegistroRecoleccion, Long> {
    // Buscar el último registro
    Optional<RegistroRecoleccion> findTopByRecolectorOrderByFechaRecoleccionDesc(Usuario recolector);

    // Buscar el último registro no completado
    Optional<RegistroRecoleccion> findTopByRecolectorAndCompletadoFalseOrderByFechaRecoleccionDesc(
            Usuario recolector);

    Optional<RegistroRecoleccion> findByRecolectorAndCompletadoFalse(Usuario recolector);

    Optional<RegistroRecoleccion> findByRecolectorAndCompletadoTrueAndVerificadoFalse(Usuario recolector);

    Optional<RegistroRecoleccion> findTopByRecolectorIdAndCompletadoTrueAndVerificadoFalseOrderByFechaRecoleccionDesc(
            Long irRecolector);
}
