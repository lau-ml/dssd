package dssd.apiecocycle.controller;

import dssd.apiecocycle.exceptions.CentroInvalidoException;
import dssd.apiecocycle.requests.LoginRequest;
import dssd.apiecocycle.requests.RegisterRequest;
import dssd.apiecocycle.response.AuthResponse;
import dssd.apiecocycle.response.MessageResponse;
import dssd.apiecocycle.service.CentroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    private CentroService centroService;

    @Operation(summary = "Iniciar sesión", description = "Este endpoint permite a los centros de recolección y depositos iniciar sesión con sus credenciales. Devuelve un token JWT para acceder a los endpoints protegidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class), examples = @ExampleObject(value = "{\"token\": \"jwt_token_aqui\"}"))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos: El email o la contraseña están vacíos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"errors\": [\"El mail del centro no puede estar vacío\", \"La contraseña no puede estar vacía.\"]}")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales inválidas",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Email o contraseña incorrectos.\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Error interno del servidor.\"}")
                    )
            )})
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        try {
            return new ResponseEntity<AuthResponse>(centroService.login(request), HttpStatus.OK);
        } catch (CentroInvalidoException e) {
            return new ResponseEntity<>(MessageResponse.builder().message(e.getMessage()).build(), HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Registro de nuevo centro de recolección", description = "Este endpoint permite registrar un nuevo centro de recolección proporcionando los datos requeridos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro exitoso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class), examples = @ExampleObject(value = "{\"message\": \"Registro exitoso.\"}"))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class),
                            examples = @ExampleObject(value = "{\"errors\": [\"El email ya está registrado.\", \"La contraseña no puede estar vacía.\"]}")
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflicto: El centro ya existe",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class),
                            examples = @ExampleObject(value = "{\"message\": \"El centro ingresado ya existe.\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Error interno del servidor.\"}")
                    )
            )
    })
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) throws UnsupportedEncodingException {
        try {
            return new ResponseEntity<>(centroService.register(request), HttpStatus.OK);
        } catch (CentroInvalidoException e) {
            return new ResponseEntity<>(MessageResponse.builder().message(e.getMessage()).build(), HttpStatus.CONFLICT);
        }

    }

}
