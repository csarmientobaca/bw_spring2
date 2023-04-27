package it.sarmientoB.bw_spring2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import it.sarmientoB.bw_spring2.enums.TipoDispositivo;
import it.sarmientoB.bw_spring2.enums.StatoDispositivi;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dispositivi")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Dispositivi {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoDispositivo tipo;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private StatoDispositivi stato;
	
	@ManyToOne
	@JoinColumn(name = "utenteID")
	@JsonIgnoreProperties({"nome", "cognome", "email", "dispositivi"})
	private Utente propietaUtente;
	
}