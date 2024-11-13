package dssd.apisorteokubernetes.repositories;

import dssd.apisorteokubernetes.models.InscripcionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InscripcionRepository extends JpaRepository<InscripcionModel, Long> {
    @Query("SELECT i.numeroSorteo FROM InscripcionModel i ORDER BY i.numeroSorteo DESC")
    Long findLatestNumeroSorteo();
}
