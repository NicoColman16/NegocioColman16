
const textoMontoFinal = document.getElementById("textoMontoFinal");

// Función que realiza el cálculo de comisiones
function calcularMontoConComision(montoBase, origen, destino) {
    let porcentaje = 0;
    const origenUpper = (origen || "").toUpperCase();
    const destinoUpper = (destino || "").toUpperCase();

    // Reglas del 10%
    if (
        (origenUpper === 'TRANSFERENCIA' && destinoUpper === 'EFECTIVO') || 
        (origenUpper === 'EFECTIVO' && destinoUpper === 'TRANSFERENCIA')
    ) {
        porcentaje = 0.10; 
    } 
    // Reglas del 20%
    else if (
        origenUpper === 'TARJETA' && 
        (destinoUpper === 'EFECTIVO' || destinoUpper === 'TRANSFERENCIA')
    ) {
        porcentaje = 0.20;
    }
    
     else if (
        
        (origenUpper === 'EFECTIVO' || origenUpper === 'TRANSFERENCIA') &&
        destinoUpper === 'TARJETA'
    ) {
        porcentaje = 0.20;
    }

    const comision = montoBase * porcentaje;
    const montoFinal = montoBase + comision;

    return {
        porcentajeAplicado: `${porcentaje * 100}%`,
        comision: comision,
        montoFinal: montoFinal
    };
}

// Función que actualiza los elementos visuales del recuadro
function actualizarRecuadroComision() {
    // Captura los valores de tus inputs (ajusta los IDs según los nombres que tengas en tu HTML)
    const inputMonto = document.getElementById("monto"); // ID de tu input de dinero
    const selectOrigen = document.getElementById("origenMedio");   // ID de tu selector u opción de origen
    const selectDestino = document.getElementById("destinoMedio");// ID de tu selector u opción de destino
    const hiddenDineroFinal = document.getElementById("input-dinero-final");

    // Si no existen los elementos en esta vista particular, evitamos errores
    if (!inputMonto) return;

    const montoBase = parseFloat(inputMonto.value) || 0;
    const origen = selectOrigen ? selectOrigen.value : '';
    const destino = selectDestino ? selectDestino.value : '';

    // Ejecutamos el cálculo
    const resultado = calcularMontoConComision(montoBase, origen, destino);

    // Actualizamos el DOM con los resultados en tiempo real
    document.getElementById("lbl-porcentaje").innerText = resultado.porcentajeAplicado;
    document.getElementById("lbl-comision").innerText = `$ ${resultado.comision.toFixed(2)}`;
    document.getElementById("lbl-total-final").innerText = `$ ${resultado.montoFinal.toFixed(2)}`;
    
    if (hiddenDineroFinal) {
        hiddenDineroFinal.value = resultado.montoFinal.toFixed(2);
    }
}

// Vinculamos los eventos para que reaccionen al instante al escribir o cambiar opciones
document.addEventListener("DOMContentLoaded", () => {
    const inputMonto = document.getElementById("monto");
    const selectOrigen = document.getElementById("origenMedio");
    const selectDestino = document.getElementById("destinoMedio");

    if (inputMonto) {
        inputMonto.addEventListener("input", actualizarRecuadroComision);
    }
    if (selectOrigen) {
        selectOrigen.addEventListener("change", actualizarRecuadroComision);
    }
    if (selectDestino) {
        selectDestino.addEventListener("change", actualizarRecuadroComision);
    }
});