package dssd.server.service;

import dssd.server.requests.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
public class BonitaService {


    // URL de login de Bonita
    @Value("${BONITA_URL}")
    private  String BONITA_URL;

    // Cookie de sesión de Bonita
    private String sessionCookie = null;


    @Getter
    private String apiToken = null;
    private final RestTemplate restTemplate;

    public BonitaService(@Lazy RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public String getSessionCookie() {
        if (sessionCookie == null) {
            login();
        }
        return sessionCookie;
    }


    private void login() {
        String password = "bpm";
        String username = "walter.bates";
        String loginUrl = BONITA_URL + "/loginservice";
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(loginUrl)
                .queryParam("username", username)
                .queryParam("password", password)
                .queryParam("redirect", false)
                .queryParam("redirectURL", "");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Realizar la petición POST para iniciar sesión
        ResponseEntity<String> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // Obtener la cookie de la respuesta y almacenarla
        HttpHeaders responseHeaders = response.getHeaders();
        List<String> valueCookies = responseHeaders.get("Set-Cookie");
        sessionCookie = valueCookies.getFirst().split(";")[0];
        apiToken = valueCookies.get(1).split(";")[0].split("=")[1];
    }

    public ResponseEntity<String> getAllProcess() {
        String url = BONITA_URL + "/API/bpm/process?p=0&c=1000";
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<String> startProcess(String processId) {
        String url = BONITA_URL + "/API/bpm/process/" + processId + "/instantiation";
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }


    public ResponseEntity<String> getProcessById(String processId) {
        String url = BONITA_URL + "/API/bpm/process/" + processId;
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<String> getProcessByName(String processName) {
        String url = BONITA_URL + "/API/bpm/process?p=0&c=1000&f=name=" + processName;
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }


    public ResponseEntity<String> getActiveProcessById(String processId) {
        String url = BONITA_URL + "/API/bpm/case?c=1&p=0&f=processId=" + processId + ",state=started";
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<String> getProcessCount() {
        String url = BONITA_URL + "/API/bpm/process?p=0&c=1000";
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }


    public ResponseEntity<String> findProcessInstancesByName(String processName) {
        String url = BONITA_URL + "/API/bpm/case?p=0&c=1000&f=processName=" + processName;
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<String> deleteProcessInstance(String id) {
        String url = BONITA_URL + "/API/bpm/case/" + id;
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<String> setVariableByCaseId(String caseId, String variableName, String variableValue) {
        String url = BONITA_URL + "/API/bpm/caseVariable/" + caseId + "/" + variableName;

        // Crea el cuerpo de la solicitud
        Map<String, String> body = Map.of(
                "type", variableValue.getClass().toString().replace("class ", ""),
                "value", variableValue
        );

        // Crea los encabezados, asegurándote de que el tipo de contenido sea JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                String.class
        );
    }


    public ResponseEntity<String> assignTask(String taskId, String userId) {
        String url = BONITA_URL + "/API/bpm/humanTask/" + taskId;
        Map<String, String> body = Map.of(
                "assigned_id", userId
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<String> searchActivityByCaseId(String caseId) {
        String url = BONITA_URL + "/API/bpm/activity?p=0&c=1000&f=caseId=" + caseId;
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<String> completeActivity(String activityId) {
        String url = BONITA_URL + "/API/bpm/userTask/" + activityId + "/execution";
        HttpEntity<?> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class);

    }

    public ResponseEntity<String> getVariableByCaseId(String caseId, String variableName) {
        String url = BONITA_URL + "/API/bpm/caseVariable/" + caseId + "/" + variableName;
        HttpEntity<?> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }


    public ResponseEntity<String> getUserByUserName(String name) {
        String url = BONITA_URL + "/API/identity/user?p=0&c=1000&f=userName=" + name;
        HttpEntity<?> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }


    public ResponseEntity<?> createUser(RegisterBonitaRequest user) {
        String url = BONITA_URL + "/API/identity/user";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<>(user, headers);
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<?> createGroup(RegisterGroupRequest group) {
        String url = BONITA_URL + "/API/identity/group";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<>(group, headers);
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<?> createRole(RegisterRoleRequest role) {
        String url = BONITA_URL + "/API/identity/role";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<>(role, headers);
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<?> createMembership(MembershipBonitaRequest membership) {
        String url = BONITA_URL + "/API/identity/membership";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<>(membership, headers);
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<?> deleteMembership(String userId, String groupId, String roleId) {
        String url = BONITA_URL + "/API/identity/membership/" + userId + "/" + groupId + "/" + roleId;
        HttpEntity<?> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                requestEntity,
                String.class
        );
    }

    //http://localhost:61589/bonita/portal/resource/app/adminAppBonita/admin-task-list/API/bpm/flowNode?c=10&p=0&f=state=failed&d=rootContainerId&d=assigned_id&t=1731800334225


    public ResponseEntity<String> failedTask() {
        String url = BONITA_URL + "/API/bpm/flowNode?c=10&p=0&f=state=failed&d=rootContainerId&d=assigned_id&t=1731800334225";
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<String> getTaskList() {
        String url = BONITA_URL + "/API/bpm/humanTask?p=0&c=1000";
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<?> getTaskListReady() {
        String url = BONITA_URL + "/API/bpm/humanTask?c=10&p=0&f=state=ready&d=rootContainerId&d=assigned_id&t=1731800334225";
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }

    /*
    http://localhost:61589/bonita/portal/resource/app/adminAppBonita/admin-task-list/API/bpm/archivedTask?c=10&p=0&d=rootContainerId&d=assigned_id&t=1731800334225&o=reached_state_date+DESC
     */

    public ResponseEntity<String> getArchivedTask() {
        String url = BONITA_URL + "/API/bpm/archivedTask?c=10&p=0&d=rootContainerId&d=assigned_id&t=1731800334225&o=reached_state_date+DESC";
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );

    }

    public ResponseEntity<String> getMembershipByUserId(String userId) {
        String url = BONITA_URL + "/API/identity/membership?p=0&c=1000&f=user_id=" + userId;
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<?> memberType(ProfileRequest memberType) {
        String url = BONITA_URL + "/API/portal/profileMember";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<>(memberType, headers);
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<?> groupSuperUser(GroupSuperUserRequest groupSuperUser) {
        String url = BONITA_URL + "/API/portal/profileMember";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<>(groupSuperUser, headers);
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<?> roleSuperUser(RoleSuperUserRequest roleSuperUser) {
        String url = BONITA_URL + "/API/portal/profileMember";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<>(roleSuperUser, headers);
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<?> profileRoleIn(ProfileRoleInRequest profileRoleIn) {

        String url = BONITA_URL + "/API/portal/profileMember";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<>(profileRoleIn, headers);
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<?> assignVariableToTask(String taskId, String variableName, String variableValue) {
        // Endpoint para actualizar la variable de la tarea
        String url = BONITA_URL + "/API/bpm/activityVariable/" + taskId + "/" + variableName;

        // Crear el cuerpo de la solicitud con el valor de la variable
        Map<String, String> body = Map.of("value", variableValue);

        // Configurar los encabezados, como el token de autenticación
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Crear la entidad de la solicitud
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // Ejecutar la solicitud PUT
        return restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<String> getVariableFromTask(String taskId, String variableName) {
        // Endpoint para obtener la variable de la tarea
        String url = BONITA_URL + "/API/bpm/activityVariable/" + taskId + "/" + variableName;

        // Configurar los encabezados, como el token de autenticación
        HttpHeaders headers = new HttpHeaders();

        // Crear la entidad de la solicitud
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Ejecutar la solicitud GET
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }


    public ResponseEntity<?> logoutService() {
        // Endpoint para cerrar la sesión
        String url = BONITA_URL + "/logoutservice";

        // Configurar los encabezados, como el token de autenticación
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Ejecutar la solicitud POST
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }
}
