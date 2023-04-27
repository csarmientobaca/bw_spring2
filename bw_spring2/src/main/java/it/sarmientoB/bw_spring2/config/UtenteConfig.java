package it.sarmientoB.bw_spring2.config;

import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.github.javafaker.Faker;

import it.sarmientoB.bw_spring2.model.Utente;

@Configuration
public class UtenteConfig {

	@Bean
	@Scope("singleton")
	public Utente customUtente() {
		return new Utente();
	}
	
	@Bean
	@Scope("prototype")
	public Utente paramsUtente(String nome, String cognome, String username, String email) {
		return Utente.builder().nome(nome).cognome(cognome).username(username).email(email).build();
	}
	
	@Bean
	@Scope("prototype")
	public Utente fakeUtente() {
		Faker fake = Faker.instance(new Locale("en-US"));
		String nome = fake.name().firstName();
		String cognome =fake.name().lastName();
		String username = nome +"_"+cognome;
		String email = nome.charAt(0) + "." + cognome + "@example.com";
		return Utente.builder().nome(nome).cognome(cognome).username(username).email(email).build();
	}
	
}