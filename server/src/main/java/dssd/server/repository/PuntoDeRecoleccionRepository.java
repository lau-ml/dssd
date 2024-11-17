package dssd.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import dssd.server.model.PuntoDeRecoleccion;
import dssd.server.model.Usuario;

public interface PuntoDeRecoleccionRepository extends JpaRepository<PuntoDeRecoleccion, Long> {

    Page<PuntoDeRecoleccion> findByUsuariosAndNombreEstablecimientoContainingIgnoreCaseOrDireccionContainingIgnoreCaseAndIsDeletedFalse(
            Usuario usuario, String nombre, String direccion, Pageable pageable);

    Page<PuntoDeRecoleccion> findByUsuariosAndIsDeletedFalse(Usuario usuario, Pageable pageable);

}
