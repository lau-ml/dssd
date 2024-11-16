package dssd.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
