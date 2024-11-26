package dssd.server.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import dssd.server.DTO.PaginatedResponseDTO;
import dssd.server.DTO.PuntoDeRecoleccionDTO;
import dssd.server.DTO.UsuarioDTO;
import dssd.server.exception.PuntoDeRecoleccionException;
import dssd.server.exception.UsuarioInvalidoException;
import dssd.server.model.DetalleRegistro;
import dssd.server.model.PuntoDeRecoleccion;
import dssd.server.model.Rol;
import dssd.server.model.SolicitudVinculacionPuntoRecoleccion;
import dssd.server.model.Usuario;
import dssd.server.repository.DetalleRegistroRepository;
import dssd.server.repository.PuntoDeRecoleccionRepository;
import dssd.server.repository.RolRepository;
import dssd.server.repository.SolicitudVinculacionPuntoRecoleccionRepository;
import dssd.server.repository.UsuarioRepository;

import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class PuntoDeRecoleccionService {
    @Autowired
    private PuntoDeRecoleccionRepository puntoDeRecoleccionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private DetalleRegistroRepository detalleRegistroRepository;

    @Autowired
    private SolicitudVinculacionPuntoRecoleccionRepository solicitudVinculacionPuntoRecoleccionRepository;

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
                .buscarPuntosPorUsuarioYFiltro(
                        usuarioActual, search, pageable);
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
    public void desvincularPuntoDeRecoleccion(Long id) throws UsuarioInvalidoException {
        Usuario usuarioActual = userService.recuperarUsuario();

        PuntoDeRecoleccion punto = puntoDeRecoleccionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El punto de recolección no existe."));

        if (!punto.getUsuarios().contains(usuarioActual)) {
            throw new IllegalArgumentException(
                    "El punto de recolección no está asociado al usuario actual.");
        }

        usuarioActual.removePuntoDeRecoleccion(punto);
        puntoDeRecoleccionRepository.save(punto);
        usuarioRepository.save(usuarioActual);
    }

    @Transactional
    public PaginatedResponseDTO<PuntoDeRecoleccionDTO> obtenerPuntosDeRecoleccionNoVinculadosPaginados(
            Pageable pageable)
            throws JsonProcessingException, UsuarioInvalidoException {

        Usuario usuarioActual = userService.recuperarUsuario();

        Page<PuntoDeRecoleccion> puntosDeRecoleccionNoVinculados = puntoDeRecoleccionRepository
                .findByUsuariosNotContainsAndIsDeletedFalse(usuarioActual, pageable);

        List<Long> puntosIdConSolicitud = solicitudVinculacionPuntoRecoleccionRepository
                .findPuntosConSolicitudPorRecolector(usuarioActual);

        List<PuntoDeRecoleccionDTO> content = puntosDeRecoleccionNoVinculados.getContent().stream()
                .map(punto -> {
                    PuntoDeRecoleccionDTO dto = new PuntoDeRecoleccionDTO(punto);

                    boolean tieneSolicitud = puntosIdConSolicitud.contains(punto.getId());
                    dto.setTieneSolicitud(tieneSolicitud);

                    return dto;
                })
                .collect(Collectors.toList());

        return new PaginatedResponseDTO<>(content,
                puntosDeRecoleccionNoVinculados.getTotalPages(),
                puntosDeRecoleccionNoVinculados.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize());
    }

    @Transactional
    public PaginatedResponseDTO<PuntoDeRecoleccionDTO> obtenerPuntosDeRecoleccionNoVinculadosFiltrados(
            Pageable pageable, String search)
            throws JsonProcessingException, UsuarioInvalidoException {

        Usuario usuarioActual = userService.recuperarUsuario();

        Page<PuntoDeRecoleccion> puntosDeRecoleccionNoVinculados = puntoDeRecoleccionRepository
                .findByUsuariosNotContainsAndIsDeletedFalseAndNombreEstablecimientoContainingIgnoreCaseOrDireccionContainingIgnoreCase(
                        usuarioActual, search, search, pageable);

        List<Long> puntosIdConSolicitud = solicitudVinculacionPuntoRecoleccionRepository
                .findPuntosConSolicitudPorRecolector(usuarioActual);

        List<PuntoDeRecoleccionDTO> content = puntosDeRecoleccionNoVinculados.getContent().stream()
                .map(punto -> {
                    PuntoDeRecoleccionDTO dto = new PuntoDeRecoleccionDTO(punto);

                    boolean tieneSolicitud = puntosIdConSolicitud.contains(punto.getId());
                    dto.setTieneSolicitud(tieneSolicitud);

                    return dto;
                })
                .collect(Collectors.toList());

        return new PaginatedResponseDTO<>(content,
                puntosDeRecoleccionNoVinculados.getTotalPages(),
                puntosDeRecoleccionNoVinculados.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize());
    }

    @Transactional
    public PaginatedResponseDTO<PuntoDeRecoleccionDTO> obtenerTodosPuntosDeRecoleccionFiltrados(
            Pageable pageable, String search)
            throws JsonProcessingException, UsuarioInvalidoException {

        Usuario usuarioActual = userService.recuperarUsuario();

        Page<PuntoDeRecoleccion> puntosDeRecoleccion = puntoDeRecoleccionRepository
                .findByIsDeletedFalseAndNombreEstablecimientoContainingIgnoreCaseOrDireccionContainingIgnoreCase(
                        search, search, pageable);

        List<PuntoDeRecoleccionDTO> content = puntosDeRecoleccion.getContent().stream()
                .map(PuntoDeRecoleccionDTO::new)
                .collect(Collectors.toList());

        return new PaginatedResponseDTO<>(content,
                puntosDeRecoleccion.getTotalPages(),
                puntosDeRecoleccion.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize());
    }

    @Transactional
    public PaginatedResponseDTO<PuntoDeRecoleccionDTO> obtenerTodosPuntosDeRecoleccionPaginados(
            Pageable pageable) {

        Page<PuntoDeRecoleccion> puntosDeRecoleccion = puntoDeRecoleccionRepository
                .findByIsDeletedFalse(pageable);

        List<PuntoDeRecoleccionDTO> content = puntosDeRecoleccion.getContent().stream()
                .map(PuntoDeRecoleccionDTO::new)
                .collect(Collectors.toList());

        return new PaginatedResponseDTO<>(content,
                puntosDeRecoleccion.getTotalPages(),
                puntosDeRecoleccion.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize());
    }

    @Transactional
    public PuntoDeRecoleccion obtenerPorId(Long id) {
        return puntoDeRecoleccionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Punto de recolección no encontrado con ID: " + id));
    }

    @Transactional
    public PuntoDeRecoleccion editarMaterial(Long id, PuntoDeRecoleccionDTO puntoDeRecoleccionDTO) {

        if (puntoDeRecoleccionDTO.getNombreEstablecimiento() == null
                || puntoDeRecoleccionDTO.getNombreEstablecimiento().trim().isEmpty()) {
            throw new PuntoDeRecoleccionException("El nombre del establecimiento es obligatorio.",
                    "INVALID_DATA");
        }
        if (puntoDeRecoleccionDTO.getDireccion() == null
                || puntoDeRecoleccionDTO.getDireccion().trim().isEmpty()) {
            throw new PuntoDeRecoleccionException("La dirección es obligatoria.", "INVALID_DATA");
        }
        if (puntoDeRecoleccionDTO.getNumeroContacto() == null
                || puntoDeRecoleccionDTO.getNumeroContacto().trim().isEmpty()) {
            throw new PuntoDeRecoleccionException("El número de contacto es obligatorio.", "INVALID_DATA");
        }

        if (!puntoDeRecoleccionDTO.getNumeroContacto().matches("^[0-9-]+$")) {
            throw new PuntoDeRecoleccionException("El número de contacto no tiene un formato válido.",
                    "INVALID_DATA");
        }

        PuntoDeRecoleccion puntoDeRecoleccion = puntoDeRecoleccionRepository.findById(id).orElse(null);
        if (puntoDeRecoleccion == null) {
            throw new PuntoDeRecoleccionException("Punto de recolección no encontrado.", "INVALID_DATA");
        }

        if (puntoDeRecoleccionRepository.existsByNombreEstablecimientoAndIdNot(
                puntoDeRecoleccionDTO.getNombreEstablecimiento(), id)) {
            throw new PuntoDeRecoleccionException(
                    "El nombre del establecimiento ya se encuentra duplicado.",
                    "INVALID_DATA");
        }

        puntoDeRecoleccion.setNombreEstablecimiento(puntoDeRecoleccionDTO.getNombreEstablecimiento());
        puntoDeRecoleccion.setDireccion(puntoDeRecoleccionDTO.getDireccion());
        puntoDeRecoleccion.setNumeroContacto(puntoDeRecoleccionDTO.getNumeroContacto());
        return puntoDeRecoleccionRepository.save(puntoDeRecoleccion);
    }

    @Transactional
    public PuntoDeRecoleccionDTO crearPuntoDeRecoleccion(PuntoDeRecoleccionDTO puntoDeRecoleccionDTO) {
        if (puntoDeRecoleccionDTO.getNombreEstablecimiento() == null
                || puntoDeRecoleccionDTO.getNombreEstablecimiento().length() < 3) {
            throw new PuntoDeRecoleccionException(
                    "El nombre del establecimiento es obligatorio y debe tener al menos 3 caracteres.",
                    "INVALID_DATA");
        }
        if (puntoDeRecoleccionDTO.getDireccion() == null || puntoDeRecoleccionDTO.getDireccion().length() < 5) {
            throw new PuntoDeRecoleccionException(
                    "La dirección es obligatoria y debe tener al menos 5 caracteres.",
                    "INVALID_DATA");
        }
        if (puntoDeRecoleccionDTO.getNumeroContacto() == null
                || !puntoDeRecoleccionDTO.getNumeroContacto().matches("^[0-9-]+$")) {
            throw new PuntoDeRecoleccionException(
                    "El número de contacto es obligatorio y debe contener solo números y guiones.",
                    "INVALID_DATA");
        }
        Optional<PuntoDeRecoleccion> puntoExistenteOptional = puntoDeRecoleccionRepository
                .findByNombreEstablecimientoIgnoreCase(
                        puntoDeRecoleccionDTO.getNombreEstablecimiento());

        if (puntoExistenteOptional.isPresent()) {
            PuntoDeRecoleccion puntoExistente = puntoExistenteOptional.get();
            if (!puntoExistente.isDeleted()) {
                throw new PuntoDeRecoleccionException(
                        "Ya existe un punto de recolección con el nombre: "
                                + puntoExistente.getNombreEstablecimiento(),
                        "INVALID_DATA");
            } else {
                puntoExistente.setDeleted(false);
                puntoExistente.setDireccion(puntoDeRecoleccionDTO.getDireccion());
                puntoExistente.setNumeroContacto(puntoDeRecoleccionDTO.getNumeroContacto());
                puntoDeRecoleccionRepository.save(puntoExistente);
                return new PuntoDeRecoleccionDTO(puntoExistente);
            }
        }

        PuntoDeRecoleccion nuevoPunto = new PuntoDeRecoleccion(puntoDeRecoleccionDTO.getNombreEstablecimiento(),
                puntoDeRecoleccionDTO.getDireccion(), puntoDeRecoleccionDTO.getNumeroContacto());

        puntoDeRecoleccionRepository.save(nuevoPunto);

        return new PuntoDeRecoleccionDTO(nuevoPunto);
    }

    public void eliminarPuntoDeRecoleccion(Long id) {
        PuntoDeRecoleccion puntoDeRecoleccion = puntoDeRecoleccionRepository.findById(id)
                .orElseThrow(
                        () -> new PuntoDeRecoleccionException(
                                "Punto de recolección no encontrado", "INVALID_DATA"));

        List<Usuario> recolectores = puntoDeRecoleccion.getUsuarios();
        for (Usuario recolector : recolectores) {
            recolector.removePuntoDeRecoleccion(puntoDeRecoleccion);
            usuarioRepository.save(recolector);
        }
        puntoDeRecoleccion.getUsuarios().clear();

        List<DetalleRegistro> detallesRegistros = detalleRegistroRepository
                .findByPuntoRecoleccion(puntoDeRecoleccion);

        if (!detallesRegistros.isEmpty()) {
            puntoDeRecoleccion.setDeleted(true);
            puntoDeRecoleccionRepository.save(puntoDeRecoleccion);
        } else {
            puntoDeRecoleccionRepository.delete(puntoDeRecoleccion);
        }
    }

    public PaginatedResponseDTO<UsuarioDTO> obtenerRecolectoresDePuntoPaginados(Long puntoId,
            Pageable pageable) {
        Optional<PuntoDeRecoleccion> punto = puntoDeRecoleccionRepository.findById(puntoId);
        Optional<Rol> rolRecolector = rolRepository.findByNombre("ROLE_RECOLECTOR");

        if (!punto.isPresent()) {
            throw new PuntoDeRecoleccionException("Punto de recolección no encontrado", "INVALID_DATA");
        }

        Page<Usuario> recolectoresPage = usuarioRepository.findByPuntosDeRecoleccionContainingAndRolAndActivo(
                punto.get(),
                rolRecolector.get(),
                pageable,
                true);

        List<UsuarioDTO> recolectorDTOs = recolectoresPage.getContent().stream()
                .map(UsuarioDTO::new)
                .collect(Collectors.toList());

        return new PaginatedResponseDTO<>(recolectorDTOs,
                recolectoresPage.getTotalPages(),
                recolectoresPage.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize());
    }

    public PaginatedResponseDTO<UsuarioDTO> obtenerRecolectoresDePuntoFiltrados(Long puntoId, Pageable pageable,
            String search) {
        Optional<PuntoDeRecoleccion> punto = puntoDeRecoleccionRepository.findById(puntoId);
        Optional<Rol> rolRecolector = rolRepository.findByNombre("ROLE_RECOLECTOR");

        if (!punto.isPresent()) {
            throw new PuntoDeRecoleccionException("Punto de recolección no encontrado", "INVALID_DATA");
        }

        Page<Usuario> recolectoresPage = usuarioRepository
                .findByPuntosDeRecoleccionContainingAndNombreContainingIgnoreCaseAndRolAndActivo(
                        punto.get(), search, pageable, rolRecolector.get(), true);

        List<UsuarioDTO> recolectorDTOs = recolectoresPage.getContent().stream()
                .map(UsuarioDTO::new)
                .collect(Collectors.toList());

        return new PaginatedResponseDTO<>(recolectorDTOs,
                recolectoresPage.getTotalPages(),
                recolectoresPage.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize());
    }

    public PaginatedResponseDTO<UsuarioDTO> obtenerRecolectoresNoAsociadosAPunto(Long puntoId, Pageable pageable) {
        Optional<PuntoDeRecoleccion> punto = puntoDeRecoleccionRepository.findById(puntoId);
        Optional<Rol> rolRecolector = rolRepository.findByNombre("ROLE_RECOLECTOR");

        if (!punto.isPresent()) {
            throw new PuntoDeRecoleccionException("Punto de recolección no encontrado", "INVALID_DATA");
        }

        Page<Usuario> recolectoresPage = usuarioRepository
                .findByPuntosDeRecoleccionNotContainingAndRolAndActivo(
                        punto.get(),
                        rolRecolector.get(),
                        pageable, true);

        List<UsuarioDTO> recolectorDTOs = recolectoresPage.getContent().stream()
                .map(UsuarioDTO::new)
                .collect(Collectors.toList());

        return new PaginatedResponseDTO<>(recolectorDTOs,
                recolectoresPage.getTotalPages(),
                recolectoresPage.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize());
    }

    public PaginatedResponseDTO<UsuarioDTO> obtenerRecolectoresNoAsociadosAPuntoFiltrados(
            Long puntoId, Pageable pageable, String search) {
        Optional<PuntoDeRecoleccion> punto = puntoDeRecoleccionRepository.findById(puntoId);
        Optional<Rol> rolRecolector = rolRepository.findByNombre("ROLE_RECOLECTOR");

        if (!punto.isPresent()) {
            throw new PuntoDeRecoleccionException("Punto de recolección no encontrado", "INVALID_DATA");
        }

        Page<Usuario> recolectoresPage = usuarioRepository
                .findByPuntosDeRecoleccionNotContainingAndNombreContainingIgnoreCaseAndRolAndActivo(
                        punto.get(), search, pageable, rolRecolector.get(), true);

        List<UsuarioDTO> recolectorDTOs = recolectoresPage.getContent().stream()
                .map(UsuarioDTO::new)
                .collect(Collectors.toList());

        return new PaginatedResponseDTO<>(recolectorDTOs,
                recolectoresPage.getTotalPages(),
                recolectoresPage.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize());
    }

    public void vincularRecolector(Long puntoId, Long recolectorId) {
        PuntoDeRecoleccion punto = puntoDeRecoleccionRepository.findById(puntoId)
                .orElseThrow(() -> new IllegalArgumentException("Punto de recolección no encontrado"));
        Usuario recolector = usuarioRepository.findById(recolectorId)
                .orElseThrow(() -> new IllegalArgumentException("Recolector no encontrado"));

        Optional<SolicitudVinculacionPuntoRecoleccion> solicitudVinculacionPuntoRecoleccion = solicitudVinculacionPuntoRecoleccionRepository
                .findByRecolectorAndPuntoDeRecoleccion(recolector, punto);

        solicitudVinculacionPuntoRecoleccion.ifPresent(solicitudVinculacionPuntoRecoleccionRepository::delete);

        recolector.addPuntoDeRecoleccion(punto);
        usuarioRepository.save(recolector);
        puntoDeRecoleccionRepository.save(punto);
    }

    public void desvincularRecolector(Long puntoId, Long recolectorId) {
        PuntoDeRecoleccion punto = puntoDeRecoleccionRepository.findById(puntoId)
                .orElseThrow(() -> new IllegalArgumentException("Punto de recolección no encontrado"));
        Usuario recolector = usuarioRepository.findById(recolectorId)
                .orElseThrow(() -> new IllegalArgumentException("Recolector no encontrado"));

        if (!recolector.getPuntosDeRecoleccion().contains(punto)) {
            throw new PuntoDeRecoleccionException(
                    "El recolector no está asociado a este punto de recolección",
                    "INVALID_DATA");
        }

        recolector.removePuntoDeRecoleccion(punto);

        usuarioRepository.save(recolector);
        puntoDeRecoleccionRepository.save(punto);
    }

    public PaginatedResponseDTO<PuntoDeRecoleccionDTO> obtenerPuntosPaginadosYFiltrados(
            Pageable pageable, String search, Long recolectorId) {
        Optional<Usuario> recolector = usuarioRepository.findById(recolectorId);

        if (recolector.isEmpty()) {
            throw new IllegalArgumentException("El recolector con el ID proporcionado no existe.");
        }

        Page<PuntoDeRecoleccion> puntosPage;

        if (search != null && !search.trim().isEmpty()) {
            puntosPage = puntoDeRecoleccionRepository
                    .buscarPuntosPorUsuarioYFiltro(
                            recolector.get(), search, pageable);
        } else {
            puntosPage = puntoDeRecoleccionRepository.findByUsuariosAndIsDeletedFalse(recolector.get(),
                    pageable);
        }

        List<PuntoDeRecoleccionDTO> puntosDTOs = puntosPage.getContent().stream()
                .map(PuntoDeRecoleccionDTO::new)
                .collect(Collectors.toList());

        return new PaginatedResponseDTO<>(
                puntosDTOs,
                puntosPage.getTotalPages(),
                puntosPage.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize());
    }

    @Transactional
    public void desvincularPuntoDeRecolector(Long recolectorId, Long puntoId) throws UsuarioInvalidoException {
        Usuario recolector = usuarioRepository.findById(recolectorId)
                .orElseThrow(() -> new IllegalArgumentException("El recolector no existe."));
        PuntoDeRecoleccion punto = puntoDeRecoleccionRepository.findById(puntoId)
                .orElseThrow(() -> new IllegalArgumentException("El punto de recolección no existe."));

        if (!punto.getUsuarios().contains(recolector)) {
            throw new IllegalArgumentException(
                    "El punto de recolección no está asociado al recolector especificado.");
        }

        recolector.removePuntoDeRecoleccion(punto);
        puntoDeRecoleccionRepository.save(punto);
        usuarioRepository.save(recolector);
    }

    @Transactional
    public PaginatedResponseDTO<PuntoDeRecoleccionDTO> obtenerPuntosDeRecoleccionNoVinculados(
            Long recolectorId, Pageable pageable, String search)
            throws JsonProcessingException, UsuarioInvalidoException {

        Usuario recolector = usuarioRepository.findById(recolectorId)
                .orElseThrow(() -> new IllegalArgumentException("El recolector no existe."));

        Page<PuntoDeRecoleccion> puntosDeRecoleccionNoVinculados;

        if (search != null && !search.trim().isEmpty()) {
            puntosDeRecoleccionNoVinculados = puntoDeRecoleccionRepository
                    .findByUsuariosNotContainsAndIsDeletedFalseAndNombreEstablecimientoContainingIgnoreCaseOrDireccionContainingIgnoreCase(
                            recolector, search, search, pageable);
        } else {
            puntosDeRecoleccionNoVinculados = puntoDeRecoleccionRepository
                    .findByUsuariosNotContainsAndIsDeletedFalse(recolector, pageable);
        }

        List<Long> puntosIdConSolicitud = solicitudVinculacionPuntoRecoleccionRepository
                .findPuntosConSolicitudPorRecolector(recolector);

        List<PuntoDeRecoleccionDTO> content = puntosDeRecoleccionNoVinculados.getContent().stream()
                .map(punto -> {
                    PuntoDeRecoleccionDTO dto = new PuntoDeRecoleccionDTO(punto);
                    dto.setTieneSolicitud(puntosIdConSolicitud.contains(punto.getId()));
                    return dto;
                })
                .collect(Collectors.toList());

        return new PaginatedResponseDTO<>(content,
                puntosDeRecoleccionNoVinculados.getTotalPages(),
                puntosDeRecoleccionNoVinculados.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize());
    }

}
