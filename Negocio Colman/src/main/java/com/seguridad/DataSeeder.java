package com.seguridad;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.Entidades.Usuario;
import com.Enum.Rol;
import com.Repositorio.UsuarioRepositorio;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner init(UsuarioRepositorio repo, PasswordEncoder passwordEncoder) {
        return args -> {
            if (repo.findAll().isEmpty()) {
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("1234"));
                admin.setRol(Rol.ADMIN);
                repo.save(admin);

                Usuario user = new Usuario();
                user.setUsername("usuario");
                user.setPassword(passwordEncoder.encode("1234"));
                user.setRol(Rol.USER);
                repo.save(user);
            }
        };
    }
}
