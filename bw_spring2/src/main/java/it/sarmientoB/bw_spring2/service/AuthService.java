package it.sarmientoB.bw_spring2.service;

import it.sarmientoB.bw_spring2.payload.Login;
import it.sarmientoB.bw_spring2.payload.Registrare;

public interface AuthService {
    
	String login(Login loginDispositivo);
    String register(Registrare registerDispositivo);
    
}