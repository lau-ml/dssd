package dssd.server.repository;

import dssd.server.model.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermisoRepository extends JpaRepository<Permiso, Long> {
    // Buscar un permiso por nombre
    Optional<Permiso> findByNombre(String nombre);
}
