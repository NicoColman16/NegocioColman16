package com.Controladores;

import com.Entidades.Usuario;
import com.Exception.MiException;
import com.Servicios.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    @Lazy
    private UsuarioServicio usuarioServicio;

    @GetMapping("/")
    public String Inicio() {
        return "inicio.html";
    }

    @GetMapping("/login")
    public String Login() {
        return "login.html";
    }

    @GetMapping("/registrar")
    public String registrar() {
        return "usuario_form.html";
    }

// PortalControlador.java

@PostMapping("/registro")
public String registro(@RequestParam String nombre, @RequestParam String email,
                       @RequestParam String password, @RequestParam String password2,
                       ModelMap modelo) throws MiException {

     try {
        usuarioServicio.crearUsuario(nombre, email, password, password2);
        modelo.put("exito", "usuario creado");
        System.out.println("Exito");
        return "login.html";

    } catch (Exception ex) { 
        Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
        
        // Determina el mensaje de error:
        String mensajeError = (ex instanceof MiException) 
            ? ex.getMessage() 
            : "Error al registrar: Revisar logs (Posible duplicado o problema de DB).";
            
        modelo.put("error", mensajeError);
        modelo.put("nombre", nombre);
        modelo.put("email", email);
        System.out.println("Error");
        return "usuario_form.html";
    }
}



    @PreAuthorize("hasAnyRole('USER','ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        return "pagina_principal.html";
    }
}
