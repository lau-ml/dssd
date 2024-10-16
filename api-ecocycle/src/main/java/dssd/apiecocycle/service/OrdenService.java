package dssd.apiecocycle.service;

import dssd.apiecocycle.DTO.OrdenDTO;
import dssd.apiecocycle.exceptions.CentroInvalidoException;
import dssd.apiecocycle.exceptions.EstadoOrdenException;
import dssd.apiecocycle.model.Centro;
import dssd.apiecocycle.model.EstadoOrden;
import dssd.apiecocycle.model.Orden;
import dssd.apiecocycle.model.Pedido;
import dssd.apiecocycle.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrdenService {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private CentroService centroService;

    @Autowired
    private PedidoService pedidoService;

    public Orden saveOrden(Orden orden) {
        return ordenRepository.save(orden);
    }

    public List<Orden> getOrdersByPedido(Optional<Pedido> pedido) {
        return ordenRepository.findByPedido(pedido);
    }

    public Orden getOrdenById(Long id) {
        Optional<Orden> orden = ordenRepository.findById(id);
        return orden.orElse(null);
    }

    public Orden updateOrden(Orden orden) {
        return ordenRepository.save(orden);
    }

    public List<Orden> getOrdenesPorPedidoId(Long pedidoId) {
        return ordenRepository.findByPedidoId(pedidoId);
    }

    public Orden getOrdenByIdAndDepositoGlobalId(Long ordenId, Long centroId) {
        return ordenRepository.findByIdAndPedido_DepositoGlobal_Id(ordenId, centroId).orElseThrow();
    }

    public Orden entregarOrden(Long id) throws CentroInvalidoException {
        Centro centro = centroService.recuperarCentro();
        Orden orden = getOrdenByIdAndDepositoGlobalId(id, centro.getId());
        if (orden.getEstado().equals(EstadoOrden.PENDIENTE)) {
            orden.setEstado(EstadoOrden.ENTREGADO);
            updateOrden(orden);
            pedidoService.updateCantSupplied(orden.getPedido(), orden.getCantidad());
            return orden;
        }
        throw new EstadoOrdenException("No se puede entregar la orden");
    }
}
