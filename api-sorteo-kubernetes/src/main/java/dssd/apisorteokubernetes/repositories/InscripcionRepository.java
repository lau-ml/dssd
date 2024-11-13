package dssd.apisorteokubernetes.repositories;

import dssd.apisorteokubernetes.models.InscripcionModel;
import dssd.apisorteokubernetes.models.SorteoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscripcionRepository extends JpaRepository<InscripcionModel, Long> {
    @Query("SELECT i.numeroSorteo FROM InscripcionModel i ORDER BY i.numeroSorteo DESC")
    Long findLatestNumeroSorteo();
    @Query("SELECT i FROM InscripcionModel i WHERE i.sorteo.id = :sorteoId ORDER BY function('RANDOM') ASC LIMIT 1")    Optional<InscripcionModel> findRandomInscripcionBySorteo(@Param("sorteoId") Long sorteoId);
}
