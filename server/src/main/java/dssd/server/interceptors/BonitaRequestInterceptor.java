package dssd.server.interceptors;

import dssd.server.service.BonitaService;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BonitaRequestInterceptor implements ClientHttpRequestInterceptor {

    private final BonitaService bonitaService;

    public BonitaRequestInterceptor(BonitaService bonitaService) {
        this.bonitaService = bonitaService;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        if (request.getURI().toString().contains("bonita") && !request.getURI().toString().contains("loginservice")) {
            String sessionCookie = bonitaService.getSessionCookie();
            String apiToken = bonitaService.getApiToken();
            request.getHeaders().add("Cookie", sessionCookie);
            request.getHeaders().add("X-Bonita-API-Token", apiToken);
        }
        return execution.execute(request, body);

    }
}
