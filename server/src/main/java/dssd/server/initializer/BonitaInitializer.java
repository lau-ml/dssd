package dssd.server.initializer;

import dssd.server.model.CentroRecoleccion;
import dssd.server.model.Usuario;
import dssd.server.repository.CentroRecoleccionRepository;
import dssd.server.repository.UsuarioRepository;
import dssd.server.requests.*;
import dssd.server.service.BonitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BonitaInitializer {

    @Autowired
    private BonitaService bonitaService;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Autowired
    private CentroRecoleccionRepository centroRecoleccion;

    @Value("${BONITA_USERNAME}")
    private String BONITA_ADMIN;

    @Value("${BONITA_PASSWORD}")
    private String BONITA_PASSWORD;

    @Value("${BONITA_SUPERADMIN_USERNAME}")
    private String BONITA_SUPERADMIN_USERNAME;

    @Value("${BONITA_SUPERADMIN_PASSWORD}")
    private String BONITA_SUPERADMIN_PASSWORD;


    public ResponseEntity<?> initializeBonita() {
        bonitaService.login(LoginRequest.builder().username(BONITA_SUPERADMIN_USERNAME).password(BONITA_SUPERADMIN_PASSWORD).build());

        bonitaService.assignProfile(
                AssignProfileRequest.builder()
                        .user_id("1")
                        .member_type("USER")
                        .profile_id("2")
                        .build()
        );
        bonitaService.logoutService();
        bonitaService.login(LoginRequest.builder().username(BONITA_ADMIN).password(BONITA_PASSWORD).build());
        List<Usuario> usuarios = usuarioRepository.findAll();

        List<CentroRecoleccion> centros = centroRecoleccion.findAll();
        for (CentroRecoleccion centro : centros) {
            RegisterGroupRequest registro = RegisterGroupRequest.builder()
                            .name(centro.getNombre())
                            .description(centro.getNombre())
                            .displayName(centro.getNombre())
                            .parent_group_id("1")
                            .build();
            bonitaService.createGroup(registro);
        }
        for (Usuario usuario : usuarios) {
            RegisterBonitaRequest registerBonitaRequest = RegisterBonitaRequest.builder()
                    .firstname(usuario.getNombre())
                    .lastname(usuario.getApellido())
                    .userName(usuario.getUsername())
                    .password_confirm("123456")
                    .password("123456")
                    .enabled("true")
                    .build();
            bonitaService.createUser(registerBonitaRequest);

        }


       return bonitaService.logoutService();
    }
}
