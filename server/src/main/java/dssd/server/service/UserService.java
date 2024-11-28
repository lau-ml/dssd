package dssd.server.service;

import dssd.server.exception.UsuarioInvalidoException;
import dssd.server.model.CentroRecoleccion;
import dssd.server.model.Rol;
import dssd.server.model.Usuario;
import dssd.server.repository.LoginBonitaRepository;
import dssd.server.repository.RolRepository;
import dssd.server.repository.UsuarioRepository;
import dssd.server.requests.*;
import dssd.server.response.AuthResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UsuarioRepository dao;

    private final JwtService jwtService;

    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final CentroRecoleccionService centroRecoleccionService;

    private final BonitaService bonitaService;

    private final RolRepository rolRepository;

    private final LoginBonitaRepository loginBonitaRepository;

    @Value("${BONITA_USERNAME}")
    private String BONITA_ADMIN;

    @Value("${BONITA_PASSWORD}")
    private String BONITA_PASSWORD;


    @Transactional
    public Usuario register(RegisterRequest request, String siteUrl)
            throws UsuarioInvalidoException, MessagingException, UnsupportedEncodingException {
        Usuario usuarioByEmail = dao.findByEmail(request.getEmail());
        Optional<Usuario> usuarioByUsuario = dao.findByUsername(request.getUsername());
        if (usuarioByEmail != null && usuarioByUsuario.isPresent()) {
            throw new UsuarioInvalidoException("El mail y/o el usuario ingresado ya existe");
        }
        if (usuarioByEmail != null) {
            throw new UsuarioInvalidoException("El mail ingresado ya existe");
        }
        if (usuarioByUsuario.isPresent()) {
            throw new UsuarioInvalidoException("El usuario ingresado ya existe");
        }
        CentroRecoleccion centro = (request.getCentro() != null)
                ? centroRecoleccionService.findById(request.getCentro())
                : null;

        Rol rol = rolRepository.findByNombre(request.getRol()).get();

        boolean habilitadoAdmin = (!rol.getNombre().equals("ROLE_EMPLEADO") && centro != null);

        String randomCode = RandomString.make(64);
        Usuario entity = Usuario
                .builder()
                .nombre(request.getFirstName())
                .apellido(request.getLastName())
                .username(request.getUsername())
                .email(request.getEmail())
                .dni(request.getDni())
                .password(passwordEncoder.encode(request.getPassword()))
                .activo(false)
                .habilitadoAdmin(habilitadoAdmin)
                .rol(rol)
                .centroRecoleccion(centro)
                .verificationCode(randomCode)
                .build();

        emailService.sendVerificationEmail(entity, siteUrl + "/verify", randomCode, "verificar");
        bonitaService.login(LoginRequest.builder().username(BONITA_ADMIN).password(BONITA_PASSWORD).build(), findByUsername(BONITA_ADMIN).orElseThrow());
        return dao.save(entity);
    }
    @Transactional
    public void crearUsuarioBonita(Usuario usuario,RegisterRequest request) throws UsuarioInvalidoException {
        String id_user=bonitaService.createUser(
                RegisterBonitaRequest.builder()
                        .userName(usuario.getUsername())
                        .enabled(usuario.getHabilitadoAdmin()? "true":"false")
                        .password(request.getPassword())
                        .firstname(usuario.getNombre())
                        .lastname(usuario.getApellido())
                        .password_confirm(request.getPassword())
                        .build()
        ).getBody().get("id").asText();
        String id_rol=bonitaService.getRoleByName(usuario.getRol().getNombre().contains("EMPLEADO")?"Empleado":"Recolector").getBody().get(0).get("id").asText();
        String id_group=bonitaService.getGroupByName("Centro de recolección").getBody().get(0).get("id").asText();
        this.bonitaService.createMembership(
                MembershipBonitaRequest.builder()
                        .user_id(id_user)
                        .group_id(id_group)
                        .role_id(id_rol)
                        .build()
        );
    }

    public Usuario recuperar(Serializable id) throws UsuarioInvalidoException {
        return dao.findById((Long) id).orElse(null);

    }

    public Usuario recuperar(String username) throws UsuarioInvalidoException {
        return dao.findByUsername(username).orElse(null);
    }

    public List<Usuario> recuperarTodos(String columnOrder) throws Exception {
        return dao.findAll().stream().sorted((a, b) -> switch (columnOrder) {
            case "apellido" -> a.getApellido().compareTo(b.getApellido());
            case "usuario" -> a.getUsername().compareTo(b.getUsername());
            case "email" -> a.getEmail().compareTo(b.getEmail());
            default -> a.getNombre().compareTo(b.getNombre());
        }).collect(Collectors.toList());
    }

    @Transactional
    public AuthResponse login(LoginRequest request) throws UsuarioInvalidoException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            Usuario user = dao.findByUsername(request.getUsername()).orElseThrow();
            String token = jwtService.getToken(user);
            if (!request.getUsername().equals("bonita")) {
                this.bonitaService.login(request, user);
            }
            return AuthResponse.builder()
                    .token(token)
                    .rol(user.getRol().getNombre())
                    .build();
        } catch (AuthenticationException e) {
            throw new UsuarioInvalidoException("Usuario o contraseña incorrectos");
        }
    }

    public boolean verify(VerificacionRequest verificacionRequest) {
        Usuario user = dao.findByVerificationCode(verificacionRequest.getCode());

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setActivo(true);
            dao.save(user);

            return true;
        }

    }

    public Optional<Usuario> findByUsername(String username) {
        return dao.findByUsername(username);
    }

    public boolean recover(EmailRequest emailRequest, String siteUrl) {
        Usuario user = dao.findByEmail(emailRequest.getEmail());

        if (user == null || !user.isEnabled()) {
            return false;
        } else {
            String randomCode = RandomString.make(64);
            user.setContraCode(randomCode);
            dao.save(user);
            try {
                emailService.sendVerificationEmail(user, siteUrl + "/reset", randomCode, "recuperar");
            } catch (MessagingException | UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
    }

    public boolean reset(CambiarPassRequest request) {
        Usuario user = dao.findByContraCode(request.getCode());

        if (user == null || !user.isEnabled()) {
            return false;
        } else {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setContraCode(null);
            dao.save(user);
            return true;
        }
    }

    public boolean resendVerification(String email, String url) {
        Usuario user = dao.findByEmail(email);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            String randomCode = RandomString.make(64);
            user.setVerificationCode(randomCode);
            dao.save(user);
            try {
                emailService.sendVerificationEmail(user, url + "/verify", randomCode, "verificar");
            } catch (MessagingException | UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
    }

    public void actualizar(Usuario usuario) throws Exception {
        Usuario usuarioPersistido = dao.findById(usuario.getId()).orElse(null);
        if (usuarioPersistido != null) {
            dao.save(usuarioPersistido);
        } else {
            throw new Exception("Error al actualizar usuario");
        }
    }

    public Usuario recuperarUsuario() throws UsuarioInvalidoException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return findByUsername(username).orElseThrow(() -> new UsuarioInvalidoException("Usuario invalido"));

    }

    public Usuario findByEmail(String email) {
        return dao.findByEmail(email);
    }
}
