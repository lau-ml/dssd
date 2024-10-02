package dssd.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dssd.server.DTO.DetalleRegistroDTO;
import dssd.server.DTO.RegistroRecoleccionDTO;
import dssd.server.helpers.ActividadBonita;
import dssd.server.helpers.Case;
import dssd.server.helpers.ProcessBonita;
import dssd.server.helpers.UserBonita;
import dssd.server.model.*;
import dssd.server.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DetalleRegistroService {

    @Autowired
    private BonitaService bonitaService;

    @Autowired
    private DetalleRegistroRepository detalleRegistroRepository;

    @Autowired
    private RegistroRecoleccionRepository registroRecoleccionRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Autowired
    private RecolectorRepository recolectorRepository;

    @Transactional

    public RegistroRecoleccionDTO agregarDetalleRegistro(DetalleRegistroDTO detalleRegistroDTO) throws JsonProcessingException {

        if (!detalleRegistroDTO.validar()) {
            throw new RuntimeException("Faltan datos.");
        }
        Material material = materialRepository.findById(detalleRegistroDTO.getMaterial().getId())
                .orElseThrow(() -> new RuntimeException("Material no encontrado."));

        Ubicacion ubicacion = ubicacionRepository.findById(detalleRegistroDTO.getUbicacion().getId())
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada."));

        Optional<RegistroRecoleccion> registroRecoleccionOpt;

        Recolector recolector = recolectorRepository.findById(detalleRegistroDTO.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Recolector no encontrado."));
        registroRecoleccionOpt = registroRecoleccionRepository.findTopByRecolectorAndCompletadoFalseOrderByFechaRecoleccionDesc(recolector);

        RegistroRecoleccion registroRecoleccion;
        if (registroRecoleccionOpt.isPresent()) {
            registroRecoleccion = registroRecoleccionOpt.get();
        } else {
            registroRecoleccion = new RegistroRecoleccion();
            registroRecoleccion.setRecolector(recolector);
            registroRecoleccion.setIdCentroRecoleccion(recolector.getCentroRecoleccion().getId());
            registroRecoleccion.setCompletado(false);
            registroRecoleccionRepository.save(registroRecoleccion);
            ObjectMapper objectMapper = new ObjectMapper();
            List<ProcessBonita> procesos =objectMapper.readValue(this.bonitaService.getProcessByName("Proceso de recolección y recepción de materiales").getBody(),  new TypeReference<List<ProcessBonita>>() {});
            String caseId = objectMapper.readValue(this.bonitaService.startProcess(procesos.getFirst().getId()).getBody(), Case.class).getCaseId();
            String estado_recoleccion = "cargando";
            this.bonitaService.setVariableByCaseId(caseId, "estado_recoleccion", estado_recoleccion);
            this.bonitaService.setVariableByCaseId(caseId, "registro_recoleccion_id", registroRecoleccion.getId().toString());
            List<UserBonita> users = objectMapper.readValue(this.bonitaService.getUserByUserName("walter.bates").getBody(), new TypeReference<List<UserBonita>>() {});
            List<ActividadBonita> actividades=objectMapper.readValue(this.bonitaService.searchActivityByCaseId(caseId).getBody(), new TypeReference<List<ActividadBonita>>() {});
            this.bonitaService.assignTask(actividades.getFirst().getId(), users.getFirst().getId());
            this.bonitaService.completeActivity(actividades.getFirst().getId());


        }


        DetalleRegistro nuevoDetalle = new DetalleRegistro();
        nuevoDetalle.setCantidadRecolectada(detalleRegistroDTO.getCantidadRecolectada());
        nuevoDetalle.setMaterial(material);
        nuevoDetalle.setUbicacion(ubicacion);
        nuevoDetalle.setRegistroRecoleccion(registroRecoleccion);

        detalleRegistroRepository.save(nuevoDetalle);


        return new RegistroRecoleccionDTO(registroRecoleccion);
    }
}
