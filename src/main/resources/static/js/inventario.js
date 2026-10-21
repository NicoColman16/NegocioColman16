// Estado global del inventario y carrito
let currentPage = 0;
let totalPages = 1;
let cart = [];
let currentFinalTotal = 0;

// Función para redondear al múltiplo de 10 superior
function roundToNextTen(amount) {
  return amount > 0 ? Math.ceil(amount / 10) * 10 : 0;
}

// 1. Cargar productos desde Spring Boot con paginación y filtro
async function loadProducts(page = 0) {
  const barcodeInput = document.getElementById("barcode-input");
  const query = barcodeInput ? barcodeInput.value.trim() : "";

  const pageSizeSelect = document.getElementById("select-page-size");
  const size = pageSizeSelect ? pageSizeSelect.value : 15;

  try {
    const response = await fetch(
      `/api/productos/listar?query=${encodeURIComponent(query)}&page=${page}&size=${size}`,
    );

    if (response.ok) {
      const data = await response.json(); // Retorna la Page<Producto> de Spring Data
      currentPage = data.number;
      totalPages = data.totalPages;

      // Si la búsqueda devuelve resultados pero es una búsqueda exacta de código de barras
      renderProductsTable(data.content, data.totalElements);
      updatePaginationControls(data.totalElements);
    } else {
      console.error("Error al obtener productos:", response.status);
    }
  } catch (error) {
    console.error("Error de comunicación con el servidor:", error);
  }
}

// 2. Renderizar filas en la tabla del inventario
function renderProductsTable(productos, totalElements) {
  const tbody = document.getElementById("cart-table-body");
  if (!tbody) return;

  tbody.innerHTML = "";

  if (!productos || productos.length === 0) {
    tbody.innerHTML = `<tr><td colspan="6" style="text-align:center;">No se encontraron productos.</td></tr>`;
    return;
  }

productos.forEach((producto) => {
  const tr = document.createElement("tr");
  tr.innerHTML = `
    <td>${producto.codigoBarras || "N/A"}</td>
    <td>${producto.nombre}</td>
    <td>${producto.stockActual ?? 0} Und(s)</td>
    <td>$ ${parseFloat(producto.precioVenta || 0).toFixed(2)}</td>
    <td>${producto.tiposProducto ? producto.tiposProducto.join(', ') : "General"}</td>
    <td>
      <div class="action-icons">
        <a href="/productos/editar/${producto.id}#precioVenta" class="btn btn-sm btn-warning">
          <i class="fa-solid fa-pen-to-square"></i> Modificar Precio
        </a>
      </div>
    </td>
  `;
  tbody.appendChild(tr);
});
}

// 3. Actualizar los botones y textos de la paginación
function updatePaginationControls(totalElements) {
  const itemsCount = document.getElementById("items-count");
  const itemsTotalCount = document.getElementById("items-total-count");
  const btnPrev = document.getElementById("btn-prev-page");
  const btnNext = document.getElementById("btn-next-page");
  const pageDisplay = document.getElementById("current-page-display");

  if (itemsCount)
    itemsCount.innerText = totalElements > 0 ? currentPage * 15 + 1 : 0;
  if (itemsTotalCount) itemsTotalCount.innerText = totalElements;
  if (pageDisplay) pageDisplay.innerText = currentPage + 1;

  if (btnPrev) {
    btnPrev.disabled = currentPage === 0;
  }
  if (btnNext) {
    btnNext.disabled = currentPage + 1 >= totalPages;
  }
}

// 4. Función para añadir al carrito
function addToCartDirect(id, code, name, price) {
  const existingIndex = cart.findIndex((item) => item.id === id);

  if (existingIndex !== -1) {
    cart[existingIndex].qty += 1;
  } else {
    cart.push({
      id: id,
      code: code || "N/A",
      name: name,
      qty: 1,
      unitPrice: parseFloat(price) || 0,
    });
  }

  // Limpiar buscador y enfocar
  const barcodeInput = document.getElementById("barcode-input");
  if (barcodeInput) {
    barcodeInput.value = "";
    barcodeInput.focus();
  }

  loadProducts(0); // Recarga la lista completa
}

// 5. Inicializador único al cargar la página
document.addEventListener("DOMContentLoaded", () => {
  const barcodeInput = document.getElementById("barcode-input");
  const searchBtn = document.getElementById("btn-search");
  const pageSizeSelect = document.getElementById("select-page-size");
  const btnPrev = document.getElementById("btn-prev-page");
  const btnNext = document.getElementById("btn-next-page");

  // Evento Enter en el buscador (Escáner o Teclado)
  if (barcodeInput) {
    barcodeInput.addEventListener("keydown", (e) => {
      if (e.key === "Enter") {
        e.preventDefault();
        loadProducts(0);
      }
    });
    // Búsqueda instantánea opcional al escribir
    barcodeInput.addEventListener("input", () => {
      loadProducts(0);
    });
  }

  // Evento Clic en el botón Buscar
  if (searchBtn) {
    searchBtn.addEventListener("click", () => {
      loadProducts(0);
    });
  }

  // Evento cambio de registros por página
  if (pageSizeSelect) {
    pageSizeSelect.addEventListener("change", () => {
      loadProducts(0);
    });
  }

  // Botones de paginación
  if (btnPrev) {
    btnPrev.addEventListener("click", () => {
      if (currentPage > 0) {
        loadProducts(currentPage - 1);
      }
    });
  }

  if (btnNext) {
    btnNext.addEventListener("click", () => {
      if (currentPage + 1 < totalPages) {
        loadProducts(currentPage + 1);
      }
    });
  }

  // CARGA INICIAL AUTOMÁTICA
  loadProducts(0);
});
