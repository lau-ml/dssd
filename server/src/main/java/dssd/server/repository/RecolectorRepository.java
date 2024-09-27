package dssd.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dssd.server.model.Recolector;

@Repository
public interface RecolectorRepository extends JpaRepository<Recolector, Long> {
}
