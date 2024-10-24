package dssd.apiecocycle.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import dssd.apiecocycle.model.Material;
import dssd.apiecocycle.model.Pedido;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByMaterial(Material material);

    List<Pedido> findByMaterialAndAbastecido(Material material, boolean abastecido);

    Optional<Pedido> findByIdAndDepositoGlobal_Id(Long id, Long id1);

    List<Pedido> findAByMaterialAndDepositoGlobal_Id(Material material, Long id);

    List<Pedido> findByDepositoGlobal_Id(Long id);

    @Query("SELECT p FROM Pedido p WHERE " +
            "(:materialNombre IS NULL OR p.material.nombre LIKE %:materialNombre%)" +
            "AND (:abastecido IS NULL OR p.abastecido = :abastecido) " +
            "AND (cast(:fechaPedido as localdate) IS NULL OR p.fecha = :fechaPedido) " +
            "AND (cast(:lastUpdate as localdatetime) IS NULL OR p.lastUpdate = :lastUpdate) " +
            "AND (:cantidad IS NULL OR p.cantidad = :cantidad)")
    Page<Pedido> findAllByParams(Pageable pageable,
                                 @Param("materialNombre")String materialNombre,
                                 @Param("abastecido") Boolean abastecido,
                                 @Param("fechaPedido")LocalDate fechaPedido,
                                 @Param("lastUpdate") LocalDateTime lastUpdate,
                                 @Param("cantidad") Integer cantidad);

    @Query(
            "SELECT p FROM Pedido p WHERE " +
                    "(:materialNombre IS NULL OR p.material.nombre LIKE %:materialNombre%) " +
                    "AND (:abastecido IS NULL OR p.abastecido = :abastecido) " +
                    "AND (cast(:fechaPedido as localdate) IS NULL OR p.fecha = :fechaPedido) " +
                    "AND (cast(:lastUpdate as localdatetime) IS NULL OR p.lastUpdate = :lastUpdate) " +
                    "AND (:cantidad IS NULL OR p.cantidad = :cantidad) " +
                    "AND p.depositoGlobal.id = :id"
    )
    Page<Pedido> findAllByParamsAndDepositoGlobal_Id(
            Pageable pageable,
            @Param("materialNombre") String materialNombre,
            @Param("abastecido") Boolean abastecido,
            @Param("fechaPedido") LocalDate fechaPedido,
            @Param("lastUpdate") LocalDateTime lastUpdate,
            @Param("cantidad") Integer cantidad,
            @Param("id") Long id
    );

    List<Pedido> findAllByMaterial_IdAndDepositoGlobal_Id(Long id, Long id1);
}
