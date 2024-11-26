package dssd.server.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import dssd.server.DTO.PaginatedResponseDTO;
import dssd.server.DTO.RecolectorAdminDTO;
import dssd.server.DTO.UsuarioDTO;
import dssd.server.exception.UsuarioInvalidoException;
import dssd.server.model.CentroRecoleccion;
import dssd.server.model.Rol;
import dssd.server.model.Usuario;
import dssd.server.repository.RegistroRecoleccionRepository;
import dssd.server.repository.RolRepository;
import dssd.server.repository.UsuarioRepository;

@Service
public class RecolectorService {
    @Autowired
    private UsuarioRepository recolectorRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RegistroRecoleccionRepository registroRecoleccionRepository;

    @Transactional
    public List<UsuarioDTO> obtenerRecolectoresDelCentroDelUsuarioActual()
            throws JsonProcessingException, UsuarioInvalidoException {

        Usuario usuarioActual = userService.recuperarUsuario();
        CentroRecoleccion centroRecoleccion = usuarioActual.getCentroRecoleccion();
        Optional<Rol> rolRecolector = rolRepository.findByNombre("ROLE_RECOLECTOR");

        return recolectorRepository.findByRolAndCentroRecoleccion(rolRecolector.get(), centroRecoleccion)
                .stream()
                .map(usuario -> {
                    boolean tieneRegistroCompletoPendiente = registroRecoleccionRepository
                            .findTopByRecolectorIdAndCompletadoTrueAndVerificadoFalseOrderByFechaRecoleccionDesc(
                                    usuario.getId())
                            .isPresent();

                    return new UsuarioDTO(usuario, tieneRegistroCompletoPendiente);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public PaginatedResponseDTO<UsuarioDTO> obtenerRecolectoresDelCentroDelUsuarioActualPaginadosYFiltrados(
            Pageable pageable,
            String search)
            throws JsonProcessingException, UsuarioInvalidoException {

        Usuario usuarioActual = userService.recuperarUsuario();
        CentroRecoleccion centroRecoleccion = usuarioActual.getCentroRecoleccion();
        Optional<Rol> rolRecolector = rolRepository.findByNombre("ROLE_RECOLECTOR");

        Page<Usuario> recolectoresPage;
        if (search != null && !search.trim().isEmpty()) {
            recolectoresPage = recolectorRepository
                    .findByRolAndCentroRecoleccionAndActivoAndNombreContainingIgnoreCaseOrDniContainingIgnoreCase(
                            rolRecolector.get(), centroRecoleccion, true, search, search, pageable);
        } else {
            recolectoresPage = recolectorRepository.findByRolAndCentroRecoleccionAndActivo(rolRecolector.get(),
                    centroRecoleccion, true, pageable);
        }

        List<UsuarioDTO> recolectoresDTOs = recolectoresPage.getContent().stream()
                .map(usuario -> {
                    boolean tieneRegistroCompletoPendiente = registroRecoleccionRepository
                            .findTopByRecolectorIdAndCompletadoTrueAndVerificadoFalseOrderByFechaRecoleccionDesc(
                                    usuario.getId())
                            .isPresent();

                    return new UsuarioDTO(usuario, tieneRegistroCompletoPendiente);
                })
                .collect(Collectors.toList());

        return new PaginatedResponseDTO<>(
                recolectoresDTOs,
                recolectoresPage.getTotalPages(),
                recolectoresPage.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize());
    }

    public List<UsuarioDTO> obtenerTodosRecolectores() {

        return recolectorRepository.findByRol_Nombre("ROLE_RECOLECTOR")
                .stream()
                .map(UsuarioDTO::new)
                .collect(Collectors.toList());
    }

    public UsuarioDTO modificarRecolector(Long id) {
        return null;
    }

    public void crearRecolector(UsuarioDTO recolector) {
        Optional<Rol> rolRecolector = rolRepository.findByNombre("ROLE_RECOLECTOR");
        Usuario usuario = new Usuario();
        recolectorRepository.save(usuario);
    }

    public PaginatedResponseDTO<RecolectorAdminDTO> obtenerRecolectoresPaginadosYFiltrados(Pageable pageable,
            String search) {
        Optional<Rol> rolRecolector = rolRepository.findByNombre("ROLE_RECOLECTOR");

        if (rolRecolector.isEmpty()) {
            throw new IllegalStateException("El rol ROLE_RECOLECTOR no está configurado en la base de datos.");
        }

        Page<Usuario> recolectoresPage;

        if (search != null && !search.trim().isEmpty()) {
            recolectoresPage = recolectorRepository.findByNombreContainingIgnoreCaseAndRol(search,
                    rolRecolector.get(), pageable);
        } else {
            recolectoresPage = recolectorRepository.findByRol(rolRecolector.get(), pageable);
        }

        List<RecolectorAdminDTO> recolectoresDTOs = recolectoresPage.getContent().stream()
                .map(RecolectorAdminDTO::new)
                .collect(Collectors.toList());

        return new PaginatedResponseDTO<>(
                recolectoresDTOs,
                recolectoresPage.getTotalPages(),
                recolectoresPage.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize());
    }

    public RecolectorAdminDTO obtenerRecolectorPorId(Long id) throws UsuarioInvalidoException {
        Optional<Usuario> recolector = recolectorRepository.findById(id);
        if (!recolector.isPresent()) {
            throw new UsuarioInvalidoException("Recolector no encontrado");
        }
        return new RecolectorAdminDTO(recolector.get());
    }

    public void cambiarEstadoActivo(Long id, boolean activo) throws UsuarioInvalidoException {
        Usuario recolector = recolectorRepository.findById(id)
                .orElseThrow(() -> new UsuarioInvalidoException("Recolector no encontrado"));

        if (!recolector.getRol().getNombre().equals("ROLE_RECOLECTOR")) {
            throw new UsuarioInvalidoException("El usuario no es un recolector");
        }

        recolector.setActivo(activo);
        recolectorRepository.save(recolector);
    }

}
