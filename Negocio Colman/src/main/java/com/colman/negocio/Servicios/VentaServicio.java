package com.colman.negocio.Servicios;

import com.colman.negocio.Entidades.Venta;
import com.colman.negocio.Enum.TipoVenta;
import com.colman.negocio.Exception.MiException;
import com.colman.negocio.Repositorio.VentaRepositorio;
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
    public void crearVenta(String tipo, Integer monto) throws MiException {

        Validar(tipo, monto);

        Venta venta = new Venta();
        Date fechaV = new Date();

        venta.setTipo(tipo);
        venta.setMonto(monto);
        venta.setFechaVenta(fechaV);

        ventaRepositorio.save(venta);
    }

    @Transactional
    public List<Venta> listar() {
        List<Venta> lista = new ArrayList();

        lista = ventaRepositorio.findAll();

        return lista;
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

    public void Validar(String tipo, Integer monto) throws MiException {

        if (tipo.isEmpty() || tipo == null) {
            throw new MiException("Tipo invalido");
        }
        if (monto == null) {
            throw new MiException("Monto nulo");
        }
    }
}
