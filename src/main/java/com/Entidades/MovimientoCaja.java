package com.Entidades;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.Enum.MedioPago;


@Entity
@Data
@Table(name = "movimientos_caja")
public class MovimientoCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaHora;
    
    private String responsable; // Nombre de la persona que lo hace
    
    private BigDecimal monto;
    
    @Enumerated(EnumType.STRING)
    private MedioPago origenMedio; // Usando el Enum
    
    @Enumerated(EnumType.STRING)
    private MedioPago destinoMedio; // Usando el Enum
    
    private String motivo; // Opcional: descripción breve

    public MovimientoCaja() {
        this.fechaHora = LocalDateTime.now();
    }

}