package com.colman.negocio.Servicios;

import com.colman.negocio.Entidades.Usuario;
import com.colman.negocio.Enum.Rol;
import com.colman.negocio.Exception.MiException;
import com.colman.negocio.Repositorio.UsuarioRepositorio;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioServicio implements UserDetailsService{
    
    @Autowired
    UsuarioRepositorio usuarioRepositorio;
    
    @Override
    public UserDetails loadUserByUsername(String email) 
            throws UsernameNotFoundException {
        Usuario user =usuarioRepositorio.buscarUsuario(email);
        
        if(user!= null){
            
            List<GrantedAuthority> permissions = new ArrayList<>();
            
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + user.getRol().toString());
            System.out.println(p);
            
            permissions.add(p);
            
            ServletRequestAttributes attr= (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            
            HttpSession session = attr.getRequest().getSession(true);
            
            session.setAttribute("usuariosession", user);
            
            return new User(user.getEmail(),user.getPassword(), permissions);         
        }
        return null;
        
    }
    
    @Transactional
    public void crearUsuario(String nombre, String mail, String contraseña, String contraseña2) throws MiException{
        Validar(nombre, mail, contraseña, contraseña2);
       
        Usuario usuario = new Usuario();
        
        usuario.setUsername(nombre);
        usuario.setEmail(mail);
        usuario.setPassword(new BCryptPasswordEncoder().encode(contraseña));
        usuario.setRol(Rol.USER);
        
        usuarioRepositorio.save(usuario);
    }
    
    @Transactional
    public void modificarUsuario(String nombre, String mail, String contraseña, String contraseña2, Long Id) throws MiException{
        Validar(nombre, mail, contraseña, contraseña2);
        
        Optional<Usuario> respuesta = usuarioRepositorio.findById(Id);
        
        if(respuesta.isPresent()){
            Usuario usuario = respuesta.get();
            
            usuario.setUsername(nombre);
            usuario.setEmail(mail);
            usuario.setPassword(contraseña);
            
            usuarioRepositorio.save(usuario);
        }
    }
    
    @Transactional
    public List<Usuario> listarUsuario(){
        List<Usuario> lista = new ArrayList();
        
        lista= usuarioRepositorio.findAll();
        return lista;
    }
    
    public Usuario getOne (Long id){
        
        return usuarioRepositorio.getOne(id);
    }
    
    public void Validar (String nombre, String mail, String contraseña, String contraseña2) throws MiException{
        
        if(nombre.isEmpty() || nombre == null){
            throw new MiException("Nombre invalido");
        }
        if (mail.isEmpty() || mail == null){
            throw new MiException("Mail invalido");
        }
        if (contraseña.isEmpty() || contraseña == null || contraseña.length() <= 5 || !contraseña.equals(contraseña2)){
            throw new MiException("Contraseña invalida");
        }
    }
}
