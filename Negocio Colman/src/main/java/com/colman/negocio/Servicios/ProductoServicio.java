package com.colman.negocio.Servicios;

import com.colman.negocio.Entidades.Producto;
import com.colman.negocio.Exception.MiException;
import com.colman.negocio.Repositorio.ProductoRepositorio;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoServicio {

    @Autowired
    ProductoRepositorio productoRepositorio;

    @Transactional
    public void crearProducto(Long id, String nombre, Integer precioCosto, Integer precioVenta, String tipo)
            throws MiException {

        Validar(nombre, precioCosto, precioVenta);
        Producto producto = new Producto();

        producto.setId(id);
        producto.setNombre(nombre);
        producto.setPrecioCosto(precioCosto);
        producto.setPrecioVenta(precioVenta);
        producto.setTipoProducto(tipo);

        productoRepositorio.save(producto);

    }

    public Producto getOne(Long id) {
        return productoRepositorio.getReferenceById(id);
    }

    @Transactional
    public void Actualizar(String nombre, Integer precioCosto, Integer precioVenta, Long id) throws MiException {

        Optional<Producto> respuesta = productoRepositorio.findById(id);

        Validar(nombre, precioCosto, precioVenta);

        if (respuesta.isPresent()) {
            Producto producto = respuesta.get();

            producto.setNombre(nombre);
            producto.setPrecioCosto(precioCosto);
            producto.setPrecioVenta(precioVenta);

            productoRepositorio.save(producto);
        }

    }

    @Transactional
    public List<Producto> Listar() {
        return productoRepositorio.Ordernar();

    }

    public void Validar(String nombre, Integer precioCosto, Integer precioVenta) throws MiException {

        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre no puede ser nulo o estar vacio");
        }
        if (precioCosto == null) {
            throw new MiException("Precio costo invalido");
        }
        if (precioVenta == null) {
            throw new MiException("Precio venta invalido");
        }

    }
}
