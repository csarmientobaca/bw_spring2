package it.sarmientoB.bw_spring2.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import it.sarmientoB.bw_spring2.enums.TipoDispositivo;
import it.sarmientoB.bw_spring2.enums.StatoDispositivi;
import it.sarmientoB.bw_spring2.model.Dispositivi;

public interface DispositivoRepo extends CrudRepository<Dispositivi, Long>, PagingAndSortingRepository<Dispositivi, Long> {

	public Page<Dispositivi> findByStatus(StatoDispositivi stato, Pageable pageable);
	public Page<Dispositivi> findByType(TipoDispositivo tipo, Pageable pageable);
	
}