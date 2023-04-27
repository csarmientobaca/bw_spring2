package it.sarmientoB.bw_spring2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.sarmientoB.bw_spring2.payload.JWTAuthResponse;
import it.sarmientoB.bw_spring2.payload.Login;
import it.sarmientoB.bw_spring2.payload.Registrare;
import it.sarmientoB.bw_spring2.service.AuthService;



@RestController
@RequestMapping("/api/auth")
public class Authorisation {

    private AuthService authService;

    public Authorisation(AuthService authService) {
        this.authService = authService;
    }

    // Build Login REST API
    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<JWTAuthResponse> login(@RequestBody Login login){
           	
    	String token = authService.login(login);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setUsername(login.getUsername());
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }

    // Build Register REST API
    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<String> register(@RequestBody Registrare registrare){
        String response = authService.register(registrare);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}