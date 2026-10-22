package com.Controladores;

import com.Entidades.Producto;
import com.Repositorio.ProductoRepositorio;
import com.Services.ProductoServicio;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {

    @Autowired
    private ProductoRepositorio productoRepository;
    private ProductoServicio productoServicio;

 @GetMapping("/listar")
public ResponseEntity<Page<Producto>> listarProductos(
        @RequestParam(defaultValue = "") String query,
        @RequestParam(required = false) Boolean soloFaltas,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "15") int size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by("nombre").ascending());
    Page<Producto> productosPage;

    // Si 'soloFaltas' es true, filtramos solo los que tienen falta en true
    if (Boolean.TRUE.equals(soloFaltas)) {
        // Nota: Asegúrate de tener un método en tu repositorio que soporte esto, 
        // por ejemplo, buscando por query y filtrando faltas a la vez.
        productosPage = productoRepository.buscarFaltasConQuery(query, true, pageable);
    } else {
        // Tu método original que ya te funciona para la búsqueda normal
        productosPage = productoRepository.buscarProductosPaginados(query, pageable);
    }

    return ResponseEntity.ok(productosPage);
}


    @GetMapping ("/inventario")
    public String mostrarPuntoDeVenta() {
        // Retorna el nombre de tu archivo html (src/main/resources/templates/ventas.html)
        return "inventario.html"; 
    }

    // 2. Endpoint rápido para actualizar Precio y Stock directamente desde la tabla
    @PutMapping("/actualizar-quick/{id}")
    public ResponseEntity<?> actualizarQuick(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload) {

        Integer nuevoPrecio = Integer.valueOf(payload.get("precioVenta").toString());
        Integer nuevoStock = Integer.valueOf(payload.get("stockActual").toString());

        Producto producto = productoServicio.obtenerPorId(id);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        producto.setPrecioVenta(nuevoPrecio);
        producto.setStockActual(nuevoStock);
        
        productoServicio.guardar(producto);

        return ResponseEntity.ok().body(Map.of("message", "Producto actualizado correctamente"));
    }

// 1. Endpoint que recibe la redirección cuando presionás Editar (ej: /producto/modificar?id=5)
    @GetMapping("/modificar")
    public String vistaModificar(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            Producto producto = productoServicio.obtenerPorId(id);
            model.addAttribute("productoEditar", producto);
        }
        return "modificar-producto"; // Nombre del archivo HTML
    }

    // 2. Endpoint POST que guarda los cambios en la BD y recarga la pantalla
    @PostMapping("/guardar-edicion")
    public String guardarEdicionProducto(@ModelAttribute Producto productoForm) {
        // Mantiene valores previos o actualiza el registro existente en la DB
        productoServicio.guardar(productoForm);
        
        // Redirige de vuelta a la pantalla de modificación limpia
        return "redirect:/producto/modificar";
    }

    @PostMapping("/toggle-falta/{id}")
    public ResponseEntity<?> toggleFalta(@PathVariable Long id) {
    Optional<Producto> productoOpt = productoRepository.findById(id);
    
    if (productoOpt.isPresent()) {
        Producto producto = productoOpt.get();
        
        // Invierte el valor actual del booleano
        boolean nuevoEstado = !producto.isFalta();
        producto.setFalta(nuevoEstado);
        
        productoRepository.save(producto);
        
        // Retorna un código 200 OK con el nuevo estado si deseas usarlo en el frontend
        return ResponseEntity.ok().body(Collections.singletonMap("nuevoEstado", nuevoEstado));
    }
    
    // Si no encuentra el producto, retorna un 404 Not Found
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
}

}
