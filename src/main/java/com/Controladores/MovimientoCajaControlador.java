package com.Controladores;

import com.Entidades.MovimientoCaja;
import com.Enum.MedioPago;
import com.Repositorio.MovimientoCajaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
@RequestMapping("/caja")
public class MovimientoCajaControlador {

    @Autowired
    private MovimientoCajaRepositorio movimientoRepo;

    @GetMapping("/movimientos")
    public String verMovimientos(Model model) {
        model.addAttribute("movimientos", movimientoRepo.findAll());
        return "movimiento-caja";
    }

   @PostMapping("/movimientos/guardar")
    public String guardarMovimiento(
        @RequestParam String responsable,
        @RequestParam BigDecimal monto,       // Monto base (ingresado por el usuario)
        @RequestParam BigDecimal montoFinal,  // Monto con recargo (calculado por el hidden de JS)
        @RequestParam String origenMedio,
        @RequestParam String destinoMedio,
        @RequestParam(required = false) String motivo) {

    try {
        MedioPago origenEnum = MedioPago.valueOf(origenMedio);
        MedioPago destinoEnum = MedioPago.valueOf(destinoMedio);

        if (origenEnum != destinoEnum && monto != null && monto.compareTo(BigDecimal.ZERO) > 0) {
            
            MovimientoCaja mov = new MovimientoCaja();
            mov.setResponsable(responsable);
            mov.setMonto(monto);       // Guardamos el que vamos a descontar del origen
            mov.setMontoFinal(montoFinal); // Guardamos el que vamos a sumar al destino (con recargo)
            mov.setOrigenMedio(origenEnum);
            mov.setDestinoMedio(destinoEnum);
            
            String detalle = " (Base: $" + monto + " | Con recargo: $" + montoFinal + ")";
            mov.setMotivo((motivo != null && !motivo.trim().isEmpty() ? motivo : "Traspaso") + detalle);

            movimientoRepo.save(mov);
        }
    } catch (IllegalArgumentException e) {
        // Manejo de errores
    }

    return "redirect:/caja/movimientos?exito=true";
}
}
    

