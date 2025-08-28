package com.colman.negocio.Entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.Date;

import lombok.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Producto {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid", strategy = "uuid2")
    private Long id;
    
    private String nombre;
    private Integer precioCosto;
    private Integer precioVenta;
    
    private Date ultMod;
    
    
    private String tipoProducto;
}
