package dssd.server.service;

import lombok.Getter;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class BonitaService {



    // URL de login de Bonita
    private final String BONITA_URL = "http://localhost:8080/bonita/";

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
        String loginUrl = BONITA_URL + "loginservice";
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

    public ResponseEntity<String> getAllProcess(){
        String url = BONITA_URL + "API/bpm/process?p=0&c=1000";
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<String> startProcess(String processId){
        String url = BONITA_URL + "API/bpm/process/" + processId + "/instantiation";
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }


    public ResponseEntity<String> getProcessById(String processId){
        String url = BONITA_URL + "API/bpm/process/" + processId;
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<String> getProcessByName(String processName){
        String url = BONITA_URL + "API/bpm/process?p=0&c=1000&f=name=" + processName;
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }


    public ResponseEntity<String> getActiveProcessById(String processId){
        String url = BONITA_URL + "API/bpm/case?c=1&p=0&f=processId=" + processId + ",state=started";
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<String> getProcessCount(){
        String url = BONITA_URL + "API/bpm/process?p=0&c=1000";
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<String> enableProcess(String id) {
        String url = BONITA_URL + "API/bpm/process/" + id + "/enable";
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<String> findProcessInstancesByName(String processName) {
        String url = BONITA_URL + "API/bpm/case?p=0&c=1000&f=processName=" + processName;
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<String> deleteProcessInstance(String id) {
        String url = BONITA_URL + "API/bpm/case/" + id;
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<String> setVariableByCaseId(String caseId, String variableName, String variableValue) {
        String url = BONITA_URL + "API/bpm/caseVariable/" + caseId + "/" + variableName;
        HttpEntity<String> requestEntity = new HttpEntity<>(variableValue, null);
        return restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<String> assignTask(String taskId, String userId) {
        String url = BONITA_URL + "API/bpm/humanTask/" + taskId + "/assign";
        HttpEntity<String> requestEntity = new HttpEntity<>(userId, null);
        return restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<String> searchActivityByCaseId(String caseId) {
        String url = BONITA_URL + "API/bpm/activity?p=0&c=1000&f=caseId=" + caseId;
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<String> completeActivity(String activityId) {
        String url = BONITA_URL + "API/bpm/userTask/" + activityId + "/execution";
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                String.class
        );

    }

    public ResponseEntity<String> getVariableByCaseId(String caseId, String variableName) {
        String url = BONITA_URL + "API/bpm/caseVariable/" + caseId + "/" + variableName;
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<String> setVariableByTaskId(String id, String variableName, String variableValue) {
        String url = BONITA_URL + "API/bpm/caseVariable/" + id + "/" + variableName;
        HttpEntity<String> requestEntity = new HttpEntity<>(variableValue, null);
        return restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                String.class
        );
    }
}
