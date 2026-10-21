package com.Repositorio;

import com.Entidades.MovimientoCaja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoCajaRepositorio extends JpaRepository<MovimientoCaja, Long> {
} 

