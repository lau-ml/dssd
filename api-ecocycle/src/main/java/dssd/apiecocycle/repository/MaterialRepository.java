package dssd.apiecocycle.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dssd.apiecocycle.model.Material;

public interface MaterialRepository extends JpaRepository<Material, Long> {

    Optional<Material> findByNombre(String string);

}
