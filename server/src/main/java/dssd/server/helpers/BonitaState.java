package dssd.server.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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

    private static final String nombre = "Proceso de recolección y recepción de materiales";

    private static final ObjectMapper objectMapper = new ObjectMapper();


    @Transactional
    public void instanciarProceso(String userName, RegistroRecoleccion registroRecoleccion) throws JsonProcessingException, UsuarioInvalidoException {
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
        } else {
            throw new JsonProcessingException("No se encontró el proceso en la respuesta") {
            };
        }

    }


    public JsonNode buscarActividadBonita(String caseId) throws JsonProcessingException {
        return this.bonitaService.searchActivityByCaseId(caseId).getBody();
    }

    private void asignarActividadBonita(String idActividadBonita, String idUser) throws JsonProcessingException {
        this.bonitaService.assignTask(idActividadBonita, idUser);
    }

    private void completarActividadBonita(String idActividadBonita, Map<String, ?> variables) {
        this.bonitaService.completeActivity(idActividadBonita, variables);
    }

    @Transactional
    public void confirmarRegistroRecoleccionBonita(RegistroRecoleccion registroRecoleccion) throws JsonProcessingException {
        // Buscar la tarea Bonita asociada con el registro de recolección
        TareaBonita tareaBonita = tareaBonitaRepository.findByRegistroRecoleccion_Id(registroRecoleccion.getId()).orElseThrow();

        // Agrupar los materiales y calcular la cantidad total
        List<Map<String, Object>> materialesCargadosContrato = registroRecoleccion.getDetalleRegistros().stream()
                .collect(Collectors.groupingBy(DetalleRegistro::getMaterial))
                .entrySet()
                .stream()
                .map(entry -> {
                    // Sumar las cantidades recolectadas para cada material
                    Integer cantidad = entry.getValue().stream()
                            .mapToInt(DetalleRegistro::getCantidadRecolectada)
                            .sum();
                    // Crear un mapa para cada material con su ID y la cantidad
                    Map<String, Object> materialMap = new HashMap<>();
                    materialMap.put("material_id", entry.getKey().getId()); // ID del material
                    materialMap.put("cantidad_cargada", cantidad);
                    materialMap.put("material_nombre", entry.getKey().getNombre()); // Cantidad total recolectada
                    materialMap.put("tipo", "cargado");
                    // Cantidad total recolectada
                    return materialMap;
                })
                .collect(Collectors.toList()); // Convertir el stream en una lista

        // Preparar las variables para Bonita
        Map<String, Object> variables = Map.of(
                "registro_recoleccion_id_contrato", registroRecoleccion.getId(), // ID del registro
                "estado_recoleccion_contrato", EstadoPedidoRecoleccion.confirmar.toString(), // Estado de la recolección (confirmado)
                "id_recolector_contrato", registroRecoleccion.getRecolector().getId(), // ID del recolector
                "id_centro_recoleccion_contrato", registroRecoleccion.getRecolector().getCentroRecoleccion().getId(), // ID del centro de recolección
                "materiales_cargados_contrato", materialesCargadosContrato // Lista de materiales (en formato JSON serializable)
        );

        // Completar la actividad de Bonita con las variables preparadas
        completarActividadBonita(tareaBonita.getId_tarea_bonita(), variables);
    }

    @Transactional
    public void eliminarRegistroBonita(RegistroRecoleccion registroRecoleccion) {
        // Obtener la tarea Bonita asociada
        TareaBonita tareaBonita = tareaBonitaRepository.findByRegistroRecoleccion_Id(registroRecoleccion.getId()).orElseThrow();

        // Agrupar los materiales y calcular cantidades totales
        List<Map<String, Object>> materialesCargadosContrato = registroRecoleccion.getDetalleRegistros().stream()
                .collect(Collectors.groupingBy(DetalleRegistro::getMaterial))
                .entrySet()
                .stream()
                .map(entry -> {
                    Integer cantidad = entry.getValue().stream()
                            .mapToInt(DetalleRegistro::getCantidadRecolectada)
                            .sum();
                    // Asegurarse de que el mapa sea de tipo Map<String, Object>
                    Map<String, Object> materialMap = new HashMap<>();
                    materialMap.put("material_id", entry.getKey().getId()); // ID del material
                    materialMap.put("cantidad_cargada", cantidad);                  // Cantidad total recolectada
                    materialMap.put("material_nombre", entry.getKey().getNombre());
                    materialMap.put("tipo", "cargado");

                    return materialMap;
                })
                .collect(Collectors.toList()); // Convertir el stream en una lista

        // Preparar las variables para Bonita
        Map<String, Object> variables = Map.of(
                "registro_recoleccion_id_contrato", registroRecoleccion.getId(), // ID del registro
                "estado_recoleccion_contrato", EstadoPedidoRecoleccion.cancelar.toString(), // Estado del pedido
                "id_recolector_contrato", registroRecoleccion.getRecolector().getId(), // ID del recolector
                "id_centro_recoleccion_contrato", registroRecoleccion.getRecolector().getCentroRecoleccion().getId(), // ID del centro
                "materiales_cargados_contrato", materialesCargadosContrato // Lista de materiales (JSON serializable)
        );

        // Completar la actividad en Bonita con todas las variables
        completarActividadBonita(tareaBonita.getId_tarea_bonita(), variables);
    }

    @Transactional
    public void completarActividadRecepcionBonita(RegistroRecoleccion registroRecoleccion) throws UsuarioInvalidoException {
        TareaBonita tareaBonitaAsociada = tareaBonitaRepository.findByRegistroRecoleccion_Id(registroRecoleccion.getId()).orElseThrow();
        Usuario usuario = userService.recuperarUsuario();
        List<Map<String, Object>> materialesCargadosContrato = registroRecoleccion.getDetalleRegistros().stream()
                .collect(Collectors.groupingBy(DetalleRegistro::getMaterial))
                .entrySet()
                .stream()
                .map(entry -> {
                    Integer cantidad = entry.getValue().stream()
                            .mapToInt(DetalleRegistro::getCantidadRecibida)
                            .sum();
                    // Asegurarse de que el mapa sea de tipo Map<String, Object>
                    Map<String, Object> materialMap = new HashMap<>();
                    materialMap.put("material_id", entry.getKey().getId()); // ID del material
                    materialMap.put("cantidad_cargada", cantidad);                  // Cantidad total recolectada
                    materialMap.put("material_nombre", entry.getKey().getNombre());
                    materialMap.put("tipo", "recibido");

                    return materialMap;
                })
                .toList(); // Convertir el stream en una lista
        String idUser = Objects.requireNonNull(bonitaService.getUserByUserName(usuario.getUsername()).getBody()).get(0).get("id").asText();
        String activityId = this.bonitaService.searchActivityByCaseId(tareaBonitaAsociada.getCaseId()).getBody().get(0).get("id").asText();
        TareaBonita tareaNueva = tareaBonitaRepository.save(TareaBonita.builder().id_user_bonita(idUser).id_tarea_bonita(activityId).registroRecoleccion(registroRecoleccion).caseId(tareaBonitaAsociada.getCaseId()).usuario(usuario).build());
        bonitaService.assignTask(activityId, idUser);
        Map<String, Object> variables = Map.of(
                "materiales_recibidos_contrato", materialesCargadosContrato);
        completarActividadBonita(tareaNueva.getId_tarea_bonita(), variables);
    }
}
