package dssd.server.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

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
}
