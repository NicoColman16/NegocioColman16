package com.Entidades;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.Enum.TipoProducto;

import lombok.*;


@Entity
@Data
@NoArgsConstructor
@Table(name = "Producto")
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String codigoBarras;
    private String nombre;
    private Integer precioCosto;
    private Integer precioVenta;
    private Integer stockActual;
    private Integer stockMinimo;
    
    private Date ultMod;

    private boolean falta;
    
    // NUEVO: Colección para soportar múltiples categorías del Enum
    @ElementCollection(targetClass = TipoProducto.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "producto_tipos", joinColumns = @JoinColumn(name = "producto_id"))
    @Column(name = "tipo_producto")
    private Set<TipoProducto> tiposProducto = new HashSet<>();
}
