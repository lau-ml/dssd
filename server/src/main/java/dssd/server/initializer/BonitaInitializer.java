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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


        String idRecolector = bonitaService.getRoleByName("Recolector").getBody().get(0).get("id").asText();
        String idAdmin = bonitaService.getRoleByName("Admin").getBody().get(0).get("id").asText();
        //String superAdmin = bonitaService.getRoleByName("SuperAdmin").getBody().get(0).get("id").asText();
        String idEmpleado = bonitaService.getRoleByName("Empleado").getBody().get(0).get("id").asText();
        Map<String, String> roles = Map.of(
                "Recolector", idRecolector,
                "Admin", idAdmin,
                //"SuperAdmin", superAdmin,
                "Empleado", idEmpleado
        );

        ProfileRoleInRequest profileRoleInRequest = ProfileRoleInRequest.builder()
                .group_id("1")
                .profile_id("1")
                .role_id(idAdmin)
                .build();
        ProfileRoleInRequest profileRoleInRequest2 = ProfileRoleInRequest.builder()
                .group_id("1")
                .profile_id("1")
                .role_id(idEmpleado)
                .build();

        ProfileRoleInRequest profileRoleInRequest3 = ProfileRoleInRequest.builder()
                .group_id("1")
                .profile_id("1")
                .role_id(idRecolector)
                .build();

        bonitaService.profileRoleIn(profileRoleInRequest);
        bonitaService.profileRoleIn(profileRoleInRequest2);
        bonitaService.profileRoleIn(profileRoleInRequest3);
//      bonitaService.profileRoleIn(profileRoleInRequest4);

        Map<String, String> centrosId = new HashMap<>();
        for (CentroRecoleccion centro : centros) {
            RegisterGroupRequest registro = RegisterGroupRequest.builder()
                    .name(centro.getNombre())
                    .description(centro.getNombre())
                    .displayName(centro.getNombre())
                    .parent_group_id("1")
                    .build();
            centrosId.put(centro.getNombre(), bonitaService.createGroup(registro).getBody().get("id").asText());
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
            String idUser = bonitaService.createUser(registerBonitaRequest).getBody().get("id").asText();
            bonitaService.createMembership(
                    MembershipBonitaRequest.builder()
                            .group_id("1")
                            .role_id(roles.get(
                                    usuario.getRol()
                                            .getNombre()
                                            .charAt(5)
                                            +
                                            usuario.getRol()
                                                    .getNombre()
                                                    .substring(6)
                                                    .toLowerCase())
                            )
                            .user_id(idUser)
                            .build()
            );
            if (usuario.getCentroRecoleccion() == null) {
                bonitaService.createMembership(
                        MembershipBonitaRequest.builder()
                                .group_id("2")
                                .role_id(roles.get(
                                        usuario.getRol()
                                                .getNombre()
                                                .charAt(5)
                                                +
                                                usuario.getRol()
                                                        .getNombre()
                                                        .substring(6)
                                                        .toLowerCase())
                                )
                                .user_id(idUser)
                                .build()
                );
            } else {
                bonitaService.createMembership(
                        MembershipBonitaRequest.builder()
                                .group_id(centrosId.get(usuario.getCentroRecoleccion().getNombre()))
                                .role_id(roles.get(
                                        usuario.getRol()
                                                .getNombre()
                                                .charAt(5)
                                                +
                                                usuario.getRol()
                                                        .getNombre()
                                                        .substring(6)
                                                        .toLowerCase())
                                )
                                .user_id(idUser)
                                .build()

                );
            }
        }
        return bonitaService.logoutService();
    }
}
