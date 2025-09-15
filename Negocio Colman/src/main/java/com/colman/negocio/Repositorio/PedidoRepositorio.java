package com.colman.negocio.Repositorio;

import com.colman.negocio.Entidades.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepositorio extends JpaRepository<Pedido,Long> {
    
}
