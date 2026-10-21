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
        @RequestParam BigDecimal monto,
        @RequestParam String origenMedio,
        @RequestParam String destinoMedio,
        @RequestParam(required = false) String motivo) {

    try {
        MedioPago origenEnum = MedioPago.valueOf(origenMedio);
        MedioPago destinoEnum = MedioPago.valueOf(destinoMedio);

        if (origenEnum != destinoEnum && monto != null && monto.compareTo(BigDecimal.ZERO) > 0) {
            
            BigDecimal montoFinal = monto;

            // APLICAR REGLA INVERSA SEGÚN EL TIPO DE OPERACIÓN
            // Ejemplo: Si entran 2000 en Tarjeta (destino), aplicamos el descuento/conversión para entregar menos en Efectivo (origen)
            if (destinoEnum == MedioPago.TARJETA) {
                // Descuento del 20% (ej: de 2000 pasaría a 1600)
                BigDecimal descuento = monto.multiply(new BigDecimal("0.20"));
                montoFinal = monto.subtract(descuento);
            } else if (origenEnum == MedioPago.EFECTIVO && destinoEnum == MedioPago.TRANSFERENCIA) {
                // Si aplicas alguna regla específica para efectivo -> transferencia, la ajustas aquí.
                // Por ejemplo, si quisieras aplicar un 10% de diferencia:
                // BigDecimal ajuste = monto.multiply(new BigDecimal("0.10"));
                // montoFinal = monto.subtract(ajuste); // o .add() según necesites
            }

            MovimientoCaja mov = new MovimientoCaja();
            mov.setResponsable(responsable);
            mov.setMonto(montoFinal); // Guardamos el monto ya ajustado
            mov.setOrigenMedio(origenEnum);
            mov.setDestinoMedio(destinoEnum);
            
            // Dejar constancia del monto original en el motivo
            String baseStr = " (Monto ingresado: $" + monto + ")";
            String motivoFinal = (motivo != null && !motivo.trim().isEmpty() ? motivo : "Traspaso con conversión") + baseStr;
            mov.setMotivo(motivoFinal);

            movimientoRepo.save(mov);
        }
    } catch (IllegalArgumentException e) {
        // Manejo por si llega un valor inválido
    }

    return "redirect:/caja/movimientos?exito=true";
}
} 
    

