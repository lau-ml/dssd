package dssd.server.interceptors;

import dssd.server.exception.UsuarioInvalidoException;
import dssd.server.model.LoginBonita;
import dssd.server.model.Usuario;
import dssd.server.repository.LoginBonitaRepository;
import dssd.server.service.BonitaService;
import dssd.server.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
@AllArgsConstructor
public class BonitaRequestInterceptor implements ClientHttpRequestInterceptor {

    private final LoginBonitaRepository loginBonitaRepository;

    private final UserService userService;

    @Override
    @Transactional
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        if (request.getURI().toString().contains("bonita") && !request.getURI().toString().contains("loginservice")) {
            try {
                Usuario usuario = userService.recuperarUsuario();
                LoginBonita loginBonita= loginBonitaRepository.findByUsuario_Username(usuario.getUsername()).orElseThrow();
                String sessionCookie = loginBonita.getCookie();
                String apiToken = loginBonita.getSessionToken();
                request.getHeaders().add("Cookie", sessionCookie);
                request.getHeaders().add("X-Bonita-API-Token", apiToken);
            } catch (UsuarioInvalidoException e) {
                throw new RuntimeException(e);
            }
        }
        return execution.execute(request, body);

    }
}
