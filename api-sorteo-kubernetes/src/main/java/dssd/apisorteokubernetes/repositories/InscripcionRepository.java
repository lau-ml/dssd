package dssd.apisorteokubernetes.repositories;

import dssd.apisorteokubernetes.models.InscripcionModel;
import dssd.apisorteokubernetes.models.SorteoModel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscripcionRepository extends JpaRepository<InscripcionModel, Long> {
    @Query("SELECT i.numeroInscripcionSorteo FROM InscripcionModel i where i.sorteo = :sorteo ORDER BY i.numeroInscripcionSorteo DESC LIMIT 1")
    Optional<Long> findLatestNumeroSorteoOfActualSorteo(@Param("sorteo") SorteoModel sorteo);
    @Query("SELECT i FROM InscripcionModel i WHERE i.sorteo = :sorteo ORDER BY function('RANDOM') ASC LIMIT 1")
    Optional<InscripcionModel> findRandomInscripcionBySorteo(@Param("sorteo") SorteoModel sorteo);

    Optional<InscripcionModel> findByCentroAndSorteo(Long centro, SorteoModel sorteo);
}
