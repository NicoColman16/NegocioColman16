    const inputMonto = document.getElementById('monto');
    const selectOrigen = document.getElementById('origenMedio');
    const selectDestino = document.getElementById('destinoMedio');
    const textoMontoFinal = document.getElementById('textoMontoFinal');

    function calcularRecargo() {
        let montoBase = parseFloat(inputMonto.value) || 0;
        let origen = selectOrigen.value;
        let destino = selectDestino.value;
        let porcentaje = 0;

        // Reglas de recargo según lo que charlamos
        if (origen === 'EFECTIVO') {
            porcentaje = 0.10; // 10% si retiran efectivo
        } else if (origen === 'TARJETA' || destino === 'TARJETA') {
            porcentaje = 0.20; // 20% si interviene tarjeta
        }

        let recargo = montoBase * porcentaje;
        let montoFinal = montoBase + recargo;

        // Mostrar el resultado formateado en pantalla
        textoMontoFinal.textContent = '$ ' + montoFinal.toFixed(2);
    }

    // Escuchar cuando el usuario escribe o cambia los selects
    inputMonto.addEventListener('input', calcularRecargo);
    selectOrigen.addEventListener('change', calcularRecargo);
    selectDestino.addEventListener('change', calcularRecargo);