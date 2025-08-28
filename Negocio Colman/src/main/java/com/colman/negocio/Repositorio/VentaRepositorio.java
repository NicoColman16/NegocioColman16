package com.colman.negocio.Repositorio;

import com.colman.negocio.Entidades.Venta;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepositorio extends JpaRepository<Venta, String> {
    
     @Query("SELECT SUM(v.monto) FROM Venta v WHERE v.fechaVenta = :fechaVenta")
    Long totalFechaVenta(@Param("fechaVenta") Date fechaVenta);
    
    @Query("SELECT SUM(v.monto) FROM Venta v WHERE DATE(v.fechaVenta) = :fechaVenta AND v.tipo = :tipo")
    Integer totalByFechaVentaAndTipo(@Param("fechaVenta") Date fechaVenta, @Param("tipo") String tipo);
}
