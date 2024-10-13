package dssd.apiecocycle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dssd.apiecocycle.model.EstadoOrden;
import dssd.apiecocycle.model.Material;
import dssd.apiecocycle.model.Orden;
import dssd.apiecocycle.model.Pedido;

public interface OrdenRepository extends JpaRepository<Orden, Long> {

    List<Orden> findByPedido(Optional<Pedido> pedido);

    List<Orden> findByPedidoId(Long pedidoId);

    List<Orden> findByMaterialAndEstado(Material material, EstadoOrden estado);

}
