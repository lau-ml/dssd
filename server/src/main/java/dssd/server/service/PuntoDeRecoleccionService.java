package dssd.server.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dssd.server.DTO.MaterialDTO;
import dssd.server.DTO.PaginatedResponseDTO;
import dssd.server.DTO.PuntoDeRecoleccionDTO;
import dssd.server.exception.UsuarioInvalidoException;
import dssd.server.model.PuntoDeRecoleccion;
import dssd.server.model.Usuario;
import dssd.server.repository.PuntoDeRecoleccionRepository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class PuntoDeRecoleccionService {
    @Autowired
    private PuntoDeRecoleccionRepository puntoDeRecoleccionRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public List<PuntoDeRecoleccion> obtenerPuntoDeRecolecciones() {
        return puntoDeRecoleccionRepository.findAll();
    }

    @Transactional
    public List<PuntoDeRecoleccion> obtenerPuntosDeRecoleccionPorRecolector()
            throws JsonProcessingException, UsuarioInvalidoException {
        Usuario usuarioActual = userService.recuperarUsuario();
        return usuarioActual.getPuntosDeRecoleccion();
    }

    @Transactional
    public PaginatedResponseDTO<PuntoDeRecoleccionDTO> obtenerPuntosDeRecoleccionPaginados(Pageable pageable)
            throws JsonProcessingException, UsuarioInvalidoException {
        Usuario usuarioActual = userService.recuperarUsuario();
        Page<PuntoDeRecoleccion> puntosDeRecoleccionPaginados = puntoDeRecoleccionRepository
                .findByUsuariosAndIsDeletedFalse(usuarioActual, pageable);

        List<PuntoDeRecoleccionDTO> content = puntosDeRecoleccionPaginados.getContent().stream()
                .map(PuntoDeRecoleccionDTO::new)
                .collect(Collectors.toList());

        return new PaginatedResponseDTO<>(content,
                puntosDeRecoleccionPaginados.getTotalPages(),
                puntosDeRecoleccionPaginados.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize());
    }

    @Transactional
    public PaginatedResponseDTO<PuntoDeRecoleccionDTO> obtenerPuntosDeRecoleccionFiltrados(Pageable pageable,
            String search) throws JsonProcessingException, UsuarioInvalidoException {

        Usuario usuarioActual = userService.recuperarUsuario();

        Page<PuntoDeRecoleccion> puntosDeRecoleccionPaginados = puntoDeRecoleccionRepository
                .findByUsuariosAndNombreEstablecimientoContainingIgnoreCaseOrDireccionContainingIgnoreCaseAndIsDeletedFalse(
                        usuarioActual, search, search, pageable);

        List<PuntoDeRecoleccionDTO> content = puntosDeRecoleccionPaginados.getContent().stream()
                .map(PuntoDeRecoleccionDTO::new)
                .collect(Collectors.toList());

        return new PaginatedResponseDTO<>(content,
                puntosDeRecoleccionPaginados.getTotalPages(),
                puntosDeRecoleccionPaginados.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize());
    }
}
