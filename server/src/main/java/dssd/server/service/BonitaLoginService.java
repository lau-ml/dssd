package dssd.server.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class BonitaLoginService {

    private final RestTemplate restTemplate = new RestTemplate();

    // URL de login de Bonita
    private final String BONITA_LOGIN_URL = "http://localhost:8080/bonita/loginservice";

    // Cookie de sesión de Bonita
    private String sessionCookie = null;

    public String login() {
        if (sessionCookie == null) {
            String password = "bpm";
            String username = "walter.bates";
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(BONITA_LOGIN_URL)
                    .queryParam("username", username)
                    .queryParam("password", password)
                    .queryParam("redirect", false)
                    .queryParam("redirectURL", "");

            // Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            // Realizar la petición POST
            ResponseEntity<String> response = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            // Obtener la cookie de la respuesta
            HttpHeaders responseHeaders = response.getHeaders();
            sessionCookie = responseHeaders.getFirst(HttpHeaders.SET_COOKIE);
        }
        return sessionCookie;  // Devolver la cookie de sesión como confirmación
    }
}
