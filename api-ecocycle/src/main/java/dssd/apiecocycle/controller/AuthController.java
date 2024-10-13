package dssd.apiecocycle.controller;

import dssd.apiecocycle.exceptions.CentroInvalidoException;

import dssd.apiecocycle.requests.LoginRequest;
import dssd.apiecocycle.requests.RegisterRequest;
import dssd.apiecocycle.response.AuthResponse;
import dssd.apiecocycle.response.MessageResponse;
import dssd.apiecocycle.service.CentroService;
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
    private CentroService centroService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

        try {
            return new ResponseEntity<AuthResponse>(centroService.login(request), HttpStatus.OK);
        } catch (CentroInvalidoException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest request) throws CentroInvalidoException, UnsupportedEncodingException{
        try{
            return new ResponseEntity<>(centroService.register(request), HttpStatus.OK);
        } catch (CentroInvalidoException e) {
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }

    }




}
