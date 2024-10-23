package dssd.apiecocycle.service;

import dssd.apiecocycle.DTO.OrdenDistribucionDTO;
import dssd.apiecocycle.exceptions.CantidadException;
import dssd.apiecocycle.exceptions.CentroInvalidoException;
import dssd.apiecocycle.exceptions.EstadoOrdenException;
import dssd.apiecocycle.model.*;
import dssd.apiecocycle.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OrdenService {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private CentroService centroService;


    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private MaterialService materialService;

    public void saveOrden(Orden orden) {
        ordenRepository.save(orden);
    }

    public Page<Orden> getOrdersByPedido(Pedido pedido, Integer cantidad, String materialName, EstadoOrden estado, LocalDate fechaOrden, int i, int pageSize) {
        return ordenRepository.findByPedidoAndArgs(pedido, cantidad, materialName, estado, fechaOrden, PageRequest.of(i, pageSize));
    }

    public Orden getOrdenById(Long id) throws CentroInvalidoException, AccessDeniedException {
        Centro centro = centroService.recuperarCentro();
        if (centro.hasPermission("CONSULTAR_ORDEN_PROVEEDOR")) {
            return getOrdenByIdAndCenterId(id, centro.getId());
        }
        if (centro.hasRole("CONSULTAR_ORDEN_DEPOSITO")) {
            return getOrdenByIdAndDepositoGlobalId(id, centro.getId());
        }
        throw new AccessDeniedException("No tiene permisos para acceder a este recurso.");
    }

    private Orden getOrdenByIdAndDepositoGlobalId(Long id, Long id1) {
        return ordenRepository.findByIdAndPedido_DepositoGlobal_Id(id, id1).orElseThrow(() -> new NoSuchElementException("Orden no encontrada"));
    }

    private Orden getOrdenByIdAndCenterId(Long id, Long id1) {
        return ordenRepository.findByIdAndCentroDeRecepcion_Id(id, id1).orElseThrow(() -> new NoSuchElementException("Orden no encontrada"));
    }

    public Orden generarOrden(OrdenDistribucionDTO ordenDistDTO) throws CentroInvalidoException {
        CentroDeRecepcion centro = (CentroDeRecepcion) centroService.recuperarCentro();
        Optional<Pedido> pedidoOptional = pedidoService.getPedidoById(ordenDistDTO.getPedidoId());
        if (pedidoOptional.isEmpty()) {
            throw new NoSuchElementException("Pedido no encontrado");
        }

        Pedido pedido = pedidoOptional.get();
        if (ordenDistDTO.getCantidad() <= 0) {
            throw new CantidadException("La cantidad de la orden debe ser mayor a cero");
        }
        int cantidadFaltante = pedido.getCantidad() - pedido.getCantidadAbastecida();
        if (ordenDistDTO.getCantidad() > cantidadFaltante) {
            throw new CantidadException("La cantidad de la orden no puede ser mayor que la cantidad faltante");
        }
        Material material = materialService.getMaterialById(ordenDistDTO.getMaterialId());
        Orden nuevaOrden = new Orden(material, EstadoOrden.PENDIENTE, ordenDistDTO.getCantidad(), centro, pedido);
        saveOrden(nuevaOrden);
        return nuevaOrden;
    }




    public Orden updateOrden(Orden orden) {
        return ordenRepository.save(orden);
    }

    public List<Orden> getOrdenesPorPedidoId(Long pedidoId) {
        return ordenRepository.findByPedidoId(pedidoId);
    }

    public List<Orden> getOrdenesPorPedidoIdAndEstado(Long pedidoId, EstadoOrden estado) {
        return ordenRepository.findByPedidoIdAndEstado(pedidoId, estado);
    }


    private void rechazarOrdenesPendientes(Pedido pedido) {
        List<Orden> ordenesPendientes = getOrdenesPorPedidoIdAndEstado(pedido.getId(), EstadoOrden.PENDIENTE);
        for (Orden orden : ordenesPendientes) {
            orden.setEstado(EstadoOrden.RECHAZADA);
            saveOrden(orden);
        }
    }

    public Orden entregarOrden(Long id) throws CentroInvalidoException {
        Centro centro = centroService.recuperarCentro();
        Orden orden = getOrdenByIdAndDepositoGlobalId(id, centro.getId());
        if (orden.is_sent()) {
            orden.setEstado(EstadoOrden.ENTREGADA);
            updateOrden(orden);

            return orden;
        }
        throw new EstadoOrdenException("No se puede entregar la orden");
    }

    public Orden rechazarOrden(Long id) throws CentroInvalidoException {
        Centro centro = centroService.recuperarCentro();
        Orden orden = getOrdenByIdAndDepositoGlobalId(id, centro.getId());
        if (orden.is_pending()) {
            orden.setEstado(EstadoOrden.RECHAZADA);
            updateOrden(orden);
            return orden;
        }
        throw new EstadoOrdenException("No se puede rechazar la orden");
    }

    public Orden aceptarOrden(Long id, Long cantidad) throws CentroInvalidoException {
        Centro centro = centroService.recuperarCentro();
        Orden orden = getOrdenByIdAndDepositoGlobalId(id, centro.getId());
        if (cantidad> orden.getCantidad())
            throw new CantidadException("La cantidad aceptada no puede ser mayor que la cantidad de la orden");
        if(cantidad<=0){
            throw new CantidadException("La cantidad aceptada debe ser mayor a cero");
        }
        Pedido pedido = orden.getPedido();
        if (cantidad>pedido.getCantidad()-pedido.getCantidadAbastecida()){
            throw new CantidadException("La cantidad aceptada no puede ser mayor que la cantidad faltante");
        }
        if (orden.is_pending()) {
            orden.setEstado(EstadoOrden.ACEPTADA);
            orden.setCantidadAceptada(Math.toIntExact(cantidad));
            updateOrden(orden);
            pedidoService.updateCantSupplied(pedido, cantidad);
            if (pedido.getAbastecido()) {
                rechazarOrdenesPendientes(pedido);
            }
            return orden;
        }
        throw new EstadoOrdenException("No se puede aceptar la orden");
    }

    public List<Orden> getMyOrders() throws CentroInvalidoException {
        Centro centro = centroService.recuperarCentro();
        return ordenRepository.findByCentroDeRecepcion_Id(centro.getId());    }

    public Page<Orden> getAllOrdersByPedidoIdAndArgs(Long id, Integer cantidad, String materialName, EstadoOrden estado, LocalDate fechaOrden, int i, int pageSize) {
        Optional<Pedido> pedido = pedidoService.getPedidoById(id);
        if (pedido.isEmpty()) {
            throw new NoSuchElementException("Pedido no encontrado");
        }

        return getOrdersByPedido(pedido.get(), cantidad, materialName, estado, fechaOrden, i, pageSize);
    }

    public Page<Orden> getMyOrders(Integer cantidad, Long globalId, String materialName, EstadoOrden estado, LocalDate fechaOrden, int page, int pageSize) {
        return ordenRepository.findMyOrders(cantidad, globalId, materialName, estado, fechaOrden, PageRequest.of(page, pageSize));
    }

    public Orden prepararOrden(Long id) {
        Orden orden = ordenRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Orden no encontrada"));
        if (orden.is_accepted()) {
            orden.setEstado(EstadoOrden.PREPARANDO);
            return updateOrden(orden);
        }
        throw new EstadoOrdenException("No se puede preparar la orden");
    }

    public Orden ordenPreparada(Long id) {
        Orden orden = ordenRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Orden no encontrada"));
        if (orden.is_preparing()) {
            orden.setEstado(EstadoOrden.PREPARADA);
            return updateOrden(orden);
        }
        throw new EstadoOrdenException("No se puede marcar la orden como preparada");
    }

    public Orden enviarOrden(Long id) {
        Orden orden = ordenRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Orden no encontrada"));
        if (orden.is_prepared()) {
            orden.setEstado(EstadoOrden.ENVIADA);
            return updateOrden(orden);
        }
        throw new EstadoOrdenException("No se puede enviar la orden");
    }
}
