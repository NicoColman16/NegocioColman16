package com.Entidades;

import com.Enum.MedioPago;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoRecibido; // Para calcular y auditoriar el billete ingresado

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal vueltoEntregado; // Vuelto calculado (montoRecibido - montoTotal)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MedioPago medioPago; // EFECTIVO, MERCADO_PAGO, DEBITO, CREDITO, TRANSFERENCIA

    @Column(length = 500)
    private String rutaVideoAuditoria; // Guarda la URL o la ruta del archivo MP4 guardado

  
}