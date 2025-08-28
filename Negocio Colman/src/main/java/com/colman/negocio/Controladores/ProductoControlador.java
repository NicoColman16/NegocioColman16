package com.colman.negocio.Controladores;

import com.colman.negocio.Entidades.Producto;
import com.colman.negocio.Enum.TipoProducto;
import com.colman.negocio.Exception.MiException;
import com.colman.negocio.Repositorio.ProductoRepositorio;
import com.colman.negocio.Servicios.ProductoServicio;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/producto")
@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
public class ProductoControlador {

    @Autowired
    private ProductoServicio productoServicio;

    @Autowired
    private ProductoRepositorio productoRepositorio;

    @GetMapping("/crear")
    public String crear(ModelMap modelo) {

        modelo.put("tipos", TipoProducto.values());
        return "producto.html";
    }

    @PostMapping("/creado")
    public String creado(Long id, String nombre, Integer precioCosto, Integer PrecioVenta, @RequestParam String tipoP, ModelMap modelo) {
        try {
            System.out.println(tipoP);
            productoServicio.crearProducto(id, nombre, precioCosto, PrecioVenta, tipoP);
            modelo.put("Exito", "EL producto ha sido registrado");
            productoRepositorio.Ordernar();
            return "redirect:/producto/crear";

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "producto.html";

        }
    }

    @GetMapping("/lista")
    public String listaProducto(ModelMap modelo) {
        modelo.put("tipos", TipoProducto.values());
        List<Producto> lista = new ArrayList();
        lista = productoServicio.Listar();
        modelo.put("lista", lista);

        return "listar_producto.html";
    }

    @PostMapping("/listar")
    public String listaBuscar(@RequestParam(required = false) String nombre, @RequestParam(required = false) String valor,
            ModelMap modelo) {
        System.out.println("nombre: " + nombre);

        if (!nombre.isEmpty()) {
            List<Producto> producto = productoRepositorio.BuscarPorNombre(nombre);
            modelo.put("lista", producto);
            System.out.println("nombre no: " + nombre);
        } else if (!valor.isEmpty()) {
            List<Producto> lista = productoRepositorio.BuscarporTipo(valor);
            System.out.println("valor: " + valor);
            modelo.put("lista", lista);
        }

        return "listar_producto.html";
    }
    
    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo){
        Producto producto = productoServicio.getOne(id);
        modelo.put("producto", producto);
        return "producto_modificar.html";
    }
    
    @PostMapping("/modificado/{id}")
    public String modificado(@PathVariable String id,String nombre,Integer precioCosto,Integer precioVenta,ModelMap modelo){
        try {
            productoServicio.Actualizar(nombre, precioCosto, precioVenta, id);
            return "redirect:/producto/lista";
        } catch (MiException ex) {
            Logger.getLogger(ProductoControlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
        }
        return "producto_modificar.html";
    }
}
