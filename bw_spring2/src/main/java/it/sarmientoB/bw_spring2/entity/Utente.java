package it.sarmientoB.bw_spring2.entity;

import lombok.Data;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Data
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = "username"),@UniqueConstraint(columnNames = "email") })
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "utenti_ruoli",
            joinColumns = @JoinColumn(name = "utenti_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "ruolo_id", referencedColumnName = "id")
    )
    private Set<Ruolo> ruolo = new HashSet<>();
}