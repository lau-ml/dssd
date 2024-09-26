package dssd.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dssd.server.model.Material;

public interface MaterialRepository extends JpaRepository<Material, Long> {

}
