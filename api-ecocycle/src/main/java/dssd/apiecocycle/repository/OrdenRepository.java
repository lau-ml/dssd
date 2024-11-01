package dssd.apiecocycle.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import dssd.apiecocycle.model.EstadoOrden;
import dssd.apiecocycle.model.Material;
import dssd.apiecocycle.model.Orden;
import dssd.apiecocycle.model.Pedido;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrdenRepository extends JpaRepository<Orden, Long> {

    List<Orden> findByPedido(Optional<Pedido> pedido);

    List<Orden> findByPedidoId(Long pedidoId);

    List<Orden> findByMaterialAndEstado(Material material, EstadoOrden estado);

    Optional<Orden> findByIdAndPedido_DepositoGlobal_Id(Long ordenId, Long centroId);

    Optional<Orden> findByIdAndCentroDeRecepcion_Id(Long id, Long id1);

    List<Orden> findByCentroDeRecepcion_Id(Long id);

    @Query("SELECT o FROM Orden o WHERE " +
            "(:cantidad IS NULL OR o.cantidad = :cantidad) " +
            "AND (:materialName IS NULL OR o.material.nombre ILIKE CONCAT('%', :materialName, '%')) " +
            "AND (:estado IS NULL OR o.estado = :estado) " +
            "AND (cast(:fechaOrden as localdate) IS NULL OR o.fecha = :fechaOrden) " +
            "AND (cast(:lastUpdate as localdate) IS NULL OR o.lastUpdate = :lastUpdate) " +
            "AND (:idCentro IS NULL OR o.centroDeRecepcion.id = :idCentro) " +
            "AND (:globalId IS NULL OR o.pedido.depositoGlobal.id = :globalId)")
    Page<Orden> findMyOrders(
            @Param("cantidad") Integer cantidad,
            @Param("globalId") Long globalId,
            @Param("idCentro") Long idCentro,
            @Param("materialName") String materialName,
            @Param("estado") EstadoOrden estado,
            @Param("fechaOrden") LocalDate fechaOrden,
            @Param("lastUpdate") LocalDate lastUpdate,
            Pageable pageable
    );

    @Query(
            "SELECT o FROM Orden o WHERE " +
                    "(:pedido IS NULL OR o.pedido = :pedido) " +
                    "AND (:cantidad IS NULL OR o.cantidad = :cantidad) " +
                    "AND (:materialName IS NULL OR o.material.nombre ILIKE CONCAT('%', :materialName, '%')) " +
                    "AND (:estado IS NULL OR o.estado = :estado) " +
                    "AND (cast(:fechaOrden as localdate) IS NULL OR o.fecha = :fechaOrden)" +
                    "AND (cast(:lastUpdate as localdate) IS NULL OR o.lastUpdate = :lastUpdate)"
    )
    Page<Orden> findByPedidoAndArgs(Pedido pedido, Integer cantidad, String materialName, EstadoOrden estado, LocalDate fechaOrden,LocalDate lastUpdate, PageRequest of);

    List<Orden> findByPedidoIdAndEstado(Long pedidoId, EstadoOrden estado);

    Optional<Orden> findByPedidoIdAndCentroDeRecepcion_IdAndEstado(Long pedidoId, Long id, EstadoOrden estado);
}
