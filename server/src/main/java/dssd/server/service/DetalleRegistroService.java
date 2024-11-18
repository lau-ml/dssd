package dssd.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import dssd.server.DTO.DetalleRegistroDTO;
import dssd.server.DTO.RegistroRecoleccionDTO;
import dssd.server.exception.RegistroPendienteException;
import dssd.server.exception.UsuarioInvalidoException;
import dssd.server.helpers.BonitaState;
import dssd.server.model.*;
import dssd.server.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DetalleRegistroService {

    @Autowired
    private DetalleRegistroRepository detalleRegistroRepository;

    @Autowired
    private RegistroRecoleccionRepository registroRecoleccionRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private PuntoDeRecoleccionRepository puntoDeRecoleccionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BonitaState bonitaState;

    @Autowired
    private RolRepository rolRepository;

    @Transactional
    public RegistroRecoleccionDTO agregarDetalleRegistro(DetalleRegistroDTO detalleRegistroDTO)
            throws JsonProcessingException, UsuarioInvalidoException {

        if (!detalleRegistroDTO.validar()) {
            throw new RuntimeException("Faltan datos.");
        }

        Material material = materialRepository.findById(detalleRegistroDTO.getMaterial().getId())
                .orElseThrow(() -> new RuntimeException("Material no encontrado."));

        PuntoDeRecoleccion puntoDeRecoleccion = puntoDeRecoleccionRepository
                .findById(detalleRegistroDTO.getPuntoDeRecoleccion().getId())
                .orElseThrow(() -> new RuntimeException("Punto de recolección no encontrada."));

        Usuario recolector = userService.recuperarUsuario();

        Optional<RegistroRecoleccion> registroRecoleccionComNoVer = registroRecoleccionRepository
                .findByRecolectorAndCompletadoTrueAndVerificadoFalse(recolector);

        if (registroRecoleccionComNoVer.isPresent()) {
            String centroRecoleccion = recolector.getCentroRecoleccion().getNombre();
            String mensaje = String.format(
                    "Ya tienes un registro completado sin verificar. Por favor, acércate a tu centro de recolección asignado: %s.",
                    centroRecoleccion);
            throw new RegistroPendienteException(mensaje, "PENDING_VERIFICATION");
        }
        Optional<RegistroRecoleccion> registroRecoleccionOpt = registroRecoleccionRepository
                .findByRecolectorAndCompletadoFalse(recolector);

        RegistroRecoleccion registroRecoleccion;

        if (registroRecoleccionOpt.isPresent()) {
            registroRecoleccion = registroRecoleccionOpt.get();

        } else {
            registroRecoleccion = new RegistroRecoleccion();
            registroRecoleccion.setRecolector(recolector);
            registroRecoleccion.setIdCentroRecoleccion(recolector.getCentroRecoleccion().getId());
            registroRecoleccion.setCompletado(false);
            registroRecoleccionRepository.save(registroRecoleccion);
            this.bonitaState.instanciarProceso(recolector.getUsername());
            this.bonitaState.cargarActividadBonita();
            this.bonitaState.asignarActividadBonita();
            //this.bonitaState.set_recoleccion_cargar();
            //this.bonitaState.setIdCentroRecoleccion(recolector.getCentroRecoleccion().getId().toString());
            //this.bonitaState.set_registro_bonita_recoleccion_id(registroRecoleccion.getId().toString());
            //BonitaState.setRegistro_recoleccion_id(registroRecoleccion.getId().toString());
            //this.bonitaState.setId_recolector(recolector.getId().toString());
        }
        DetalleRegistro nuevoDetalle = new DetalleRegistro();
        nuevoDetalle.setCantidadRecolectada(detalleRegistroDTO.getCantidadRecolectada());
        nuevoDetalle.setMaterial(material);
        nuevoDetalle.setPuntoRecoleccion(puntoDeRecoleccion);
        nuevoDetalle.setRegistroRecoleccion(registroRecoleccion);

        detalleRegistroRepository.save(nuevoDetalle);

        return new RegistroRecoleccionDTO(registroRecoleccion);
    }
}
