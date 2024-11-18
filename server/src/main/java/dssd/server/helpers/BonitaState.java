package dssd.server.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dssd.server.model.*;
import dssd.server.service.BonitaService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@Getter
@Setter
public class BonitaState {
    @Autowired
    private BonitaService bonitaService;

    private static final String  nombre ="Proceso de recolección y recepción de materiales";

    private static String idProcess;

    private  static String idCase;
    private static final ObjectMapper objectMapper=new ObjectMapper();


    @Setter
    private static String registro_recoleccion_id;

    private static String id_recolector;

    private static EstadoPedidoRecoleccion estado_recoleccion;
    private static String idActivity;

    private static String idActividadBonita;

    private static String idCentroRecoleccion;

    private static String idUser;

    public  void instanciarProceso() throws JsonProcessingException {
        JsonNode responseNode = this.bonitaService.getProcessByName(nombre).getBody();

        // Asegúrate de que el JSON no esté vacío
        if (responseNode != null && responseNode.isArray() && !responseNode.isEmpty()) {
            // Accede al primer elemento del array y extrae el ID
            idProcess = responseNode.get(0).get("id").asText();
            idCase = Objects.requireNonNull(this.bonitaService.startProcess(idProcess).getBody()).get("caseId").asText();
            idUser = Objects.requireNonNull(this.bonitaService.getUserByUserName("walter.bates").getBody()).get(0).get("id").asText();

        } else {
            // Si no hay procesos o el JSON es vacío, maneja el caso de error
            throw new JsonProcessingException("No se encontró el proceso en la respuesta") {};
        }

    }

    public void set_recoleccion_cancelar(){
        estado_recoleccion=EstadoPedidoRecoleccion.cancelar;
        this.set_estado_bonita_recoleccion();
    }

    public void set_recoleccion_confirmar(){
        estado_recoleccion=EstadoPedidoRecoleccion.confirmar;
        this.set_estado_bonita_recoleccion();
    }

    public void set_recoleccion_cargar(){
        estado_recoleccion=EstadoPedidoRecoleccion.cargar;
        this.set_estado_bonita_recoleccion();
    }

    private void set_estado_bonita_recoleccion(){
        this.bonitaService.setVariableByCaseId(idCase, "estado_recoleccion", estado_recoleccion.valorActual());
    }

    public void setIdCentroRecoleccion(String id){
        idCentroRecoleccion=id;
        this.bonitaService.setVariableByCaseId(idCase, "id_centro_recoleccion", id);
    }

    public void setId_recolector(String id){
        id_recolector=id;
        this.bonitaService.setVariableByCaseId(idCase, "id_recolector", id);
    }



    public void set_registro_bonita_recoleccion_id(String id){
        registro_recoleccion_id=id;
        this.bonitaService.setVariableByCaseId(idCase, "registro_recoleccion_id", id);
    }


    public void cargarActividadBonita() throws JsonProcessingException {
        JsonNode responseNode = this.bonitaService.searchActivityByCaseId(idCase).getBody();

        // Asegúrate de que el JSON no esté vacío
        if (responseNode != null && responseNode.isArray() && !responseNode.isEmpty()) {
            // Accede al primer elemento del array y extrae el ID
            idActividadBonita = responseNode.get(0).get("id").asText();
        } else {
            // Si no hay actividades o el JSON es vacío, maneja el caso de error
            throw new JsonProcessingException("No se encontró la actividad en la respuesta") {};
        }
    }


    public void asignarActividadBonita() throws JsonProcessingException {
        this.bonitaService.assignTask(idActividadBonita, idUser);
    }

    public void completarActividadBonita(){
        this.bonitaService.completeActivity(idActividadBonita);
    }



}
