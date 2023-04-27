package it.sarmientoB.bw_spring2.service;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.javafaker.Faker;

import it.sarmientoB.bw_spring2.enums.StatoDispositivi;
import it.sarmientoB.bw_spring2.enums.TipoDispositivo;
import it.sarmientoB.bw_spring2.model.Dispositivi;
import it.sarmientoB.bw_spring2.model.Utente;
import it.sarmientoB.bw_spring2.repo.DispositivoRepo;
import it.sarmientoB.bw_spring2.repo.UtenteRepo;
import jakarta.persistence.EntityNotFoundException;

@Service
public class DispositiviServizi {

	@Autowired 
	private DispositivoRepo dispoRepo;
	
	@Autowired 
	private UtenteRepo utenteRepo;
		
	@Autowired @Qualifier("fakeDispo") 
	private ObjectProvider<Dispositivi> fakeDispositivoProvider;
	
	// internal methods for beans
	
	public TipoDispositivo fakeType(int random) {
		TipoDispositivo toReturn = null;
		switch(random) {
		case 1 -> toReturn = TipoDispositivo.PHONE;
		case 2 -> toReturn = TipoDispositivo.SERVER;
		case 3 -> toReturn = TipoDispositivo.DESKTOP;
		
		}
		return toReturn;
	}
	
	public StatoDispositivi fakeStatus(int random) {
		StatoDispositivi toReturn = null;
		switch(random) {
		case 1 -> toReturn = StatoDispositivi.DISPONIBILE;
		case 2 -> toReturn = StatoDispositivi.ASSEGNATO;
		case 3 -> toReturn = StatoDispositivi.MANUTENZIONE;
		case 4 -> toReturn = StatoDispositivi.MANUTENZIONE;
		}
		return toReturn;
	}
	
	public Dispositivi setOwnerIfAssigned(Dispositivi d) {
		Faker fake = Faker.instance(new Locale("en-US"));
		List<Utente> eList = (List<Utente>) utenteRepo.findAll();
		Utente newpropietaUtente = eList.get(fake.number().numberBetween(0, (eList.size()-1)));
		d.setPropietaUtente(newpropietaUtente);
		return d;
	}
	
	// JPA methods
	
	public String createFakeDevice() {
		Dispositivi dispo = fakeDispositivoProvider.getObject();
		return salvaDispositivo(dispo);
	}
	
	public String salvaDispositivo(Dispositivi dispo) {
		dispoRepo.save(dispo);
		return "Dispositivo salvato";
	}
	
	public String aggiornaDispositivo(Dispositivi dispo) {
		if (dispoRepo.existsById(dispo.getId())) {
			dispoRepo.save(dispo);
			return "Dispositivo aggironato";
		} else {
			throw new EntityNotFoundException("dispositivo non esiste");
		}
	}
	
	public String cancellaDispositivoID(Long id) {
		if (dispoRepo.existsById(id)) {
			dispoRepo.deleteById(id);
			return "Dispositivo cancellato usando ID";
		} else {
			throw new EntityNotFoundException("dispositivo non esiste");
		} 
	}
	
	public String cancellaDispositivo(Dispositivi dispo) {
		if (dispoRepo.existsById(dispo.getId())) {
			dispoRepo.delete(dispo);
			return "Dispositivo cancellato usando ID";
		} else {
			throw new EntityNotFoundException("dispositivo non esiste");
		} 
	}
	
	public Dispositivi cercaDispositivoID(Long id) {
		if (dispoRepo.existsById(id)) {
			return dispoRepo.findById(id).get();
		} else {
			throw new EntityNotFoundException("dispositivo non esiste");
		}
	}
	
	public Page<Dispositivi> findDeviceByStatusAvailable(Pageable pageable) {
		return dispoRepo.findByStatus(StatoDispositivi.DISPONIBILE, pageable);
	}
	
	public Page<Dispositivi> findDeviceByType(TipoDispositivo type, Pageable pageable) {
		return dispoRepo.findByType(type, pageable);
	}
	
	public Page<Dispositivi> findDeviceByStatus(StatoDispositivi status, Pageable pageable) {
		return dispoRepo.findByStatus(status, pageable);
	}
	
	public List<Dispositivi> findAllDevice() {
		return (List<Dispositivi>) dispoRepo.findAll();
	}
	
	public Page<Dispositivi> findAllDevice(Pageable pageable) {
		return (Page<Dispositivi>) dispoRepo.findAll(pageable);
	}
	
	
	public String linkDeviceToEmployee(Long employeeId, Long deviceId) {
		if (utenteRepo.existsById(employeeId) && dispoRepo.existsById(deviceId)) {
			Utente uten = utenteRepo.findById(employeeId).get(); 
			Dispositivi dispo =dispoRepo.findById(deviceId).get(); 
			if (dispo.getStato().equals(StatoDispositivi.ASSEGNATO)) { 
				throw new DataIntegrityViolationException("Dispositivo assegnato per altro utente");
			} else if (dispo.getStato().equals(StatoDispositivi.ABBANDONATO)) {
				throw new DataIntegrityViolationException("Dispositivo abbandonato");
			}
			uten.getDispositivi().add(dispo); 
			utenteRepo.save(uten);
			dispo.setStato(StatoDispositivi.ASSEGNATO); 
			dispo.setPropietaUtente(uten); 
			aggiornaDispositivo(dispo); 
			return "Device with ID: " + deviceId + " has been correctly assigned to employee with ID: " + employeeId; 
		} else if (!dispoRepo.existsById(deviceId) && !utenteRepo.existsById(employeeId)) {  
			throw new EntityNotFoundException("Both Employee & Device doesn't exists on Database!"); 
		} else if (!utenteRepo.existsById(employeeId)) {
		  	throw new EntityNotFoundException("Utente con ID	: " + employeeId +" dispositivo non esiste"); 
		} else {   
			throw new EntityNotFoundException("Dispositivi con ID	: " + deviceId + "dispositivo non esiste"); 
		} 
	}
	 
	public String unlinkDeviceFromEmployee(Long deviceId) {
		if (dispoRepo.existsById(deviceId)) {
			Dispositivi dispo = cercaDispositivoID(deviceId);
			try {
				dispo.getPropietaUtente().equals(null); // this is used only to invoke NullPointerException in case of null, and send custom exception
				dispo.setPropietaUtente(null);
				aggiornaDispositivo(dispo);
				return "dispositivo cancellato di utente";
			} catch (NullPointerException e) {
				if (dispo.getStato().equals(StatoDispositivi.DISPONIBILE)) {
					throw new NullPointerException("Dispositivo Disponibile, senza propietaUtente");
				} else if (dispo.getStato().equals(StatoDispositivi.MANUTENZIONE)) {
					throw new NullPointerException("Dispositivo in manutenzione, senza propietaUtente");
				} else {
					throw new NullPointerException("Dispositivo in abbandonato, nessuno puo usarlo");
				}
			}
		} else {
			throw new EntityNotFoundException("dispositivo non esiste");
		}
	}
}