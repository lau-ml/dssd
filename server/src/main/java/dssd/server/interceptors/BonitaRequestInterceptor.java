package dssd.server.interceptors;

import dssd.server.exception.UsuarioInvalidoException;
import dssd.server.model.LoginBonita;
import dssd.server.model.Usuario;
import dssd.server.repository.LoginBonitaRepository;
import dssd.server.service.EncryptionService;
import dssd.server.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component

public class BonitaRequestInterceptor implements ClientHttpRequestInterceptor {
    @Value("${BONITA_USERNAME}")
    private String BONITA_ADMIN;

    private final LoginBonitaRepository loginBonitaRepository;
    private final UserService userService;
    private final EncryptionService encryptionService;

    public BonitaRequestInterceptor(LoginBonitaRepository loginBonitaRepository, UserService userService, EncryptionService encryptionService) {
        this.loginBonitaRepository = loginBonitaRepository;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }


    @Override
    @Transactional
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        if (request.getURI().toString().contains("bonita") && !request.getURI().toString().contains("loginservice")) {
            Usuario usuario = null;
            try {
                usuario=userService.recuperarUsuario();
            } catch (UsuarioInvalidoException e) {
                usuario = loginBonitaRepository.findByUsuario_Username(BONITA_ADMIN).get().getUsuario();
            }finally {
                assert usuario != null;
                LoginBonita loginBonita = loginBonitaRepository.findByUsuario_Username(usuario.getUsername()).orElseThrow();
                String sessionCookie = encryptionService.decrypt(loginBonita.getCookie());
                String apiToken = encryptionService.decrypt(loginBonita.getSessionToken());
                request.getHeaders().add("Cookie", sessionCookie);
                request.getHeaders().add("X-Bonita-API-Token", apiToken);
            }
        }
        return execution.execute(request, body);

    }
}
