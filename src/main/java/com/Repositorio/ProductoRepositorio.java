package com.Repositorio;

import com.Entidades.Producto;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepositorio extends JpaRepository<Producto,Long> {
    
    Optional<Producto> findFirstByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT DISTINCT p FROM Producto p LEFT JOIN p.tiposProducto t WHERE " +
       ":query IS NULL OR :query = '' OR " +
       "LOWER(p.nombre) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
       "LOWER(p.codigoBarras) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
       "LOWER(CAST(t AS string)) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Producto> buscarProductosPaginados(@Param("query") String query, Pageable pageable);

    // Buscar por código de barras exacto (útil para el lector)
    Optional<Producto> findByCodigoBarras(String codigoBarras);

}

