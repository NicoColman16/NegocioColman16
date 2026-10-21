document.addEventListener("DOMContentLoaded", () => {
  // ==========================================
  // 1. MANEJO DEL LECTOR DE CÓDIGO DE BARRAS
  // ==========================================
  const inputCodigo = document.getElementById("codigoBarra");
  const formCargar = document.getElementById("formCargarProducto");

  if (inputCodigo) {
    // Mantener siempre el foco listo para escanear
    inputCodigo.focus();

    inputCodigo.addEventListener("keydown", (e) => {
      // Los lectores disparan 'Enter' al terminar la lectura
      if (e.key === "Enter") {
        e.preventDefault(); // Evitar envío prematuro del formulario

        const codigo = inputCodigo.value.trim();
        if (codigo.length > 0) {
          inputCodigo.classList.add("barcode-scanned");
          setTimeout(
            () => inputCodigo.classList.remove("barcode-scanned"),
            400,
          );

          // Pasar al siguiente campo (Nombre) automáticamente
          const nextInput = document.getElementById("nombre");
          if (nextInput) nextInput.focus();
        }
      }
    });

    // Botón para generar código de barra interno si el producto no trae
    const btnRandom = document.getElementById("btnGenerarRandom");
    if (btnRandom) {
      btnRandom.addEventListener("click", () => {
        const randomCode =
          "INT-" + Math.floor(100000000000 + Math.random() * 900000000000);
        inputCodigo.value = randomCode;
        document.getElementById("nombre")?.focus();
      });
    }
  }

  // ==========================================
  // 2. MODIFICACIÓN DE PRECIOS Y STOCK (API)
  // ==========================================
  const tablaBody = document.getElementById("tablaModificarBody");
  const inputBuscar = document.getElementById("inputBuscarUpdate");

  let currentPage = 0;
  const pageSize = 15;
  let currentQuery = "";

  if (tablaBody) {
    cargarProductosEdicion(0);

    if (inputBuscar) {
      let delayTimer;
      inputBuscar.addEventListener("input", (e) => {
        clearTimeout(delayTimer);
        currentQuery = e.target.value.trim();
        delayTimer = setTimeout(() => {
          cargarProductosEdicion(0);
        }, 300); // Debounce de 300ms
      });
    }

    document
      .getElementById("btnLimpiarBusqueda")
      ?.addEventListener("click", () => {
        if (inputBuscar) inputBuscar.value = "";
        currentQuery = "";
        cargarProductosEdicion(0);
      });
  }

  // Carga de productos mediante Fetch API
  async function cargarProductosEdicion(page = 0) {
    currentPage = page;
    try {
      const response = await fetch(
        `/api/productos/listar?query=${encodeURIComponent(currentQuery)}&page=${page}&size=${pageSize}`,
      );
      if (!response.ok) throw new Error("Error al obtener datos");

      const data = await response.json();
      renderTablaEdicion(data.content);
      actualizarPaginacion(data);
    } catch (err) {
      console.error(err);
      tablaBody.innerHTML = `<tr><td colspan="5" class="text-center text-danger">Error al cargar productos</td></tr>`;
    }
  }

  // Dibuja las filas con inputs editables directos
  function renderTablaEdicion(productos) {
    tablaBody.innerHTML = "";

    if (productos.length === 0) {
      tablaBody.innerHTML = `<tr><td colspan="5" class="text-center text-muted py-4">No se encontraron productos</td></tr>`;
      return;
    }

    productos.forEach((p) => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td><span class="badge bg-light text-dark border">${p.codigoBarra || "SIN CÓDIGO"}</span></td>
        <td class="fw-bold">${p.nombre}</td>
        <td>
          <div class="input-group input-group-sm">
            <span class="input-group-text">$</span>
            <input type="number" step="0.01" class="form-control input-grid-edit" id="precio-${p.id}" value="${p.precioVenta}">
          </div>
        </td>
        <td>
          <input type="number" class="form-control form-control-sm input-grid-edit" id="stock-${p.id}" value="${p.stockActual}">
        </td>
        <td class="text-end">
          <button class="btn btn-sm btn-success fw-bold" onclick="guardarCambiosProducto(${p.id})">
            <i class="fa-solid fa-check me-1"></i>Guardar
          </button>
        </td>
      `;
      tablaBody.appendChild(tr);
    });
  }

  // Paginación
  function actualizarPaginacion(data) {
    const badge = document.getElementById("totalProductosBadge");
    if (badge) badge.textContent = `${data.totalElements} productos`;

    const info = document.getElementById("infoPaginacion");
    if (info)
      info.textContent = `Página ${data.number + 1} de ${data.totalPages || 1}`;

    const controls = document.getElementById("paginationControls");
    if (!controls) return;

    controls.innerHTML = "";

    // Botón Anterior
    const prevLi = document.createElement("li");
    prevLi.className = `page-item ${data.first ? "disabled" : ""}`;
    prevLi.innerHTML = `<a class="page-link" href="#">Anterior</a>`;
    prevLi.addEventListener("click", (e) => {
      e.preventDefault();
      if (!data.first) cargarProductosEdicion(currentPage - 1);
    });
    controls.appendChild(prevLi);

    // Botón Siguiente
    const nextLi = document.createElement("li");
    nextLi.className = `page-item ${data.last ? "disabled" : ""}`;
    nextLi.innerHTML = `<a class="page-link" href="#">Siguiente</a>`;
    nextLi.addEventListener("click", (e) => {
      e.preventDefault();
      if (!data.last) cargarProductosEdicion(currentPage + 1);
    });
    controls.appendChild(nextLi);
  }
});

// Función global ejecutada por el botón Guardar de la tabla
async function guardarCambiosProducto(id) {
  const nuevoPrecio = document.getElementById(`precio-${id}`).value;
  const nuevoStock = document.getElementById(`stock-${id}`).value;

  try {
    const response = await fetch(`/api/productos/actualizar-quick/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        precioVenta: parseFloat(nuevoPrecio),
        stockActual: parseInt(nuevoStock),
      }),
    });

    if (response.ok) {
      const btn = event.currentTarget;
      const originalText = btn.innerHTML;
      btn.innerHTML = `<i class="fa-solid fa-circle-check me-1"></i>¡OK!`;
      btn.classList.replace("btn-success", "btn-outline-success");

      setTimeout(() => {
        btn.innerHTML = originalText;
        btn.classList.replace("btn-outline-success", "btn-success");
      }, 1500);
    } else {
      alert("No se pudieron guardar los cambios.");
    }
  } catch (err) {
    console.error(err);
    alert("Error de conexión al intentar actualizar.");
  }
}

document.addEventListener("DOMContentLoaded", function () {
  const inputNombre = document.getElementById("nombre");
  const inputCodigo = document.getElementById("codigoBarras");

  if (inputNombre && inputCodigo) {
    inputNombre.addEventListener("keydown", function (event) {
      if (event.key === "Enter") {
        event.preventDefault(); // Evitamos que envíe el formulario
        inputCodigo.focus(); // Saltamos al campo Código de Barras
        inputCodigo.select(); // Opcional: selecciona el texto si hubiere algo
      }
    });
  }
});
