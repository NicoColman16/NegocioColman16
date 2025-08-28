package com.colman.negocio.Controladores;

import com.colman.negocio.Entidades.Usuario;
import com.colman.negocio.Exception.MiException;
import com.colman.negocio.Servicios.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
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
@RequestMapping("/home")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/")
    public String Inicio() {
        return "index.html";
    }

    @GetMapping("/login")
    public String Login() {
        return "login.html";
    }

    @GetMapping("/registrar")
    public String registrar() {
        return "usuario_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String email,
            @RequestParam String password, @RequestParam String password2, ModelMap modelo) {
        try {
            usuarioServicio.crearUsuario(nombre, email, password, password2);
            modelo.put("exito", "usuario creado");
            System.out.println("Exito");
            return "login.html";
        } catch (MiException ex) {
            Logger.getLogger(PortalControlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            System.out.println("Error");
            return "usuario_form.html";

        }

    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        return "pagina_principal.html";
    }
}
