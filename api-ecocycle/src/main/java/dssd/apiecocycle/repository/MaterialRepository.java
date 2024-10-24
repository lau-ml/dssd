package dssd.apiecocycle.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import dssd.apiecocycle.model.Material;
import org.springframework.data.jpa.repository.Query;

public interface MaterialRepository extends JpaRepository<Material, Long> {

    Optional<Material> findByNombre(String string);

    @Query(
            value =
                    "SELECT c FROM Material c WHERE " +
                            "(:nombre IS NULL OR c.nombre LIKE CONCAT('%', :nombre, '%')) " +
                            "AND (:descripcion IS NULL OR c.descripcion LIKE CONCAT('%', :descripcion, '%'))"
    )
    Page<Material> findAll(String nombre, String descripcion, PageRequest of);
}
