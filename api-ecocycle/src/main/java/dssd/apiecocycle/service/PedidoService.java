package dssd.apiecocycle.service;

import dssd.apiecocycle.DTO.CreatePedidoDTO;
import dssd.apiecocycle.DTO.OrdenDistribucionDTO;
import dssd.apiecocycle.exceptions.CentroInvalidoException;
import dssd.apiecocycle.model.*;
import dssd.apiecocycle.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private CentroDeRecepcionService centroDeRecepcionService;

    @Autowired
    private OrdenService ordenService;

    @Autowired
    private CentroService centroService;
    public Optional<Pedido> getPedidoById(Long id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> getOrdersByMaterial(Material material) {
        return pedidoRepository.findByMaterial(material);
    }

    public Orden generarOrden(OrdenDistribucionDTO ordenDistDTO) {
        Optional<CentroDeRecepcion> centroDeRecepcion = centroDeRecepcionService
                .getCentroById(ordenDistDTO.getCentroDeRecepcionId());
        Optional<Pedido> pedidoOptional = getPedidoById(ordenDistDTO.getPedidoId());

        if (centroDeRecepcion.isEmpty() || pedidoOptional.isEmpty()) {
            throw new NoSuchElementException("Centro de recepción o pedido no encontrado");
        }
        Pedido pedido = pedidoOptional.get();
        if (ordenDistDTO.getCantidad() <= 0) {
            throw new RuntimeException("La cantidad de la orden debe ser mayor a cero");
        }
        int cantidadFaltante = pedido.getCantidad() - pedido.getCantidadAbastecida();
        if (ordenDistDTO.getCantidad() > cantidadFaltante) {
            throw new RuntimeException("La cantidad de la orden no puede ser mayor que la cantidad faltante");
        }
        Material material = materialService.getMaterialById(ordenDistDTO.getMaterialId());
        Orden nuevaOrden = new Orden(material, EstadoOrden.PENDIENTE, ordenDistDTO.getCantidad(), centroDeRecepcion.get(), pedido);
        ordenService.saveOrden(nuevaOrden);
        return nuevaOrden;
    }

    public Pedido savePedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public void updateCantSupplied(Pedido pedido, int cantidad) {
        pedido.setCantidadAbastecida(pedido.getCantidadAbastecida() + cantidad);
        if (pedido.getCantidadAbastecida() >= pedido.getCantidad()) {
            pedido.setAbastecido(true);
            rechazarOrdenesPendientes(pedido);
        }
        savePedido(pedido);
    }

    private void rechazarOrdenesPendientes(Pedido pedido) {
        List<Orden> ordenesPendientes = ordenService.getOrdenesPorPedidoId(pedido.getId())
                .stream()
                .filter(orden -> orden.getEstado() == EstadoOrden.PENDIENTE)
                .collect(Collectors.toList());

        for (Orden orden : ordenesPendientes) {
            orden.setEstado(EstadoOrden.RECHAZADO);
            ordenService.saveOrden(orden);
        }
    }

    public List<Pedido> getOrdersByMaterialAndAbastecido(Material material, boolean b) {
        return pedidoRepository.findByMaterialAndAbastecido(material, b);
    }

    public Pedido obtenerPedido(Long id) {
        return getPedidoById(id).orElseThrow();
    }

    public List<Pedido> getOrdersByMaterialNameAndAbastecido(String nameMaterial, boolean b) {
        Material material = materialService.getMaterialByName(nameMaterial);
        return getOrdersByMaterialAndAbastecido(material, b);
    }


    public Pedido crearPedido(CreatePedidoDTO createPedidoDTO) throws CentroInvalidoException {
        Material material = materialService.getMaterialById(createPedidoDTO.getMaterialId());
        if (material == null) {
            throw new NoSuchElementException("Material no encontrado");
        }
        if (createPedidoDTO.getCantidad() < 1) {
            throw new RuntimeException("La cantidad del pedido debe ser mayor a cero");
        }
        DepositoGlobal depositoGlobal = (DepositoGlobal) centroService.recuperarCentro();
        Pedido newPedido = new Pedido(material, createPedidoDTO.getCantidad(), depositoGlobal);
        return savePedido(newPedido);
    }

    public List<Orden> getAllOrdersByPedidoId(Long id) {
        Optional<Pedido> pedido = getPedidoById(id);
        if (pedido.isEmpty()) {
            throw new NoSuchElementException("Pedido no encontrado");
        }
        return ordenService.getOrdersByPedido(pedido);
    }
}
