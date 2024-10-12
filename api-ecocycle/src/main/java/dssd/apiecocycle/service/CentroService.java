package dssd.apiecocycle.service;

import dssd.apiecocycle.exceptions.CentroInvalidoException;
import dssd.apiecocycle.model.Centro;
import dssd.apiecocycle.repository.CentroRepository;

import dssd.apiecocycle.requests.LoginRequest;
import dssd.apiecocycle.requests.RegisterRequest;
import dssd.apiecocycle.response.AuthResponse;
import dssd.apiecocycle.response.MessageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CentroService {

    private final CentroRepository dao;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public MessageResponse register(RegisterRequest request) throws CentroInvalidoException, UnsupportedEncodingException {

        Optional<Centro> centro = dao.findByEmail(request.getEmail());
        if (centro != null && centro.isPresent()) {
            throw new CentroInvalidoException("El mail y/o el usuario ingresado ya existe");
        }
        if (centro != null) {
            throw new CentroInvalidoException("El mail ingresado ya existe");
        }
        if (centro.isPresent()) {
            throw new CentroInvalidoException("El usuario ingresado ya existe");
        }

        String randomCode = RandomString.make(64);
        Centro entity = Centro
                .builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .direccion(request.getDireccion())
                .telefono(request.getTelefono())
                .build();

        dao.save(entity);
        return MessageResponse.builder().message("Usuario creado con exito").build();
    }




    public AuthResponse login(@Valid LoginRequest request) throws CentroInvalidoException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            UserDetails center = dao.findByEmail(request.getEmail()).orElseThrow();
            String token = jwtService.getToken(center);
            return AuthResponse.builder()
                    .token(token)
                    .build();
        } catch (AuthenticationException e) {
            throw new CentroInvalidoException("Email o contraseña incorrectos");
        }
    }

    public Optional<Centro> findByEmail(String email) {
        return dao.findByEmail(email);
    }

    public Centro recuperarCentro() throws CentroInvalidoException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return findByEmail(username).orElseThrow(() -> new CentroInvalidoException("Centro inválido"));

    }

}


