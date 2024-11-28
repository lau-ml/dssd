package dssd.server.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import dssd.server.helpers.BonitaState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import dssd.server.DTO.PaginatedResponseDTO;
import dssd.server.DTO.PagoDTO;
import dssd.server.exception.UsuarioInvalidoException;
import dssd.server.model.CentroRecoleccion;
import dssd.server.model.Pago;
import dssd.server.model.Rol;
import dssd.server.model.Usuario;
import dssd.server.repository.PagoRepository;
import dssd.server.repository.RolRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private BonitaState bonitaStateService;

    public PaginatedResponseDTO<PagoDTO> listarPagosPorRecolector(
            Long recolectorId,
            Pageable pageable,
            String estadoStr,
            String columnaFecha,
            LocalDate fechaDesde,
            LocalDate fechaHasta) {

        Page<Pago> pagosPage;

        Date fechaDesdeDate = fechaDesde != null ? java.sql.Date.valueOf(fechaDesde) : null;
        Date fechaHastaDate = fechaHasta != null ? java.sql.Date.valueOf(fechaHasta) : null;

        Pago.EstadoPago estado = null;
        if (estadoStr != null && !estadoStr.trim().isEmpty() && !estadoStr.equalsIgnoreCase("TODOS")) {
            try {
                estado = Pago.EstadoPago.valueOf(estadoStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Estado de pago no válido: " + estadoStr);
            }
        }

        if (estado != null) {
            if (fechaDesdeDate != null && fechaHastaDate != null) {
                if ("fechaEmision".equalsIgnoreCase(columnaFecha)) {
                    pagosPage = pagoRepository.findByRegistroRecoleccion_Recolector_IdAndEstadoAndFechaEmisionBetween(
                            recolectorId, estado, fechaDesdeDate, fechaHastaDate, pageable);
                } else {
                    pagosPage = pagoRepository.findByRegistroRecoleccion_Recolector_IdAndEstadoAndFechaPagoBetween(
                            recolectorId, estado, fechaDesdeDate, fechaHastaDate, pageable);
                }
            } else {
                pagosPage = pagoRepository.findByRegistroRecoleccion_Recolector_IdAndEstado(
                        recolectorId, estado, pageable);
            }
        } else {
            if (fechaDesdeDate != null && fechaHastaDate != null) {
                if ("fechaEmision".equalsIgnoreCase(columnaFecha)) {
                    pagosPage = pagoRepository.findByRegistroRecoleccion_Recolector_IdAndFechaEmisionBetween(
                            recolectorId, fechaDesdeDate, fechaHastaDate, pageable);
                } else {
                    pagosPage = pagoRepository.findByRegistroRecoleccion_Recolector_IdAndFechaPagoBetween(
                            recolectorId, fechaDesdeDate, fechaHastaDate, pageable);
                }
            } else {
                pagosPage = pagoRepository.findByRegistroRecoleccion_Recolector_Id(recolectorId, pageable);
            }
        }

        return new PaginatedResponseDTO<>(
                pagosPage.getContent().stream().map(PagoDTO::new).collect(Collectors.toList()),
                pagosPage.getTotalPages(),
                pagosPage.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize());
    }

    public PaginatedResponseDTO<PagoDTO> listarPagosPorCentro(
            Pageable pageable,
            String estadoStr,
            String columnaFecha,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            String username) throws JsonProcessingException, UsuarioInvalidoException {

        Usuario usuarioActual = userService.recuperarUsuario();
        CentroRecoleccion centroRecoleccion = usuarioActual.getCentroRecoleccion();
        if (centroRecoleccion == null) {
            throw new IllegalArgumentException("El usuario no está asociado a un centro de recolección.");
        }

        Long centroRecoleccionId = centroRecoleccion.getId();
        Page<Pago> pagosPage;

        Date fechaDesdeDate = fechaDesde != null ? java.sql.Date.valueOf(fechaDesde) : null;
        Date fechaHastaDate = fechaHasta != null ? java.sql.Date.valueOf(fechaHasta) : null;

        Pago.EstadoPago estado = null;
        if (estadoStr != null && !estadoStr.trim().isEmpty() && !estadoStr.equalsIgnoreCase("TODOS")) {
            try {
                estado = Pago.EstadoPago.valueOf(estadoStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Estado de pago no válido: " + estadoStr);
            }
        }

        if (username != null && !username.trim().isEmpty()) {
            if (estado != null) {
                if (fechaDesdeDate != null && fechaHastaDate != null) {
                    if ("fechaEmision".equalsIgnoreCase(columnaFecha)) {
                        pagosPage = pagoRepository
                                .findByRegistroRecoleccion_Recolector_CentroRecoleccion_IdAndEstadoAndFechaEmisionBetweenAndRegistroRecoleccion_Recolector_UsernameContainingIgnoreCase(
                                        centroRecoleccionId, estado, fechaDesdeDate, fechaHastaDate, username,
                                        pageable);
                    } else {
                        pagosPage = pagoRepository
                                .findByRegistroRecoleccion_Recolector_CentroRecoleccion_IdAndEstadoAndFechaPagoBetweenAndRegistroRecoleccion_Recolector_UsernameContainingIgnoreCase(
                                        centroRecoleccionId, estado, fechaDesdeDate, fechaHastaDate, username,
                                        pageable);
                    }
                } else {
                    pagosPage = pagoRepository
                            .findByRegistroRecoleccion_Recolector_CentroRecoleccion_IdAndEstadoAndRegistroRecoleccion_Recolector_UsernameContainingIgnoreCase(
                                    centroRecoleccionId, estado, username, pageable);
                }
            } else {
                if (fechaDesdeDate != null && fechaHastaDate != null) {
                    if ("fechaEmision".equalsIgnoreCase(columnaFecha)) {
                        pagosPage = pagoRepository
                                .findByRegistroRecoleccion_Recolector_CentroRecoleccion_IdAndFechaEmisionBetweenAndRegistroRecoleccion_Recolector_UsernameContainingIgnoreCase(
                                        centroRecoleccionId, fechaDesdeDate, fechaHastaDate, username, pageable);
                    } else {
                        pagosPage = pagoRepository
                                .findByRegistroRecoleccion_Recolector_CentroRecoleccion_IdAndFechaPagoBetweenAndRegistroRecoleccion_Recolector_UsernameContainingIgnoreCase(
                                        centroRecoleccionId, fechaDesdeDate, fechaHastaDate, username, pageable);
                    }
                } else {
                    pagosPage = pagoRepository
                            .findByRegistroRecoleccion_Recolector_CentroRecoleccion_IdAndRegistroRecoleccion_Recolector_UsernameContainingIgnoreCase(
                                    centroRecoleccionId, username, pageable);
                }
            }
        } else {
            if (estado != null) {
                if (fechaDesdeDate != null && fechaHastaDate != null) {
                    if ("fechaEmision".equalsIgnoreCase(columnaFecha)) {
                        pagosPage = pagoRepository
                                .findByRegistroRecoleccion_Recolector_CentroRecoleccion_IdAndEstadoAndFechaEmisionBetween(
                                        centroRecoleccionId, estado, fechaDesdeDate, fechaHastaDate, pageable);
                    } else {
                        pagosPage = pagoRepository
                                .findByRegistroRecoleccion_Recolector_CentroRecoleccion_IdAndEstadoAndFechaPagoBetween(
                                        centroRecoleccionId, estado, fechaDesdeDate, fechaHastaDate, pageable);
                    }
                } else {
                    pagosPage = pagoRepository.findByRegistroRecoleccion_Recolector_CentroRecoleccion_IdAndEstado(
                            centroRecoleccionId, estado, pageable);
                }
            } else {
                if (fechaDesdeDate != null && fechaHastaDate != null) {
                    if ("fechaEmision".equalsIgnoreCase(columnaFecha)) {
                        pagosPage = pagoRepository
                                .findByRegistroRecoleccion_Recolector_CentroRecoleccion_IdAndFechaEmisionBetween(
                                        centroRecoleccionId, fechaDesdeDate, fechaHastaDate, pageable);
                    } else {
                        pagosPage = pagoRepository
                                .findByRegistroRecoleccion_Recolector_CentroRecoleccion_IdAndFechaPagoBetween(
                                        centroRecoleccionId, fechaDesdeDate, fechaHastaDate, pageable);
                    }
                } else {
                    pagosPage = pagoRepository.findByRegistroRecoleccion_Recolector_CentroRecoleccion_Id(
                            centroRecoleccionId, pageable);
                }
            }
        }

        return new PaginatedResponseDTO<>(
                pagosPage.getContent().stream().map(PagoDTO::new).collect(Collectors.toList()),
                pagosPage.getTotalPages(),
                pagosPage.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize());
    }

    @Transactional
    public PagoDTO realizarPago(Long pagoId) {
        System.out.println("entre al metodo");
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        System.out.println("encontre el pago");
        if (pago.getEstado().equals(Pago.EstadoPago.PENDIENTE)) {
            System.out.println("tiene el estado pendiente");
            pago.setEstado(Pago.EstadoPago.PAGADO);
            bonitaStateService.confirmarPago(pago);
            pagoRepository.save(pago);
        } else {
            System.out.println("no tiene el metodo pendiente");
            throw new RuntimeException("El pago ya ha sido realizado");
        }

        return new PagoDTO(pago);
    }

    public PaginatedResponseDTO<PagoDTO> listarPagosRecolecor(Pageable pageable, String estadoStr, String columnaFecha,
            LocalDate fechaDesde, LocalDate fechaHasta) throws JsonProcessingException, UsuarioInvalidoException {

        Usuario usuarioActual = userService.recuperarUsuario();
        Page<Pago> pagosPage;

        Date fechaDesdeDate = fechaDesde != null ? java.sql.Date.valueOf(fechaDesde) : null;
        Date fechaHastaDate = fechaHasta != null ? java.sql.Date.valueOf(fechaHasta) : null;

        Pago.EstadoPago estado = null;
        if (estadoStr != null && !estadoStr.trim().isEmpty() && !estadoStr.equalsIgnoreCase("TODOS")) {
            try {
                estado = Pago.EstadoPago.valueOf(estadoStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Estado de pago no válido: " + estadoStr);
            }
        }

        if (estado != null) {
            if (fechaDesdeDate != null && fechaHastaDate != null) {
                if ("fechaEmision".equalsIgnoreCase(columnaFecha)) {
                    pagosPage = pagoRepository.findByRegistroRecoleccion_Recolector_IdAndEstadoAndFechaEmisionBetween(
                            usuarioActual.getId(), estado, fechaDesdeDate, fechaHastaDate, pageable);
                } else {
                    pagosPage = pagoRepository.findByRegistroRecoleccion_Recolector_IdAndEstadoAndFechaPagoBetween(
                            usuarioActual.getId(), estado, fechaDesdeDate, fechaHastaDate, pageable);
                }
            } else {
                pagosPage = pagoRepository.findByRegistroRecoleccion_Recolector_IdAndEstado(
                        usuarioActual.getId(), estado, pageable);
            }
        } else {
            if (fechaDesdeDate != null && fechaHastaDate != null) {
                if ("fechaEmision".equalsIgnoreCase(columnaFecha)) {
                    pagosPage = pagoRepository.findByRegistroRecoleccion_Recolector_IdAndFechaEmisionBetween(
                            usuarioActual.getId(), fechaDesdeDate, fechaHastaDate, pageable);
                } else {
                    pagosPage = pagoRepository.findByRegistroRecoleccion_Recolector_IdAndFechaPagoBetween(
                            usuarioActual.getId(), fechaDesdeDate, fechaHastaDate, pageable);
                }
            } else {
                pagosPage = pagoRepository.findByRegistroRecoleccion_Recolector_Id(usuarioActual.getId(), pageable);
            }
        }

        return new PaginatedResponseDTO<>(
                pagosPage.getContent().stream().map(PagoDTO::new).collect(Collectors.toList()),
                pagosPage.getTotalPages(),
                pagosPage.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize());
    }

}
