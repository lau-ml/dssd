package dssd.apisorteokubernetes.repositories;

import dssd.apisorteokubernetes.models.SorteoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SorteoRepository extends JpaRepository<SorteoModel,Long> {

   SorteoModel findTopByActivoTrueOrderByFechaSorteoDesc();

   SorteoModel findTopByActivoFalseOrderByFechaSorteoDesc();
}
