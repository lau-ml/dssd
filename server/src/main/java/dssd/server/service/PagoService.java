package dssd.server.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dssd.server.DTO.PaginatedResponseDTO;
import dssd.server.DTO.PagoDTO;
import dssd.server.model.Pago;
import dssd.server.repository.PagoRepository;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

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

}
