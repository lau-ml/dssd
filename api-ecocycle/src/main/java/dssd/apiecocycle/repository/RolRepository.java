package dssd.apiecocycle.repository;

import dssd.apiecocycle.model.Rol;
import dssd.apiecocycle.model.Centro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    // Buscar un rol por nombre
    Optional<Rol> findByNombre(String nombre);

    // Buscar un rol por id Centro
    Optional<Rol> findByCentroAndNombre(List<Centro> centro, String nombre);
}
