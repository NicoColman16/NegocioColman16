package com.colman.negocio.Controladores;

import com.colman.negocio.Entidades.Venta;
import com.colman.negocio.Enum.TipoVenta;
import com.colman.negocio.Exception.MiException;
import com.colman.negocio.Repositorio.VentaRepositorio;
import com.colman.negocio.Servicios.VentaServicio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/venta")
@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
public class VentaControlador {

    @Autowired
    private VentaServicio ventaServicio;

    @GetMapping("/nuevo")
    public String agregar(ModelMap modelo) {
        modelo.put("tipo", TipoVenta.values());

        return "venta.html";
    }

    @PostMapping("/vendido")
    public String vendido(@RequestParam String tipo, @RequestParam Integer monto, ModelMap modelo) {

        try {
            ventaServicio.crearVenta(tipo, monto);
            modelo.put("Exito", "Venta realizada");
            return "redirect:/venta/nuevo";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "venta.html";
        }
    }

    @GetMapping("/lista")
    public String listarVenta(ModelMap modelo) {
        Date fecha = new Date();
        List<Venta> venta = new ArrayList();
        venta = ventaServicio.listar();
        modelo.addAllAttributes(venta);
        Long total = ventaServicio.totalVentaFecha(fecha);
        modelo.put("total", total);

        return "venta_list.html";
    }

    @GetMapping("/total")
    public String totalVenta(@RequestParam(required = false) Date fechas, @RequestParam(required = false) String tipo, ModelMap modelo) {

        Date fecha = new Date();
        List<Venta> venta = new ArrayList();
        venta = ventaServicio.listar();
        modelo.addAllAttributes(venta);
        Long total;
        List<Venta> ventasTipo = new ArrayList();

        total = ventaServicio.totalVentaFecha(fecha);
        ventasTipo = ventaServicio.totalVentaTipo(fecha);

        modelo.put("Venta", total);
        modelo.put("VentaTipo", ventasTipo);
        return "venta_list.html";
    }

}
