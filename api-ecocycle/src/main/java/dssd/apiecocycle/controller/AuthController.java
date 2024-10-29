package dssd.apiecocycle.controller;

import dssd.apiecocycle.exceptions.CentroInvalidoException;
import dssd.apiecocycle.requests.LoginRequest;
import dssd.apiecocycle.requests.RegisterRequest;
import dssd.apiecocycle.response.AuthResponse;
import dssd.apiecocycle.response.ErrorResponse;
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
                            examples ={
                                    @ExampleObject(name = "Centro vacío", value = "{\"errors\": [\"El mail del centro no puede estar vacío\"]}"),
                                    @ExampleObject(name = "Contraseña vacía", value = "{\"errors\": [\"La contraseña no puede estar vacía.\"]}"),
                                    @ExampleObject(name = "Ambos vacíos", value = "{\"errors\": [\"El mail del centro no puede estar vacío\", \"La contraseña no puede estar vacía.\"]}")
                            })
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales inválidas",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Email o contraseña incorrectos.\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Error interno del servidor.\"}")
                    )
            )})
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos para iniciar sesión",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LoginRequest.class),
                    examples = {
                            @ExampleObject(name = "Caso Exitoso Centro1", value = "{\"email\": \"mailcentro1@ecocycle.com\", \"password\": \"123456\"}"),
                            @ExampleObject(name = "Caso Exitoso Depósito1", value = "{\"email\": \"global1@ecocycle.com\", \"password\": \"123456\"}"),
                            @ExampleObject(name = "Caso Exitoso Centro2", value = "{\"email\": \"mailcentro2@ecocycle.com\", \"password\": \"123456\"}"),
                            @ExampleObject(name = "Caso Exitoso Depósito2", value = "{\"email\": \"global2@ecocycle.com\", \"password\": \"123456\"}"),
                            @ExampleObject(name = "Caso Exitoso Centro3", value = "{\"email\": \"mailcentro3@ecocycle.com\", \"password\": \"123456\"}"),
                            @ExampleObject(name = "Caso Exitoso Depósito3", value = "{\"email\": \"global3@ecocycle.com\", \"password\": \"123456\"}"),
                            @ExampleObject(name = "Caso fallido", value = "{\"email\": \"usuario1@example.com\", \"password\": \"incorrecta\"}")
                    }
            )
    )
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        try {
            return new ResponseEntity<AuthResponse>(centroService.login(request), HttpStatus.OK);
        } catch (CentroInvalidoException e) {
            return new ResponseEntity<>(ErrorResponse.builder().error(e.getMessage()).build(), HttpStatus.UNAUTHORIZED);
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
                            examples ={
                                    @ExampleObject(name = "Centro vacío",
                                            value = "{\"errors\": [\"El mail del centro no puede estar vacío\", \"La contraseña no puede estar vacía\", \"La confirmación de contraseña no puede estar vacía\", \"La dirección no puede estar vacía\", \"El teléfono no puede estar vacío\", \"El nombre no puede estar vacío\", \"El tipo no puede estar vacío\"]}"),
                                    @ExampleObject(name = "Contraseña vacía",
                                            value = "{\"errors\": [\"La contraseña no puede estar vacía\", \"La confirmación de contraseña no puede estar vacía\"]}"),
                                    @ExampleObject(name = "Contraseña no válida",
                                            value = "{\"errors\": [\"Debe tener 8 caracteres, y al menos una letra minúscula, una letra mayúscula, un número y un caracter especial\"]}"),
                                    @ExampleObject(name = "Email inválido",
                                            value = "{\"errors\": [\"El email debe ser válido\"]}"),
                                    @ExampleObject(name = "Email vacío",
                                            value = "{\"errors\": [\"El mail del centro no puede estar vacío\"]}"),
                                    @ExampleObject(name = "Dirección vacía",
                                            value = "{\"errors\": [\"La dirección no puede estar vacía\"]}"),
                                    @ExampleObject(name = "Teléfono vacío",
                                            value = "{\"errors\": [\"El teléfono no puede estar vacío\"]}"),
                                    @ExampleObject(name = "Nombre vacío",
                                            value = "{\"errors\": [\"El nombre no puede estar vacío\"]}"),
                                    @ExampleObject(name = "Tipo inválido",
                                            value = "{\"errors\": [\"Tipo de centro inválido\"]}"),
                                    @ExampleObject(name = "Número de teléfono inválido",
                                            value = "{\"errors\": [\"El teléfono debe ser un número válido de 10 dígitos, con un código de país opcional\"]}")
                            })
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflicto: El centro ya existe",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class),
                            examples = @ExampleObject(value = "{\"error\": \"El centro ingresado ya existe.\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Error interno del servidor.\"}")
                    )
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos necesarios para registrar un nuevo centro de recolección",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RegisterRequest.class),
                    examples = {
                            @ExampleObject(name = "Caso de Registro Exitoso centro", value = "{\n" +
                                    "  \"password\": \"Password123!\",\n" +
                                    "  \"confirmPassword\": \"Password123!\",\n" +
                                    "  \"email\": \"usuario@ecocycle.com\",\n" +
                                    "  \"direccion\": \"Calle Ejemplo 123\",\n" +
                                    "  \"telefono\": \"+12 3456789099\",\n" +
                                    "  \"nombre\": \"Centro Ecocycle\",\n" +
                                    "  \"tipo\": \"CENTRO_RECEPCION\"\n" +
                                    "}"),
                            @ExampleObject(name = "Caso de Registro Exitoso depósito", value = "{\n" +
                                    "  \"password\": \"Password123!\",\n" +
                                    "  \"confirmPassword\": \"Password123!\",\n" +
                                    "  \"email\": \"deposito@ecocycle.com\",\n" +
                                    "  \"direccion\": \"Calle Ejemplo 123\",\n" +
                                    "  \"telefono\": \"+12 3456789099\",\n" +
                                    "  \"nombre\": \"Centro Ecocycle\",\n" +
                                    "  \"tipo\": \"DEPOSITO_GLOBAL\"\n" +
                                    "}"),
                            @ExampleObject(name = "Caso de Registro Fallido - Email Inválido", value = "{\n" +
                                    "  \"password\": \"Password123!\",\n" +
                                    "  \"confirmPassword\": \"Password123!\",\n" +
                                    "  \"email\": \"correo_invalido\",\n" +
                                    "  \"direccion\": \"Calle Ejemplo 456\",\n" +
                                    "  \"telefono\": \"+12 3456789099\",\n" +
                                    "  \"nombre\": \"Centro Ecocycle Fallido\",\n" +
                                    "  \"tipo\": \"CENTRO_RECEPCION\"\n" +
                                    "}"),
                            @ExampleObject(name = "Caso de Registro Fallido - Existente", value = "{\n" +
                                    "  \"password\": \"Password123!\",\n" +
                                    "  \"confirmPassword\": \"Password123!\",\n" +
                                    "  \"email\": \"mailcentro1@ecocycle.com\",\n" +
                                    "  \"direccion\": \"Calle Ejemplo 456\",\n" +
                                    "  \"telefono\": \"+12 3456789099\",\n" +
                                    "  \"nombre\": \"Centro Ecocycle Fallido\",\n" +
                                    "  \"tipo\": \"CENTRO_RECEPCION\"\n" +
                                    "}")
                    }
            )
    )
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) throws UnsupportedEncodingException {
        try {
            return new ResponseEntity<>(centroService.register(request), HttpStatus.OK);
        } catch (CentroInvalidoException e) {
            return new ResponseEntity<>(ErrorResponse.builder().error(e.getMessage()).build(), HttpStatus.CONFLICT);
        }

    }

}
