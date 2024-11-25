package dssd.server.controller;

import dssd.server.exception.UsuarioInvalidoException;
import dssd.server.model.Usuario;
import dssd.server.requests.*;
import dssd.server.response.AuthResponse;
import dssd.server.response.MessageResponse;
import dssd.server.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RequestMapping("/auth")
@RestController
public class AuthController {

    @Autowired
    private Environment environment;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

        try {
            return new ResponseEntity<AuthResponse>(userService.login(request), HttpStatus.OK);
        } catch (UsuarioInvalidoException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest request) throws MessagingException, UsuarioInvalidoException, UnsupportedEncodingException, MessagingException {
        Usuario usuario=userService.register(request, environment.getProperty("url"));
        userService.crearUsuarioBonita(usuario,request);
        return new ResponseEntity<>(MessageResponse.builder().message("Usuario creado con exito").build(), HttpStatus.OK);
    }

    @RequestMapping(value = "/resend", method = RequestMethod.POST)
    public ResponseEntity<?> resendVerification(@Valid @RequestBody EmailRequest emailRequest) {
        if (userService.resendVerification(emailRequest.getEmail(), environment.getProperty("url"))) {
            return new ResponseEntity<>(MessageResponse.builder().message("Código de verificación enviado").build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(MessageResponse.builder().message("No se pudo enviar el mensaje de verificación").build(), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/verify", method = RequestMethod.PATCH)
    public ResponseEntity<?> verifyUser(@RequestBody VerificacionRequest verificacionRequest) {

        if (userService.verify(verificacionRequest)) {
            return new ResponseEntity<>(MessageResponse.builder().message("Usuario verificado").build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(MessageResponse.builder().message("No se pudo verificar el usuario").build(), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/recover", method = RequestMethod.POST)
    public ResponseEntity<?> recoverUser(@Valid @RequestBody EmailRequest emailRequest) {
        if (userService.recover(emailRequest, environment.getProperty("url"))) {
            return new ResponseEntity<>(MessageResponse.builder().message("Código de recuperación enviado").build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(MessageResponse.builder().message("No se pudo recuperar el usuario").build(), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/reset", method = RequestMethod.PATCH)
    public ResponseEntity<MessageResponse> resetUser(@Valid @RequestBody CambiarPassRequest request) {
        if (userService.reset(request)) {
            return new ResponseEntity<>(MessageResponse.builder().message("Contraseña cambiada").build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(MessageResponse.builder().message("No se pudo cambiar la contraseña").build(), HttpStatus.UNAUTHORIZED);
        }
    }


}
