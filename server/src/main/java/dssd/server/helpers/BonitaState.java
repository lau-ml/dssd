package dssd.server.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dssd.server.exception.UsuarioInvalidoException;
import dssd.server.model.*;
import dssd.server.repository.TareaBonitaRepository;
import dssd.server.service.BonitaService;
import dssd.server.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Getter
@Setter
public class BonitaState {
    @Autowired
    private BonitaService bonitaService;

    @Autowired
    private TareaBonitaRepository tareaBonitaRepository;

    @Autowired
    private UserService userService;

    private static final String  nombre ="Proceso de recolección y recepción de materiales";

    private static final ObjectMapper objectMapper=new ObjectMapper();




    public  void instanciarProceso(String userName, RegistroRecoleccion registroRecoleccion) throws JsonProcessingException, UsuarioInvalidoException {
        JsonNode responseNode = this.bonitaService.getProcessByName(nombre).getBody();

        // Asegúrate de que el JSON no esté vacío
        if (responseNode != null && responseNode.isArray() && !responseNode.isEmpty()) {
            String idProcess = responseNode.get(0).get("id").asText();
            String idCase = Objects.requireNonNull(this.bonitaService.startProcess(idProcess).getBody()).get("caseId").asText();
            String idUser = Objects.requireNonNull(this.bonitaService.getUserByUserName(userName).getBody()).get(0).get("id").asText();
            String idActividadBonita = Objects.requireNonNull(this.bonitaService.searchActivityByCaseId(idCase).getBody()).get(0).get("id").asText();
            TareaBonita tareaBonita = TareaBonita.builder().id_tarea_bonita(idActividadBonita).caseId(idCase).id_user_bonita(idUser).usuario(userService.recuperarUsuario()).registroRecoleccion(registroRecoleccion).build();
            asignarActividadBonita(idActividadBonita, idUser);
            tareaBonitaRepository.save(tareaBonita);
        } else {throw new JsonProcessingException("No se encontró el proceso en la respuesta") {};
        }

    }





    private void asignarActividadBonita(String idActividadBonita, String idUser) throws JsonProcessingException {
        this.bonitaService.assignTask(idActividadBonita, idUser);
    }

    private void completarActividadBonita(String idActividadBonita, Map<String, ?> variables){
        this.bonitaService.completeActivity(idActividadBonita,variables );
    }


    public void confirmarRegistroRecoleccionBonita(RegistroRecoleccion registroRecoleccion) throws JsonProcessingException {
        TareaBonita tareaBonita = tareaBonitaRepository.findByRegistroRecoleccion(registroRecoleccion).orElseThrow();
        registroRecoleccion.getDetalleRegistros().stream().collect(Collectors.groupingBy(DetalleRegistro::getMaterial)).forEach((material, detalleRegistros) -> {
            Integer cantidad = detalleRegistros.stream().mapToInt(DetalleRegistro::getCantidadRecolectada).sum();
            Map<String, Object> variables = Map.of("registro_recoleccion_id_contrato", registroRecoleccion.getId(),
                    "estado_recoleccion_contrato", EstadoPedidoRecoleccion.confirmar.toString(),
                    "id_recolector_contrato", registroRecoleccion.getRecolector().getId(),
                    "id_centro_recoleccion_contrato", registroRecoleccion.getRecolector().getCentroRecoleccion().getId(),
                    "materiales_cargados_contrato", List.of(Map.of("material_id", material.getId(), "cantidad", cantidad)));
            completarActividadBonita(tareaBonita.getId_tarea_bonita(), variables);
        });
    }

    public void eliminarRegistroBonita(RegistroRecoleccion registroRecoleccion) {
        TareaBonita tareaBonita = tareaBonitaRepository.findByRegistroRecoleccion(registroRecoleccion).orElseThrow();
        registroRecoleccion.getDetalleRegistros().stream().collect(Collectors.groupingBy(DetalleRegistro::getMaterial)).forEach((material, detalleRegistros) -> {
            Integer cantidad = detalleRegistros.stream().mapToInt(DetalleRegistro::getCantidadRecolectada).sum();
            Map<String, Object> variables = Map.of("registro_recoleccion_id_contrato", registroRecoleccion.getId(),
                    "estado_recoleccion_contrato", EstadoPedidoRecoleccion.cancelar.toString(),
                    "id_recolector_contrato", registroRecoleccion.getRecolector().getId(),
                    "id_centro_recoleccion_contrato", registroRecoleccion.getRecolector().getCentroRecoleccion().getId(),
                    "materiales_cargados_contrato", List.of(Map.of("material_id", material.getId(), "cantidad", cantidad)));
            completarActividadBonita(tareaBonita.getId_tarea_bonita(), variables);
        });}
}
