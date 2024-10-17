package dssd.apiecocycle.service;

import dssd.apiecocycle.DTO.OrdenDistribucionDTO;
import dssd.apiecocycle.exceptions.CantidadException;
import dssd.apiecocycle.exceptions.CentroInvalidoException;
import dssd.apiecocycle.exceptions.EstadoOrdenException;
import dssd.apiecocycle.model.*;
import dssd.apiecocycle.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
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
    private CentroDeRecepcionService centroDeRecepcionService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private MaterialService materialService;

    public Orden saveOrden(Orden orden) {
        return ordenRepository.save(orden);
    }

    public List<Orden> getOrdersByPedido(Optional<Pedido> pedido) {
        return ordenRepository.findByPedido(pedido);
    }

    public Orden getOrdenById(Long id) throws CentroInvalidoException, AccessDeniedException {
        Centro centro = centroService.recuperarCentro();
        Orden orden = centro.getOrdenById(id);
        if (orden == null) {
            throw new NoSuchElementException("Orden no encontrada");
        }
        return orden;
    }

    public Orden generarOrden(OrdenDistribucionDTO ordenDistDTO) {
        Optional<CentroDeRecepcion> centroDeRecepcion = centroDeRecepcionService
                .getCentroById(ordenDistDTO.getCentroDeRecepcionId());
        Optional<Pedido> pedidoOptional = pedidoService.getPedidoById(ordenDistDTO.getPedidoId());

        if (centroDeRecepcion.isEmpty()) {
            throw new NoSuchElementException("Centro de recepción");
        }
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
        Orden nuevaOrden = new Orden(material, EstadoOrden.PENDIENTE, ordenDistDTO.getCantidad(), centroDeRecepcion.get(), pedido);
        saveOrden(nuevaOrden);
        return nuevaOrden;
    }




    public Orden updateOrden(Orden orden) {
        return ordenRepository.save(orden);
    }

    public List<Orden> getOrdenesPorPedidoId(Long pedidoId) {
        return ordenRepository.findByPedidoId(pedidoId);
    }


    private boolean is_pending(Orden orden) {
        return orden.getEstado().equals(EstadoOrden.PENDIENTE);
    }


    private void rechazarOrdenesPendientes(Pedido pedido) {
        List<Orden> ordenesPendientes = getOrdenesPorPedidoId(pedido.getId())
                .stream()
                .filter(orden -> orden.getEstado() == EstadoOrden.PENDIENTE)
                .toList();

        for (Orden orden : ordenesPendientes) {
            orden.setEstado(EstadoOrden.RECHAZADO);
            saveOrden(orden);
        }
    }

    public Orden entregarOrden(Long id) throws CentroInvalidoException {
        Centro centro = centroService.recuperarCentro();
        Orden orden = centro.getOrdenById(id);
        if (is_pending(orden)) {
            orden.setEstado(EstadoOrden.ENTREGADO);
            updateOrden(orden);
            Pedido pedido = pedidoService.updateCantSupplied(orden.getPedido(), orden.getCantidad());
            if (pedido.getAbastecido()) {
                rechazarOrdenesPendientes(pedido);
            }
            return orden;
        }
        throw new EstadoOrdenException("No se puede entregar la orden");
    }

    public Orden rechazarOrden(Long id) throws CentroInvalidoException {
        Centro centro = centroService.recuperarCentro();
        Orden orden = centro.getOrdenById(id);
        if (is_pending(orden)) {
            orden.setEstado(EstadoOrden.RECHAZADO);
            updateOrden(orden);
            return orden;
        }
        throw new EstadoOrdenException("No se puede rechazar la orden");
    }

    public Orden aceptarOrden(Long id) throws CentroInvalidoException {
        Centro centro = centroService.recuperarCentro();
        Orden orden = centro.getOrdenById(id);
        if (is_pending(orden)) {
            orden.setEstado(EstadoOrden.ACEPTADO);
            updateOrden(orden);
            return orden;
        }
        throw new EstadoOrdenException("No se puede aceptar la orden");
    }

    public List<Orden> getMyOrders() throws CentroInvalidoException {
        Centro centro = centroService.recuperarCentro();
        return centro.getOrdenes();
    }

    public List<Orden> getAllOrdersByPedidoId(Long id) {
        Optional<Pedido> pedido = pedidoService.getPedidoById(id);
        if (pedido.isEmpty()) {
            throw new NoSuchElementException("Pedido no encontrado");
        }
        return getOrdersByPedido(pedido);
    }

}
