package dssd.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import dssd.server.exception.UsuarioInvalidoException;
import dssd.server.model.LoginBonita;
import dssd.server.model.Usuario;
import dssd.server.repository.LoginBonitaRepository;
import dssd.server.requests.*;
import lombok.Getter;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Value("${BONITA_USERNAME}")
    private String BONITA_ADMIN;

    @Value("${BONITA_PASSWORD}")
    private String BONITA_PASSWORD;

    @Value("${BONITA_SUPERADMIN_USERNAME}")
    private String BONITA_SUPERADMIN_USERNAME;

    @Value("${BONITA_SUPERADMIN_PASSWORD}")
    private String BONITA_SUPERADMIN_PASSWORD;
    // Cookie de sesión de Bonita

    @Autowired
    private LoginBonitaRepository loginBonitaRepository;

    @Autowired
    private EncryptionService encryptionService;


    private final RestTemplate restTemplate;

    public BonitaService(@Lazy RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Transactional
    public ResponseEntity<?> login(LoginRequest loginRequest, Usuario usuario) throws UsuarioInvalidoException {
        String loginUrl = BONITA_URL + "/loginservice";
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(loginUrl)
                .queryParam("username", loginRequest.getUsername())
                .queryParam("password", loginRequest.getPassword())
                .queryParam("redirect", false)
                .queryParam("redirectURL", "");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Realizar la petición POST para iniciar sesión
        ResponseEntity<JsonNode> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.POST,
                requestEntity,
                JsonNode.class
        );
        // Obtener la cookie de la respuesta y almacenarla
        HttpHeaders responseHeaders = response.getHeaders();
        List<String> valueCookies = responseHeaders.get("Set-Cookie");
        String sessionCookie = valueCookies.getFirst().split(";")[0];
        String apiToken = valueCookies.get(1).split(";")[0].split("=")[1];
        loginBonitaRepository.findByUsuario_Username(usuario.getUsername()).ifPresentOrElse(
                loginBonita1 -> {
                    loginBonita1.setSessionToken(encryptionService.encrypt(apiToken));
                    loginBonita1.setCookie(encryptionService.encrypt(sessionCookie));
                    loginBonitaRepository.save(loginBonita1);
                },
                () -> {
                    loginBonitaRepository.save(LoginBonita.builder()
                            .cookie(encryptionService.encrypt(sessionCookie))
                            .sessionToken(encryptionService.encrypt(apiToken))
                            .usuario(usuario)
                            .build());
                }
        );

        return response;
    }

    public ResponseEntity<?> getAllProcess() {
        String url = BONITA_URL + "/API/bpm/process?p=0&c=1000";
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                JsonNode.class
        );
    }

    public ResponseEntity<JsonNode> startProcess(String processId) {
        String url = BONITA_URL + "/API/bpm/process/" + processId + "/instantiation";
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                JsonNode.class
        );
    }


    public ResponseEntity<?> getProcessById(String processId) {
        String url = BONITA_URL + "/API/bpm/process/" + processId;
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                JsonNode.class
        );
    }

    public ResponseEntity<JsonNode> getProcessByName(String processName) {
        String url = BONITA_URL + "/API/bpm/process?p=0&c=1000&f=name=" + processName;
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                JsonNode.class
        );
    }


    public ResponseEntity<JsonNode> getActiveProcessById(String processId) {
        String url = BONITA_URL + "/API/bpm/case?c=1&p=0&f=processId=" + processId + ",state=started";
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                JsonNode.class
        );
    }

    public ResponseEntity<JsonNode> getProcessCount() {
        String url = BONITA_URL + "/API/bpm/process?p=0&c=1000";
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                JsonNode.class
        );
    }


    public ResponseEntity<JsonNode> findProcessInstancesByName(String processName) {
        String url = BONITA_URL + "/API/bpm/case?p=0&c=1000&f=processName=" + processName;
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                JsonNode.class
        );
    }

    public ResponseEntity<JsonNode> deleteProcessInstance(String id) {
        String url = BONITA_URL + "/API/bpm/case/" + id;
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                requestEntity,
                JsonNode.class
        );
    }

    public ResponseEntity<JsonNode> setVariableByCaseId(String caseId, String variableName, String variableValue) {
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
                JsonNode.class
        );
    }

    public ResponseEntity<JsonNode> getGroupByName(String name) {
        String url = BONITA_URL + "/API/identity/group?p=0&c=1000&f=name=" + name;
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                JsonNode.class
        );
    }

    public ResponseEntity<JsonNode> getRoleByName(String name) {
        String url = BONITA_URL + "/API/identity/role?p=0&c=1000&f=name=" + name;
        HttpEntity<JsonNode> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                JsonNode.class
        );
    }


    public ResponseEntity<JsonNode> getUserByUsername(String username) {
        String url = BONITA_URL + "/API/identity/user?p=0&c=1000&f=userName=" + username;
        HttpEntity<JsonNode> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                JsonNode.class
        );
    }



    public ResponseEntity<JsonNode> assignTask(String taskId, String userId) {
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
                JsonNode.class
        );
    }

    public ResponseEntity<JsonNode> searchActivityByCaseId(String caseId) {
        String url = BONITA_URL + "/API/bpm/activity?p=0&c=1000&f=caseId=" + caseId;
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                JsonNode.class
        );
    }

    public ResponseEntity<?> completeActivity(String activityId, Map<String, ?> variables) {
        String url = BONITA_URL + "/API/bpm/userTask/" + activityId + "/execution";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (variables == null) {
            variables = Map.of();
        }
        HttpEntity<Map<String, ?>> requestEntity = new HttpEntity<>(variables, headers);
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                JsonNode.class);

    }

    public ResponseEntity<?> getVariableByCaseId(String caseId, String variableName) {
        String url = BONITA_URL + "/API/bpm/caseVariable/" + caseId + "/" + variableName;
        HttpEntity<?> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                JsonNode.class
        );
    }


    public ResponseEntity<JsonNode> getUserByUserName(String name) {
        String url = BONITA_URL + "/API/identity/user?p=0&c=1000&f=userName=" + name;
        HttpEntity<?> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                JsonNode.class
        );
    }


    public ResponseEntity<JsonNode> createUser(RegisterBonitaRequest user) {
        String url = BONITA_URL + "/API/identity/user";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<>(user, headers);
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                JsonNode.class
        );
    }

    public ResponseEntity<JsonNode> createGroup(RegisterGroupRequest group) {
        String url = BONITA_URL + "/API/identity/group";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<>(group, headers);
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                JsonNode.class
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
                JsonNode.class
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
                JsonNode.class
        );
    }

    public ResponseEntity<?> deleteMembership(String userId, String groupId, String roleId) {
        String url = BONITA_URL + "/API/identity/membership/" + userId + "/" + groupId + "/" + roleId;
        HttpEntity<?> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                requestEntity,
                JsonNode.class
        );
    }

    //http://localhost:61589/bonita/portal/resource/app/adminAppBonita/admin-task-list/API/bpm/flowNode?c=10&p=0&f=state=failed&d=rootContainerId&d=assigned_id&t=1731800334225


    public ResponseEntity<?> failedTask() {
        String url = BONITA_URL + "/API/bpm/flowNode?c=10&p=0&f=state=failed&d=rootContainerId&d=assigned_id&t=1731800334225";
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                JsonNode.class
        );
    }

    public ResponseEntity<?> getTaskList() {
        String url = BONITA_URL + "/API/bpm/humanTask?p=0&c=1000";
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                JsonNode.class
        );
    }

    public ResponseEntity<?> getTaskListReady() {
        String url = BONITA_URL + "/API/bpm/humanTask?c=10&p=0&f=state=ready&d=rootContainerId&d=assigned_id&t=1731800334225";
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                JsonNode.class
        );
    }

    /*
    http://localhost:61589/bonita/portal/resource/app/adminAppBonita/admin-task-list/API/bpm/archivedTask?c=10&p=0&d=rootContainerId&d=assigned_id&t=1731800334225&o=reached_state_date+DESC
     */

    public ResponseEntity<?> getArchivedTask() {
        String url = BONITA_URL + "/API/bpm/archivedTask?c=10&p=0&d=rootContainerId&d=assigned_id&t=1731800334225&o=reached_state_date+DESC";
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                JsonNode.class
        );

    }

    public ResponseEntity<?> getMembershipByUserId(String userId) {
        String url = BONITA_URL + "/API/identity/membership?p=0&c=1000&f=user_id=" + userId;
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                JsonNode.class
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
                JsonNode.class
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
                JsonNode.class
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
                JsonNode.class
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
                JsonNode.class
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
                JsonNode.class
        );
    }

    public ResponseEntity<?> getVariableFromTask(String taskId, String variableName) {
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
                JsonNode.class
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
                JsonNode.class
        );
    }

    public ResponseEntity<?> assignProfile(AssignProfileRequest assignProfileRequest) {
        String url = BONITA_URL + "/API/portal/profileMember";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<>(assignProfileRequest, headers);
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                JsonNode.class
        );
    }

    public ResponseEntity<JsonNode> assignProfileGroup(AssignProfileGroupRequest assignProfileGroupRequest) {
        String url = BONITA_URL + "/API/portal/profileMember";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<>(assignProfileGroupRequest, headers);
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                JsonNode.class
        );
    }

    //http://localhost:8080/bonita/API/identity/personalcontactdata/248
    public ResponseEntity<JsonNode> putPersonalContactData(String userId, PersonalContactDataRequest personalContactDataRequest) {
        String url = BONITA_URL + "/API/identity/personalcontactdata/" + userId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<>(personalContactDataRequest, headers);
        return restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                JsonNode.class
        );
    }

    public ResponseEntity<JsonNode> searchActivityByCaseIdAndRoot(String caseId, String rootCaseId) {
        String url = BONITA_URL + "/API/bpm/activity?p=0&c=1000&f=caseId=" + rootCaseId + "&f=parentCaseId=" + caseId;
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                JsonNode.class
        );
    }
}
