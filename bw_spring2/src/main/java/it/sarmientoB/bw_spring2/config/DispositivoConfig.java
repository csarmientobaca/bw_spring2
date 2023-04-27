package it.sarmientoB.bw_spring2.config;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.github.javafaker.Faker;
import it.sarmientoB.bw_spring2.enums.StatoDispositivi;
import it.sarmientoB.bw_spring2.enums.TipoDispositivo;
import it.sarmientoB.bw_spring2.model.Dispositivi;
import it.sarmientoB.bw_spring2.service.DispositiviServizi;

@Configuration
public class DispositivoConfig {
	
	@Autowired private DispositiviServizi dispoService;

	
	@Bean
	@Scope("prototype")
	public Dispositivi dispositivoFake() {
		Faker f = Faker.instance(new Locale("en-US"));
		int rand_one = f.number().numberBetween(0, 7);
		int rand_two = f.number().numberBetween(0, 11);
		Dispositivi dispo = Dispositivi.builder().tipo(dispoService.fakeType(rand_one)).stato(dispoService.fakeStatus(rand_two)).propietaUtente(null).build();
		if (dispo.getStato().equals(StatoDispositivi.ASSEGNATO)) {
			dispo = dispoService.setOwnerIfAssigned(dispo);
		}
		return dispo;
	}
	@Bean
	@Scope("singleton")
	public Dispositivi disposistivoCustom() {
		return new Dispositivi();
	}
	
	@Bean
	@Scope("prototype")
	public Dispositivi dispositivoParams(TipoDispositivo tipo, StatoDispositivi stato) {
		return Dispositivi.builder().tipo(tipo).stato(stato).propietaUtente(null).build();
	}
}