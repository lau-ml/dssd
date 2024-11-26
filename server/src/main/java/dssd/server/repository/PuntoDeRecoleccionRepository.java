package dssd.server.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dssd.server.model.PuntoDeRecoleccion;
import dssd.server.model.Usuario;

public interface PuntoDeRecoleccionRepository extends JpaRepository<PuntoDeRecoleccion, Long> {

    Page<PuntoDeRecoleccion> findByUsuariosAndIsDeletedFalse(Usuario usuario, Pageable pageable);

    Page<PuntoDeRecoleccion> findByUsuariosNotContainsAndIsDeletedFalse(Usuario usuario, Pageable pageable);

    Page<PuntoDeRecoleccion> findByUsuariosNotContainsAndIsDeletedFalseAndNombreEstablecimientoContainingIgnoreCaseOrDireccionContainingIgnoreCase(
            Usuario usuario, String nombre, String direccion, Pageable pageable);

    Page<PuntoDeRecoleccion> findByIsDeletedFalseAndNombreEstablecimientoContainingIgnoreCaseOrDireccionContainingIgnoreCase(
            String nombre, String direccion, Pageable pageable);

    Page<PuntoDeRecoleccion> findByIsDeletedFalse(Pageable pageable);

    boolean existsByNombreEstablecimientoAndIdNot(String nombreEstablecimiento, Long id);

    Optional<PuntoDeRecoleccion> findByNombreEstablecimientoIgnoreCase(String nombreEstablecimiento);

    @Query("SELECT p FROM PuntoDeRecoleccion p " +
            "WHERE p.isDeleted = false " +
            "AND :usuario MEMBER OF p.usuarios " +
            "AND (LOWER(p.nombreEstablecimiento) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.direccion) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<PuntoDeRecoleccion> buscarPuntosPorUsuarioYFiltro(
            @Param("usuario") Usuario usuario,
            @Param("search") String search,
            Pageable pageable);

}
