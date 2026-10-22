// ==========================================
// 1. ESTADO GLOBAL Y VARIABLES
// ==========================================
let currentFinalTotal = 0;
let cart = [];
let isSearching = false; // Flag para evitar lecturas dobles consecutivas (debounce)

// Redondear al múltiplo de 10 superior sin decimales
function roundToNextTen(amount) {
  return amount > 0 ? Math.ceil(amount / 10) * 10 : 0;
}

function editItemFromCart(codigoBarras) {
  if (!codigoBarras) {
    alert("El producto no tiene un código de barras válido.");
    return;
  }
  // Redirige a la vista de modificación pasando el código por URL
  window.location.href = `/producto/modificar?id=${encodeURIComponent(codigoBarras)}`;
}

// ==========================================
// 2. RENDERIZADO DEL CARRITO Y TOTALES
// ==========================================
function renderCart() {
  const tbody = document.getElementById("cart-table-body");
  if (tbody) tbody.innerHTML = "";

  let baseTotal = 0;
  let igv = 0;

  cart.forEach((item, index) => {
    const itemTotal = item.qty * item.unitPrice;
    baseTotal += itemTotal;

    if (tbody) {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${item.code}</td>
        <td>${item.name}</td>
        <td>
          <div class="qty-control" style="display: flex; align-items: center; gap: 6px;">
            <button type="button" class="btn-qty" onclick="updateQty(${index}, -1)" style="padding: 2px 8px; cursor: pointer;">-</button>
            <input type="number" 
                   value="${item.qty}" 
                   min="1" 
                   onchange="changeQtyInput(${index}, this.value)" 
                   style="width: 50px; text-align: center;" class="form-control">
            <button type="button" class="btn-qty" onclick="updateQty(${index}, 1)" style="padding: 2px 8px; cursor: pointer;">+</button>
          </div>
        </td>
        <td>$ ${item.unitPrice.toFixed(2)}</td>
        <td>$ ${itemTotal.toFixed(2)}</td>
        <td>
          <div class="action-icons">
            <i class="fa-solid fa-rotate icon-cart" 
              onclick="toggleFaltaEstado(${item.id})" 
              title="Cambiar Estado" 
              style="cursor: pointer;"></i>
            <i class="fa-solid fa-pen-to-square icon-edit" 
               onclick="editItemFromCart('${item.code}')" 
               title="Editar Producto" 
               style="cursor: pointer; color: #0d6efd;"></i><i class="fa-solid fa-trash icon-delete" 
               onclick="removeItem(${index})" 
               title="Eliminar"></i>
          </div>
        </td>
      `;
      tbody.appendChild(tr);
    }
  });

  // Método de pago y recargos

  const methodSelect = document.getElementById("payment-method");
  const method = methodSelect ? methodSelect.value : "efectivo";

  const checkRecarga = document.getElementById("checkRecarga");

  const esRecarga = checkRecarga ? checkRecarga.checked : false; // Para porcentajes

  // 1. Inicializamos las variables de recargo
  let surchargeRatio = 0; // Para porcentajes (TRANSFERENCIA, TARJETA)
  let surchargeAmount = 0; // Para montos fijos en pesos ($)

  // 2. Evaluamos según el método de pago o regla seleccionada
  if (method === "TRANSFERENCIA") {
    surchargeRatio = 0.05; // 5%
  } else if (method === "TARJETA") {
    surchargeRatio = 0.08; // 8%
  } else if (method === "RECARGA") {
    // Ajusta esto según el nombre que uses para tu tercera regla
    // Si la baseTotal es menor a 2000, cobra $20 cada $100
    if (baseTotal < 2499) {
      surchargeAmount = (baseTotal / 100) * 20;
    }
    // Si supera los 2500, se agrega un recargo fijo de 500 sin importar el monto
    else if (baseTotal >= 2500) {
      surchargeAmount = 500;
    }
  }

  // 3. Aplicamos tanto el porcentaje como el monto fijo al total base
  let rawTotal = baseTotal * (1 + surchargeRatio) + surchargeAmount;

  currentFinalTotal = roundToNextTen(rawTotal);

  // 4. Actualizar UI
  document.querySelectorAll(".summary-total-val").forEach((el) => {
    el.innerText = currentFinalTotal;
  });

  const setElemText = (id, text) => {
    const el = document.getElementById(id);
    if (el) el.innerText = text;
  };

  setElemText("summary-subtotal", currentFinalTotal.toFixed(2));
  setElemText("summary-igv", igv.toFixed(2));
  setElemText("main-total", currentFinalTotal);
  setElemText("side-total", currentFinalTotal);
  setElemText("items-count", cart.length);
  setElemText("items-total-count", cart.length);

  updatePaymentSummary();
}

//Cambio de Estado de producto

async function toggleFaltaEstado(id, eventTarget) {
    try {
        const response = await fetch(`/api/productos/toggle-falta/${id}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const data = await response.json();
            console.log("Estado actualizado:", data.nuevoEstado);

            alert("Producto cambiado");
            
            // O si preferís actualizar solo la tablita/carrito local sin recargar:
            // actualizarVistaLocal();

        } else {
            console.error("Error al actualizar el estado");
            alert("Error al cambiar");
        }
    } catch (error) {
        console.error("Error de red:", error);
    }
}

// Auxiliar para vuelto
function updatePaymentSummary() {
  const exactCashCheckbox = document.getElementById("exact-cash");
  const exactCash = exactCashCheckbox ? exactCashCheckbox.checked : false;
  const receivedInput = document.getElementById("received-amount");

  if (!receivedInput) return;

  const setElemText = (id, text) => {
    const el = document.getElementById(id);
    if (el) el.innerText = text;
  };

  if (exactCash) {
    receivedInput.value = currentFinalTotal;
    setElemText("summary-cash", currentFinalTotal);
    setElemText("summary-change", "0");
  } else {
    const received = parseFloat(receivedInput.value) || 0;
    const change = received - currentFinalTotal;

    setElemText("summary-cash", received);
    setElemText("summary-change", change > 0 ? change : "0");
  }
}

// Modificar cantidades
function updateQty(index, delta) {
  if (cart[index]) {
    const newQty = cart[index].qty + delta;
    if (newQty > 0) {
      cart[index].qty = newQty;
    } else {
      removeItem(index);
      return;
    }
    renderCart();
  }
}

function changeQtyInput(index, value) {
  const parsedQty = parseInt(value, 10);
  if (cart[index]) {
    cart[index].qty = !isNaN(parsedQty) && parsedQty > 0 ? parsedQty : 1;
    renderCart();
  }
}

function removeItem(index) {
  cart.splice(index, 1);
  renderCart();
}

// ==========================================
// 3. BÚSQUEDA Y MONTO MANUAL
// ==========================================
async function executeProductSearch() {
  const barcodeInput = document.getElementById("barcode-input");
  if (!barcodeInput) return;

  const query = barcodeInput.value.trim();
  if (!query) return;

  // Evitar ejecuciones duplicadas simultáneas
  if (isSearching) return;
  isSearching = true;

  try {
    const response = await fetch(
      `/venta/buscar?query=${encodeURIComponent(query)}`,
    );

    if (response.ok) {
      const productoDB = await response.json();

      const existingItemIndex = cart.findIndex(
        (item) =>
          item.code === productoDB.codigoBarra || item.id === productoDB.id,
      );

      if (existingItemIndex !== -1) {
        cart[existingItemIndex].qty += 1;
      } else {
        cart.push({
          id: productoDB.id,
          code: productoDB.codigoBarras,
          name: productoDB.nombre,
          qty: 1,
          unitPrice: parseFloat(productoDB.precioVenta),
        });
      }

      barcodeInput.value = "";
      renderCart();
    } else if (response.status === 404) {
      alert("Producto no encontrado.");
      barcodeInput.select();
    }
  } catch (error) {
    console.error("Error al buscar el producto:", error);
  } finally {
    isSearching = false;
  }
}

function agregarMontoManual() {
  const input = document.getElementById("inputMontoManual");
  if (!input) return;

  const valor = parseFloat(input.value);

  if (isNaN(valor) || valor <= 0) {
    alert("Ingrese un monto válido mayor a 0");
    return;
  }

  const tempId = "MANUAL_" + Date.now();
  const existingItemIndex = cart.findIndex(
    (item) =>
      item.id === tempId ||
      (item.code === "VARIOS" && item.unitPrice === valor),
  );

  if (existingItemIndex !== -1) {
    cart[existingItemIndex].qty += 1;
  } else {
    cart.push({
      id: tempId,
      code: "VARIOS",
      name: "Monto Manual / Varios",
      qty: 1,
      unitPrice: valor,
    });
  }

  input.value = "";
  renderCart();

  const barcodeInput = document.getElementById("barcode-input");
  if (barcodeInput) barcodeInput.focus();
}

// ==========================================
// 4. INICIALIZACIÓN DE EVENTOS (UNIFICADO)
// ==========================================
document.addEventListener("DOMContentLoaded", () => {
  const btnVenta = document.getElementById("btnRealizarVenta");

  if (btnVenta) {
    btnVenta.addEventListener("click", realizarVenta);
  }

  async function realizarVenta() {
    if (!cart || cart.length === 0) {
      alert("El carrito está vacío.");
      return;
    }

    const paymentMethodSelect = document.getElementById("payment-method");
    const medioPago = paymentMethodSelect
      ? paymentMethodSelect.value
      : "EFECTIVO";

    // Solo enviamos el total y el medio de pago
    const ventaData = {
      montoTotal: currentFinalTotal,
      medioPago: medioPago,
      montoRecibido: parseFloat(
        document.getElementById("received-amount")?.value || 0,
      ),
      vueltoEntregado: parseFloat(
        document.getElementById("summary-change")?.value || 0,
      ),
    };

    const btnVenta = document.getElementById("btnRealizarVenta");
    btnVenta.disabled = true;

    try {
      const response = await fetch("/venta/vendido", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(ventaData),
      });

      if (response.ok) {
        cart = [];
        renderCart(); // Limpia la tabla y el total en pantalla

        const inputCodigo = document.getElementById("codigoBarra");
        if (inputCodigo) inputCodigo.focus();

        alert("¡Venta registrada con éxito!");
      } else {
        alert("Error al registrar la venta.");
        console.log();
      }
    } catch (error) {
      console.error("Error de conexión:", error);
    } finally {
      btnVenta.disabled = false;
    }
  }

  // Lector de código de barras
  const barcodeInput = document.getElementById("barcode-input");
  const searchBtn = document.getElementById("btn-search");

  if (barcodeInput) {
    barcodeInput.addEventListener("keydown", (e) => {
      if (e.key === "Enter") {
        e.preventDefault();
        executeProductSearch();
      }
    });
  }

  if (searchBtn) {
    searchBtn.addEventListener("click", () => {
      executeProductSearch();
    });
  }

  // Monto Manual
  const inputMonto = document.getElementById("inputMontoManual");
  if (inputMonto) {
    inputMonto.addEventListener("keydown", (e) => {
      if (e.key === "Enter") {
        e.preventDefault();
        agregarMontoManual();
      }
    });
  }

  // Controles de pago
  const methodElem = document.getElementById("payment-method");
  if (methodElem) methodElem.addEventListener("change", renderCart);

  const emptyBtn = document.getElementById("btn-empty");
  if (emptyBtn) {
    emptyBtn.addEventListener("click", () => {
      cart = [];
      renderCart();
    });
  }

  // Listener para el Checkbox de Recarga / Comisión
  const checkRecargaElem = document.getElementById("checkRecarga");
  if (checkRecargaElem) {
    checkRecargaElem.addEventListener("change", renderCart);
  }

  const receivedElem = document.getElementById("received-amount");
  if (receivedElem) {
    receivedElem.addEventListener("input", () => {
      const exactCashCheckbox = document.getElementById("exact-cash");
      if (exactCashCheckbox) exactCashCheckbox.checked = false;
      updatePaymentSummary();
    });
  }

  const exactElem = document.getElementById("exact-cash");
  if (exactElem) exactElem.addEventListener("change", updatePaymentSummary);

  // DataTables si aplica
  if (window.jQuery && $.fn.DataTable && $("#tablaVentas").length) {
    $("#tablaVentas").DataTable({
      searching: false,
      paging: true,
      info: true,
    });
  }

  // Render inicial
  renderCart();
});
