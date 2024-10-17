package dssd.apiecocycle.service;

import dssd.apiecocycle.DTO.OrdenDTO;
import dssd.apiecocycle.DTO.OrdenDistribucionDTO;
import dssd.apiecocycle.exceptions.CantidadException;
import dssd.apiecocycle.exceptions.CentroInvalidoException;
import dssd.apiecocycle.exceptions.EstadoOrdenException;
import dssd.apiecocycle.model.*;
import dssd.apiecocycle.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        if (centro.hasRole("ROLE_CENTER")) {
            return getOrdenByIdAndCenterId(id, centro.getId());
        }
        if (centro.hasRole("ROLE_DEPOSIT")) {
            return getOrdenByIdAndDepositoGlobalId(id, centro.getId());
        }

        throw new AccessDeniedException("No tiene permisos para acceder a este recurso.");
    }

    public Orden generarOrden(OrdenDistribucionDTO ordenDistDTO) {
        Optional<CentroDeRecepcion> centroDeRecepcion = centroDeRecepcionService
                .getCentroById(ordenDistDTO.getCentroDeRecepcionId());
        Optional<Pedido> pedidoOptional = pedidoService.getPedidoById(ordenDistDTO.getPedidoId());

        if (centroDeRecepcion.isEmpty() || pedidoOptional.isEmpty()) {
            throw new NoSuchElementException("Centro de recepción o pedido no encontrado");
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


    private Orden getOrdenByIdAndCenterId(Long idOrden, Long idCentro) {
        return ordenRepository.findByIdAndCentroDeRecepcion_Id(idOrden, idCentro).orElseThrow();
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

    private boolean is_pending(Orden orden) {
        return orden.getEstado().equals(EstadoOrden.PENDIENTE);
    }
    public Orden entregarOrden(Long id) throws CentroInvalidoException {
        Centro centro = centroService.recuperarCentro();
        Orden orden = getOrdenByIdAndDepositoGlobalId(id, centro.getId());
        if (is_pending(orden)) {
            orden.setEstado(EstadoOrden.ENTREGADO);
            updateOrden(orden);
            pedidoService.updateCantSupplied(orden.getPedido(), orden.getCantidad());
            return orden;
        }
        throw new EstadoOrdenException("No se puede entregar la orden");
    }

    public Orden rechazarOrden(Long id) throws CentroInvalidoException {
        Centro centro = centroService.recuperarCentro();
        Orden orden = getOrdenByIdAndDepositoGlobalId(id, centro.getId());
        if (is_pending(orden)) {
            orden.setEstado(EstadoOrden.RECHAZADO);
            updateOrden(orden);
            return orden;
        }
        throw new EstadoOrdenException("No se puede rechazar la orden");
    }

    public Orden aceptarOrden(Long id) throws CentroInvalidoException {
        Centro centro = centroService.recuperarCentro();
        Orden orden = getOrdenByIdAndDepositoGlobalId(id, centro.getId());
        if (is_pending(orden)) {
            orden.setEstado(EstadoOrden.ACEPTADO);
            updateOrden(orden);
            return orden;
        }
        throw new EstadoOrdenException("No se puede aceptar la orden");
    }

    public List<Orden> getMyOrders() throws CentroInvalidoException {
        Centro centro = centroService.recuperarCentro();
        return ordenRepository.findByCentroDeRecepcion_Id(centro.getId());
    }
}
