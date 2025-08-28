package com.colman.negocio.Entidades;

import com.colman.negocio.Enum.Rol;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@NoArgsConstructor
@Entity
public class Usuario {
    
    @Id
    @GeneratedValue(generator= "uuid")
    @GenericGenerator(name="uuid", strategy="uuid2")
    private String id;
    
    private String username;
    private String email;
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Rol rol; 
}
