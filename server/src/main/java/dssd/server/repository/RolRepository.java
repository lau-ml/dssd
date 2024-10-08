package dssd.server.repository;

import dssd.server.model.Rol;
import dssd.server.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    // Buscar un rol por nombre
    Optional<Rol> findByNombre(String nombre);

    // Buscar un rol por id usuario
    Optional<Rol> findByUsuarioAndNombre(List<Usuario> usuario, String nombre);
}
