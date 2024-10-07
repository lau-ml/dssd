package dssd.server.service;

import dssd.server.exception.UsuarioInvalidoException;
import dssd.server.model.Usuario;
import dssd.server.repository.UsuarioRepository;
import dssd.server.requests.RegisterRequest;
import dssd.server.response.MessageResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Service
public class UserService {

    @Autowired
    private final UsuarioRepository dao;

    @Autowired
    private final EmailService emailService;

    public MessageResponse register(RegisterRequest request, String siteUrl) throws UsuarioInvalidoException, MessagingException, UnsupportedEncodingException {

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

        String randomCode = RandomString.make(64);
        Usuario entity = Usuario
                .builder()
                .nombre(request.getFirstName())
                .apellido(request.getLastName())
                .usuario(request.getUsername())
                .email(request.getEmail())
                .contrasena(passwordEncoder.encode(request.getPassword()))
                .activo(false)
                .saldo(0.0)
                .verificationCode(randomCode)
                .build();

        emailService.sendVerificationEmail(entity, siteUrl + "/verify", randomCode, "verificar");
        dao.save(entity);
        return MessageResponse.builder().message("Usuario creado con exito").build();
    }


    public Usuario recuperar(Serializable id) throws UsuarioInvalidoException {
        return dao.findById((Long) id).orElse(null);

    }

    public Usuario recuperar(String username) throws UsuarioInvalidoException {
        return dao.findByUsuario(username).orElse(null);
    }


    public List<Usuario> recuperarTodos(String columnOrder) throws Exception {
        return dao.findAll().stream().sorted((a, b) -> switch (columnOrder) {
            case "apellido" -> a.getApellido().compareTo(b.getApellido());
            case "usuario" -> a.getUsuario().compareTo(b.getUsuario());
            case "email" -> a.getEmail().compareTo(b.getEmail());
            default -> a.getNombre().compareTo(b.getNombre());
        }).collect(Collectors.toList());
    }


    public AuthResponse login(LoginRequest request) throws UsuarioInvalidoException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            UserDetails user = dao.findByUsuario(request.getUsername()).orElseThrow();
            String token = jwtService.getToken(user);
            return AuthResponse.builder()
                    .token(token)
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
        return dao.findByUsuario(username);
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
            user.setContrasena(passwordEncoder.encode(request.getPassword()));
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

}

}
