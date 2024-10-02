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

    private String caseId;
    private String userId;

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
        ObjectMapper objectMapper = new ObjectMapper();

        if (registroRecoleccionOpt.isPresent()) {
            registroRecoleccion = registroRecoleccionOpt.get();
        } else {
            registroRecoleccion = new RegistroRecoleccion();
            registroRecoleccion.setRecolector(recolector);
            registroRecoleccion.setIdCentroRecoleccion(recolector.getCentroRecoleccion().getId());
            registroRecoleccion.setCompletado(false);
            registroRecoleccionRepository.save(registroRecoleccion);
            String procesoId=objectMapper.readValue(this.bonitaService.getProcessByName("Proceso de recolección y recepción de materiales").getBody(),  new TypeReference<List<ProcessBonita>>() {}).getFirst().getId();
            this.caseId = objectMapper.readValue(this.bonitaService.startProcess(procesoId).getBody(), Case.class).getCaseId();
            String estado_recoleccion = "cargando";
            this.bonitaService.setVariableByCaseId(this.caseId, "registro_recoleccion_id", registroRecoleccion.getId().toString());
            this.bonitaService.setVariableByCaseId(this.caseId, "estado_recoleccion", estado_recoleccion);
            this.userId= objectMapper.readValue(this.bonitaService.getUserByUserName("walter.bates").getBody(), new TypeReference<List<UserBonita>>() {}).getFirst().getId();

        }
        List<ActividadBonita> actividades=objectMapper.readValue(this.bonitaService.searchActivityByCaseId(this.caseId).getBody(), new TypeReference<List<ActividadBonita>>() {});
        this.bonitaService.assignTask(actividades.getFirst().getId(), this.userId);
        this.bonitaService.completeActivity(actividades.getFirst().getId());

        DetalleRegistro nuevoDetalle = new DetalleRegistro();
        nuevoDetalle.setCantidadRecolectada(detalleRegistroDTO.getCantidadRecolectada());
        nuevoDetalle.setMaterial(material);
        nuevoDetalle.setUbicacion(ubicacion);
        nuevoDetalle.setRegistroRecoleccion(registroRecoleccion);

        detalleRegistroRepository.save(nuevoDetalle);


        return new RegistroRecoleccionDTO(registroRecoleccion);
    }
}
