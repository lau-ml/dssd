package dssd.apiecocycle.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dssd.apiecocycle.model.Material;

public interface MaterialRepository extends JpaRepository<Material, Long> {

}
