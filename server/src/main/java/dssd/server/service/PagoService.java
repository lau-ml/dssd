package dssd.server.service;

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
            String search) {

        Page<Pago> pagosPage;

        if (search != null && !search.trim().isEmpty()) {
            pagosPage = pagoRepository.findByRegistroRecoleccion_Recolector_IdAndMontoContainingIgnoreCase(
                    recolectorId, search, pageable);
        } else {
            pagosPage = pagoRepository.findByRegistroRecoleccion_Recolector_Id(recolectorId, pageable);
        }

        return new PaginatedResponseDTO<>(
                pagosPage.getContent().stream().map(PagoDTO::new).collect(Collectors.toList()),
                pagosPage.getTotalPages(),
                pagosPage.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize());
    }
}
