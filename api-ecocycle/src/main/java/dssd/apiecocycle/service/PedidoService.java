package dssd.apiecocycle.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dssd.apiecocycle.model.CentroDeRecepcion;
import dssd.apiecocycle.model.EstadoOrden;
import dssd.apiecocycle.model.Material;
import dssd.apiecocycle.model.Orden;
import dssd.apiecocycle.model.Pedido;
import dssd.apiecocycle.repository.PedidoRepository;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private OrdenSerive ordenService;

    public Optional<Pedido> getPedidoById(Long id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> getOrdersByMaterial(Material material) {
        return pedidoRepository.findByMaterial(material);
    }

    public List<Pedido> getOrdersByMaterialAndAbastecido(Material material, boolean abastecido) {
        return pedidoRepository.findByMaterialAndAbastecido(material, abastecido);
    }

    public Orden generarOrden(Long pedidoId, Long materialId, int cantidad, CentroDeRecepcion centroDeRecepcion) {
        Optional<Pedido> pedidoOptional = getPedidoById(pedidoId);
        if (!pedidoOptional.isPresent()) {
            throw new RuntimeException("Pedido no encontrado");
        }

        Pedido pedido = pedidoOptional.get();

        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad de la orden debe ser mayor a cero");
        }

        int cantidadFaltante = pedido.getCantidad() - pedido.getCantidadAbastecida();
        if (cantidad > cantidadFaltante) {
            throw new RuntimeException("La cantidad de la orden no puede ser mayor que la cantidad faltante");
        }

        Material material = materialService.getMaterialById(materialId);
        Orden nuevaOrden = new Orden();
        nuevaOrden.setMaterial(material);
        nuevaOrden.setEstado(EstadoOrden.EN_ESPERA);
        nuevaOrden.setCantidad(cantidad);
        nuevaOrden.setCentroDeRecepcion(centroDeRecepcion);
        nuevaOrden.setPedido(pedido);

        ordenService.saveOrden(nuevaOrden);

        pedido.setCantidadAbastecida(pedido.getCantidadAbastecida() + cantidad);
        if (pedido.getCantidadAbastecida() >= pedido.getCantidad()) {
            pedido.setAbastecido(true);
        }

        savePedido(pedido);

        return nuevaOrden;
    }

    public Pedido savePedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }
}