package com.Entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

import lombok.*;


@Entity
@Data
@NoArgsConstructor
@Table(name = "Producto")
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private Integer precioCosto;
    private Integer precioVenta;
    
    private Date ultMod;
    
    
    private String tipoProducto;
}
