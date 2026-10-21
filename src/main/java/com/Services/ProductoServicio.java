package com.Services;

import com.Entidades.Producto;
import com.Exception.MiException;
import com.Repositorio.ProductoRepositorio;

import io.micrometer.common.lang.NonNull;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductoServicio {

    @Autowired
    ProductoRepositorio productoRepository;

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    @NonNull
    public Producto obtenerPorId(Long id) {
        System.out.println("busque aqui");
        return productoRepository.findById(id).orElse(null);
    }

    public Producto obtenerPorCodigoBarra(String codigoBarras) {
        return productoRepository.findByCodigoBarras(codigoBarras).orElse(null);
    }

    public Page<Producto> buscarPorFiltroPaginado(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return productoRepository.findAll(pageable);
        }
        return productoRepository.buscarProductosPaginados(query.trim(), pageable);
    }

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    public void eliminarPorId(Long id) {
        productoRepository.deleteById(id);
    }
}
