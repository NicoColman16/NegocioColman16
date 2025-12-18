package com.Servicios;

import com.Entidades.Venta;
import com.Enum.TipoVenta;
import com.Exception.MiException;
import com.Repositorio.VentaRepositorio;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VentaServicio {

    @Autowired
    private VentaRepositorio ventaRepositorio;

    @Transactional
    public void crearVenta(String tipo, Long monto) throws MiException {

        Validar(tipo, monto);

        Venta venta = new Venta();
        Date fechaV = new Date();

        if(tipo.equals("Recarga")){
            Venta venta2 = new Venta();
            Date fechaV2 = new Date();
            
            long num = monto;
            
            if (monto >= 3000){
                num = num + 500;
            } else{
                num = num + ((num*20)/100);
            }
            venta2.setTipo("Efectivo");
            venta2.setMonto(num);
            venta2.setFechaVenta(fechaV2);
            ventaRepositorio.save(venta2);
        }

        venta.setTipo(tipo);
        venta.setMonto(monto);
        venta.setFechaVenta(fechaV);

        ventaRepositorio.save(venta);
    }

    @Transactional
    public List<Venta> listar() {
        return ventaRepositorio.findAll();
    }

    @Transactional
    public List<Venta> totalVentaTipo(Date fecha) {
        
        List<Venta> ventas = new ArrayList();
       
        for (TipoVenta value : TipoVenta.values()) {
                 
            Venta venta = new Venta();

            venta.setTipo(value.toString());
            venta.setMonto(ventaRepositorio.totalByFechaVentaAndTipo(fecha, value.toString()));
            ventas.add(venta);
            System.out.println(venta.getMonto());
            
        }
        
        return ventas;
    }

    @Transactional
    public Long totalVentaFecha(Date fecha) {
        return ventaRepositorio.totalFechaVenta(fecha);
    }

    @Transactional
    public Long totalDia (Date fecha) throws MiException{
        

        long total = ventaRepositorio.totalByFechaDia(fecha);
    
        System.out.println(total + "Este es el total");
        return total;
    }

    public void Validar(String tipo, Long monto) throws MiException {

        if (tipo.isEmpty() || tipo == null) {
            throw new MiException("Tipo invalido");
        }
        if (monto == null) {
            throw new MiException("Monto nulo");
        }
    }
}
