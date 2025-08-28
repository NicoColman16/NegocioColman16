package com.colman.negocio.Servicios;

import com.colman.negocio.Entidades.Pedido;
import com.colman.negocio.Exception.MiException;
import com.colman.negocio.Repositorio.PedidoRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoServicio {

@Autowired
private PedidoRepositorio pedidoRepositorio;

@Transactional(readOnly = true)
public void agregar(String nombre, Integer cantidad) throws MiException{
    validar(nombre);
    Pedido pedido = new Pedido();
    
    pedido.setNombre(nombre);    
    if(cantidad != null){
        pedido.setCantidad(cantidad);
    }else {
        pedido.setCantidad(0);
    }
    
    pedidoRepositorio.save(pedido);
    
}


@Transactional(readOnly = true)
public List<Pedido> listar(){
    List<Pedido> lista = new ArrayList();
    lista = pedidoRepositorio.findAll();
    return lista;
}
@Transactional(readOnly = true)
public void eliminar(String id){
    Optional<Pedido> respuesta = pedidoRepositorio.findById(id);
    
    if(respuesta.isPresent()){
        Pedido pedido = respuesta.get();
        
        pedidoRepositorio.delete(pedido);
    }
}

public void validar(String pedido) throws MiException{

    if(pedido.isEmpty() || pedido == null) throw new MiException("No puede estar vacio");
}

}
