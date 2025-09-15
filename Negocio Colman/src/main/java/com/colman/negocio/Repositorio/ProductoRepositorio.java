package com.colman.negocio.Repositorio;

import com.colman.negocio.Entidades.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepositorio extends JpaRepository<Producto,Long> {
    
    
    @Query ("SELECT p FROM Producto p WHERE p.nombre LIKE %:nombre% ")
    public List<Producto> BuscarPorNombre(@Param ("nombre") String nombre);
    
    @Query ("SELECT p FROM Producto p WHERE p.tipoProducto = :tipo")
    public List<Producto> BuscarporTipo(@Param("tipo") String tipo);
    
    @Query ("SELECT p FROM Producto p ORDER BY nombre ASC")
    public List<Producto> Ordernar();
}
