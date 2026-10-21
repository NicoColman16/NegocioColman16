package com.Controladores;

import com.Entidades.Producto;

import com.Repositorio.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.HashSet;

@Controller
@RequestMapping("/productos")
public class ProductoControlador {

    @Autowired
    private ProductoRepositorio productoRepo;

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new Producto());
        return "producto.html";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto) {
        producto.setUltMod(new Date());
        
        if (producto.getFalta() == null) {
            producto.setFalta(false);
        }
        
        // Si no se marcó ninguna categoría, nos aseguramos que no sea null
        if (producto.getTiposProducto() == null) {
            producto.setTiposProducto(new HashSet<>());
        }

        productoRepo.save(producto);
        return "redirect:/productos/nuevo?exito=true";
    }
    // Endpoint GET para mostrar el formulario de edición cargando los datos existentes
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Producto producto = productoRepo.findById(id).orElse(null);
        if (producto == null) {
            return "redirect:/productos/nuevo?error=noencontrado";
        }
        model.addAttribute("producto", producto);
        return "producto.html"; // Reutilizamos la misma vista HTML
    }

    

    
}