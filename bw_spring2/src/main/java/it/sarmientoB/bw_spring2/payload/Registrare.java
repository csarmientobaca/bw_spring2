package it.sarmientoB.bw_spring2.payload;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Registrare {
    private String name;
    private String username;
    private String email;
    private String password;
    private Set<String> roles;
}