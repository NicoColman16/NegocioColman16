package com.Controladores;

import com.Entidades.Venta;
import com.Enum.MedioPago;
import com.Entidades.MovimientoCaja;
import com.Entidades.Producto;
import com.Services.VideoAuditService;

import com.Repositorio.VentaRepositorio;
import com.Repositorio.MovimientoCajaRepositorio;
import com.Repositorio.ProductoRepositorio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/venta")
@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
@CrossOrigin(origins = "*")

public class VentaControlador {

    @Autowired
    private VentaRepositorio ventaRepo;
    @Autowired
    private VideoAuditService videoAuditService;
    @Autowired
    private MovimientoCajaRepositorio movimientoRepo;

    @GetMapping("/nuevo")
    public String agregar() {
        // modelo.put("tipo", MedioPago.values());
        return "venta.html";
    }

    @GetMapping("/inventario")
    public String ver() {
        return "inventario.html";
    }

    @Autowired
    private ProductoRepositorio productoRepository;

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarProducto(@RequestParam("query") String query) {
        // 1. Intentar buscar por código de barras exacto
        Optional<Producto> productoOpt = productoRepository.findByCodigoBarras(query);

        if (productoOpt.isPresent()) {
            return ResponseEntity.ok(productoOpt.get());
        }

        // 2. Si no es un código exacto, buscar el primer producto cuyo nombre coincida
        Optional<Producto> porNombre = productoRepository.findFirstByNombreContainingIgnoreCase(query);
        if (porNombre.isPresent()) {
            return ResponseEntity.ok(porNombre.get());
        }

        // 3. Si no encuentra nada, retornar 404
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/vendido")
    public ResponseEntity<Venta> registrarVenta(@RequestBody Venta venta) {
        // 1. Asignar la hora exacta del cobro
        venta.setFechaHora(LocalDateTime.now());

        // 2. Guardar venta inicial (montoTotal y medioPago ya vienen en el JSON)
        Venta ventaGuardada = ventaRepo.save(venta);

        // 3. Generar la URL para la auditoría de video
        String urlVideo = videoAuditService.generarUrlClipVideo(ventaGuardada.getFechaHora());
        ventaGuardada.setRutaVideoAuditoria(urlVideo);

        // 4. Actualizar y retornar
        return ResponseEntity.ok(ventaRepo.save(ventaGuardada));
    }

    @GetMapping("/reportes")
    public String verReporteTransacciones(
            @RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta,
            @RequestParam(required = false) String medioPago,
            @RequestParam(required = false) String monto,
            @RequestParam(required = false) String passwordAdmin,
            Model model) {


        // 1. Validar la contraseña (si usas la protección de clave)
        List<Venta> ventas = new ArrayList<>();

        LocalDateTime fechaDesde = null;
        LocalDateTime fechaHasta = null;

        if (desde == null || desde.trim().isEmpty()) {
            LocalDate hoy = LocalDate.now();
            // Formato requerido por datetime-local: yyyy-MM-ddTHH:mm
            desde = hoy.atTime(15, 0).toString();
        }
        // 2. Asignar fecha 'hasta' por defecto (hoy a las 23:59:59) si viene vacía
        if (hasta == null || hasta.trim().isEmpty()) {
            hasta = LocalDate.now().atTime(23, 59, 59).toString();
        }

        try {
            if (desde != null && !desde.trim().isEmpty()) {
                fechaDesde = LocalDateTime.parse(desde);
            }
            if (hasta != null && !hasta.trim().isEmpty()) {
                fechaHasta = LocalDateTime.parse(hasta);
            }
        } catch (Exception e) {
            System.err.println("Error parseando fechas: " + e.getMessage());
        }

        if ("1234".equals(passwordAdmin)) {

            MedioPago enumMedioPago = null;
            if (medioPago != null && !medioPago.trim().isEmpty()) {
                try {
                    // .trim() saca espacios y .toUpperCase() evita problemas de
                    // mayúsculas/minúsculas
                    enumMedioPago = MedioPago.valueOf(medioPago.trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Medio de pago no válido recibido: " + medioPago);
                    enumMedioPago = null;
                }
            }

            BigDecimal montoDecimal = (monto != null && !monto.trim().isEmpty()) ? new BigDecimal(monto) : null;

            // Buscamos las ventas con los filtros
            ventas = ventaRepo.filtrarVentas(fechaDesde, fechaHasta, enumMedioPago, montoDecimal);
        
        }

        model.addAttribute("ventas", ventas);

        // Pasamos la variable 'desde' modificada al modelo para que el input HTML la
        // recuerde
        model.addAttribute("desdeParam", desde);
        model.addAttribute("hastaParam", hasta);

        // 2. Cálculo base sumando las ventas
        BigDecimal totalEfectivo = BigDecimal.ZERO;
        BigDecimal totalTarjeta = BigDecimal.ZERO;
        BigDecimal totalTransferencia = BigDecimal.ZERO;
        BigDecimal totalRecarga = BigDecimal.ZERO;
        BigDecimal totalGeneral = BigDecimal.ZERO;


        // Iteramos sobre 'ventas' (que ya tiene aplicados los filtros de fecha, medio
        // de pago, monto, etc.)
        for (Venta v : ventas) {
            if (v.getMontoTotal() != null && v.getMedioPago() != null) {
                BigDecimal valor = v.getMontoTotal();
                totalGeneral = totalGeneral.add(valor);

                switch (v.getMedioPago()) {
                    case EFECTIVO -> totalEfectivo = totalEfectivo.add(valor);
                    case TARJETA -> totalTarjeta = totalTarjeta.add(valor);
                    case TRANSFERENCIA -> totalTransferencia = totalTransferencia.add(valor);
                    case RECARGA -> totalRecarga = totalRecarga.add(valor);
                }
            }
        }

        // 3. Ajuste con movimientos de caja (filtrados por fecha si se ingresaron
        // rangos)
         if ("1234".equals(passwordAdmin)) {
        List<MovimientoCaja> listaMovimientos = movimientoRepo.findAll();
        for (MovimientoCaja m : listaMovimientos) {
            if (m.getMonto() != null) {

                // Si hay filtro de fecha, verificamos que el movimiento esté dentro del rango
                // buscado
                boolean dentroDeFecha = true;
                if (m.getFechaHora() != null) {
                    if (fechaDesde != null && m.getFechaHora().isBefore(fechaDesde)) {
                        dentroDeFecha = false;
                    }
                    if (fechaHasta != null && m.getFechaHora().isAfter(fechaHasta)) {
                        dentroDeFecha = false;
                    }
                }

                if (dentroDeFecha) {
                    BigDecimal montoMov = m.getMonto();

                    // Restar de Origen
                    if (m.getOrigenMedio() == MedioPago.EFECTIVO)
                        totalEfectivo = totalEfectivo.subtract(montoMov);
                    if (m.getOrigenMedio() == MedioPago.TARJETA)
                        totalTarjeta = totalTarjeta.subtract(montoMov);
                    if (m.getOrigenMedio() == MedioPago.TRANSFERENCIA)
                        totalTransferencia = totalTransferencia.subtract(montoMov);
                    if (m.getOrigenMedio() == MedioPago.RECARGA)
                        totalRecarga = totalRecarga.subtract(montoMov);

                    // Sumar a Destino
                    if (m.getDestinoMedio() == MedioPago.EFECTIVO)
                        totalEfectivo = totalEfectivo.add(montoMov);
                    if (m.getDestinoMedio() == MedioPago.TARJETA)
                        totalTarjeta = totalTarjeta.add(montoMov);
                    if (m.getDestinoMedio() == MedioPago.TRANSFERENCIA)
                        totalTransferencia = totalTransferencia.add(montoMov);
                    if (m.getDestinoMedio() == MedioPago.RECARGA)
                        totalRecarga = totalRecarga.add(montoMov);
                }
            }
        }
        }
        
        // 4. Pasar todos los totales calculados a la vista
        model.addAttribute("totalEfectivo", totalEfectivo);
        model.addAttribute("totalTarjeta", totalTarjeta);
        model.addAttribute("totalTransferencia", totalTransferencia);
        model.addAttribute("totalRecarga", totalRecarga);
        model.addAttribute("totalGeneral", totalGeneral);
        model.addAttribute("cantidadRegistros", ventas.size());
        model.addAttribute("ventas", ventas);


        // 5. Retornar el nombre de la vista HTML (Este es el String que requiere el
        // método)
        return "venta_list.html";
    }
}
