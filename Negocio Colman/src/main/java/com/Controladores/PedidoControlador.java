package com.Controladores;

import com.Exception.MiException;
import com.Servicios.PedidoServicio;
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
@RequestMapping("/pedido")
@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
public class PedidoControlador {

    @Autowired
    PedidoServicio pedidoServicio;

    @GetMapping("/agregar")
    public String agregarPedido() {
        return "pedido_agregar.html";
    }

    @PostMapping("/agregado")
    public String agregadoPedido(String nombre, @RequestParam(required = false) Integer cantidad, ModelMap modelo) {
        try {
            pedidoServicio.agregar(nombre, cantidad);
            modelo.put("Exito", "El pedido ha sido agregado");
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
        }
        return "redirect:/pedido/agregar";
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        return "pedido_lista.html";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarPedido(@PathVariable Long id, ModelMap modelo) {

        pedidoServicio.eliminar(id);

        return "pedido_lista.html";
    }
    
    

}
