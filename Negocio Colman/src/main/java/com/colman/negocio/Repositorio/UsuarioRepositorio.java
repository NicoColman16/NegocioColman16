package com.colman.negocio.Repositorio;

import com.colman.negocio.Entidades.Usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Usuario buscarUsuario(@Param("email") String email);

    Optional <Usuario> findByUsername(String username);

}
