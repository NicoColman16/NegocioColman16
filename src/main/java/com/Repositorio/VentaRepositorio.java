package com.Repositorio;

import com.Entidades.Venta;
import com.Enum.MedioPago;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepositorio extends JpaRepository<Venta, Long> {
    
    // Filtrar por rango de fecha/hora y opcionalmente por medio de pago
    @Query("SELECT v FROM Venta v WHERE " +
           "(:desde IS NULL OR v.fechaHora >= :desde) AND " +
           "(:hasta IS NULL OR v.fechaHora <= :hasta) AND " +
           "(:medioPago IS NULL OR v.medioPago = :medioPago) AND " +
           "(:monto IS NULL OR v.montoTotal = :monto)")
    List<Venta> filtrarVentas(@Param("desde") LocalDateTime desde,
                              @Param("hasta") LocalDateTime hasta,
                              @Param("medioPago") MedioPago medioPago,
                              @Param("monto") BigDecimal monto);

}
