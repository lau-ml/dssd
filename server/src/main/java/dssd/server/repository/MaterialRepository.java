package dssd.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dssd.server.model.Material;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    boolean existsByNombreAndIdNot(String nombre, Long id);

    Page<Material> findAllByIsDeletedFalse(Pageable pageable);

    Page<Material> findByNombreContainingIgnoreCaseAndIsDeletedFalse(String search, Pageable pageable);

    Material findByNombreIgnoreCase(String nombre);
}
