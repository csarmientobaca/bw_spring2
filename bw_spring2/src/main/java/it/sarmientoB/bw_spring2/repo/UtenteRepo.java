package it.sarmientoB.bw_spring2.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import it.sarmientoB.bw_spring2.model.Utente;

public interface UtenteRepo extends CrudRepository<Utente, Long>, PagingAndSortingRepository<Utente, Long> {
	
	public Utente findByFirstname(String firstname);
	public Utente findByLastname(String lastname);
	public Utente findByEmail(String email);
	public Utente findByUsername(String username);
	
	public boolean existsByEmail(String email);
	
	
}