package dssd.server.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    Optional<List<RegistroRecoleccion>> findByRecolectorAndFechaRecoleccionBetweenOrderByFechaRecoleccion(
            Usuario recolector,
            LocalDate startDate,
            LocalDate endDate
    );
    Optional<RegistroRecoleccion> findByRecolectorAndCompletadoFalse(Usuario recolector);

    Optional<RegistroRecoleccion> findByRecolectorAndCompletadoTrueAndVerificadoFalse(Usuario recolector);

    Optional<RegistroRecoleccion> findTopByRecolectorIdAndCompletadoTrueAndVerificadoFalseOrderByFechaRecoleccionDesc(
            Long irRecolector);

    Optional<RegistroRecoleccion> findByRecolectorAndCompletadoFalseAndVerificadoFalse(Usuario recolector);
}
