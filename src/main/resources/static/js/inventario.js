// Estado global del inventario y carrito
let currentPage = 0;
let totalPages = 1;
let cart = [];
let currentFinalTotal = 0;

// Variables para el scroll infinito dinámico
let isLoadingMore = false;
let hasMoreData = true;
const pageSize = 15; // Bloques de carga al hacer scroll

// Función para redondear al múltiplo de 10 superior
function roundToNextTen(amount) {
  return amount > 0 ? Math.ceil(amount / 10) * 10 : 0;
}

// 1. Cargar productos desde Spring Boot con scroll infinito y filtro
async function loadProducts(page = 0, append = false) {
  if (isLoadingMore) return;
  isLoadingMore = true;

  const barcodeInput = document.getElementById("barcode-input");
  const query = barcodeInput ? barcodeInput.value.trim() : "";

  const chkSoloFaltas = document.getElementById("chk-solo-faltas");
  const soloFaltas = chkSoloFaltas ? chkSoloFaltas.checked : false;

  try {
    const response = await fetch(
      `/api/productos/listar?query=${encodeURIComponent(query)}&soloFaltas=${soloFaltas}&page=${page}&size=${pageSize}`,
    );

    if (response.ok) {
      const data = await response.json(); // Retorna la Page<Producto> de Spring Data
      currentPage = data.number;
      totalPages = data.totalPages;

      hasMoreData = currentPage + 1 < totalPages;

      // Renderiza acumulando si es scroll, o reemplaza si es nueva búsqueda/filtro
      renderProductsTable(data.content, data.totalElements, append);
      updateTotalElementsCount(data.totalElements);
    } else {
      console.error("Error al obtener productos:", response.status);
    }
  } catch (error) {
    console.error("Error de comunicación con el servidor:", error);
  } finally {
    isLoadingMore = false;
  }
}

// CONFIGURACIÓN DEL SCROLL INFINITO EN EL CONTENEDOR DE LA TABLA
  const tableContainer = document.querySelector(".table-container");

  if (tableContainer) {
    tableContainer.addEventListener("scroll", () => {
      const scrollTop = tableContainer.scrollTop;
      const scrollHeight = tableContainer.scrollHeight;
      const clientHeight = tableContainer.clientHeight;

      // Si el usuario llega a 50px del final del scroll interno de la tabla
      if (scrollTop + clientHeight >= scrollHeight - 50) {
        if (hasMoreData && !isLoadingMore) {
          currentPage++;
          console.log("Cargando más productos, página:", currentPage);
          loadProducts(currentPage, true); // true = hace append abajo
        }
      }
    });
  } else {
    // Por seguridad, si no encuentra la clase, usa la ventana
    window.addEventListener("scroll", () => {
      const scrollTop = window.scrollY || document.documentElement.scrollTop;
      const scrollHeight = document.documentElement.scrollHeight;
      const clientHeight = window.innerHeight;

      if (scrollTop + clientHeight >= scrollHeight - 100) {
        if (hasMoreData && !isLoadingMore) {
          currentPage++;
          loadProducts(currentPage, true);
        }
      }
    });
  }
// 2. Renderizar filas en la tabla del inventario
function renderProductsTable(productos, totalElements, append = false) {
  const tbody = document.getElementById("cart-table-body");
  if (!tbody) return;

  if (!append) {
    tbody.innerHTML = ""; // Limpia si es una nueva búsqueda o filtro
  }

  if (!productos || productos.length === 0 && !append) {
    tbody.innerHTML = `<tr><td colspan="7" style="text-align:center;">No se encontraron productos.</td></tr>`;
    return;
  }

  productos.forEach((producto) => {
    const tr = document.createElement("tr");

    // Clases dinámicas para el botón según el estado
    const btnClass = producto.falta
      ? "btn btn-sm btn-danger"
      : "btn btn-sm btn-success";

    const btnText = producto.falta ? "Quitar Falta" : "Marcar Falta";

    tr.innerHTML = `
    <td>${producto.codigoBarras || "N/A"}</td>
    <td>${producto.nombre}</td>
    <td>${producto.precioCosto ?? 0} $ </td>
    <td>$ ${parseFloat(producto.precioVenta || 0).toFixed(2)}</td>
    <td>${producto.tiposProducto ? producto.tiposProducto.join(", ") : "General"}</td>
    <td>
      <div class="action-icons">
        <a href="/productos/editar/${producto.id}#precioVenta" class="btn btn-sm btn-warning">
          <i class="fa-solid fa-pen-to-square"></i> Modificar
        </a>
      </div>
    </td>
    <td class="text-center" style="display: flex; align-items: center; gap: 8px; justify-content: center;">
      <button type="button" class="${btnClass}" onclick="toggleFaltaEstado(${producto.id})" title="Cambiar Estado">
        <i class="fa-solid fa-rotate"></i> ${btnText}
      </button>
    </td>
  `;
    tbody.appendChild(tr);
  });
}

// Función asíncrona para cambiar el estado de falta sin recargar la página
async function toggleFaltaEstado(id) {
  try {
    const response = await fetch(`/api/productos/toggle-falta/${id}`, {
      method: "POST",
    });
    if (response.ok) {
      // Recarga la tabla manteniendo los filtros actuales desde la página inicial
      hasMoreData = true;
      loadProducts(0, false);
    } else {
      console.error("Error al cambiar el estado del producto");
    }
  } catch (error) {
    console.error("Error de red:", error);
  }
}

// 3. Actualizar el contador total de elementos en pantalla
function updateTotalElementsCount(totalElements) {
  const itemsCount = document.getElementById("items-count");
  const itemsTotalCount = document.getElementById("items-total-count");

  if (itemsCount) itemsCount.innerText = totalElements > 0 ? totalElements : 0;
  if (itemsTotalCount) itemsTotalCount.innerText = totalElements;
}

// 4. Inicializador único al cargar la página y configurar el Scroll Infinito
document.addEventListener("DOMContentLoaded", () => {
  const chkSoloFaltas = document.getElementById("chk-solo-faltas");
  if (chkSoloFaltas) {
    chkSoloFaltas.addEventListener("change", () => {
      currentPage = 0;
      hasMoreData = true;
      loadProducts(0, false); // Vuelve a la página 0 al alternar el filtro
    });
  }

  const barcodeInput = document.getElementById("barcode-input");
  const searchBtn = document.getElementById("btn-search");

  // Evento Enter en el buscador (Escáner o Teclado)
  if (barcodeInput) {
    barcodeInput.addEventListener("keypress", (e) => {
      if (e.key === "Enter") {
        e.preventDefault();
        currentPage = 0;
        hasMoreData = true;
        loadProducts(0, false);
      }
    });
    // Búsqueda instantánea opcional al escribir
    barcodeInput.addEventListener("input", () => {
      currentPage = 0;
      hasMoreData = true;
      loadProducts(0, false);
    });
  }

  // Evento Clic en el botón Buscar
  if (searchBtn) {
    searchBtn.addEventListener("click", () => {
      currentPage = 0;
      hasMoreData = true;
      loadProducts(0, false);
    });
  }

  // CONFIGURACIÓN DEL SCROLL DINÁMICO INFINITO
  // Detecta el contenedor con scroll de la tabla (puede ser la ventana o el contenedor padre)
  const scrollableContainer = document.querySelector(".table-responsive") || window;

  scrollableContainer.addEventListener("scroll", () => {
    let scrollTop, scrollHeight, clientHeight;

    if (scrollableContainer === window) {
      scrollTop = window.scrollY;
      scrollHeight = document.documentElement.scrollHeight;
      clientHeight = window.innerHeight;
    } else {
      scrollTop = scrollableContainer.scrollTop;
      scrollHeight = scrollableContainer.scrollHeight;
      clientHeight = scrollableContainer.clientHeight;
    }

    // Si el usuario llega a 50px del final del scroll y hay más datos por cargar
    if (scrollTop + clientHeight >= scrollHeight - 50) {
      if (hasMoreData && !isLoadingMore) {
        currentPage++;
        loadProducts(currentPage, true); // true indica que hace un append (agrega abajo)
      }
    }
  });

  // 1. Creamos un elemento centinela invisible al final de la tabla
  const tabla = document.querySelector(".table-container table");
  if (tabla) {
    let sentinel = document.getElementById("scroll-sentinel");
    if (!sentinel) {
      sentinel = document.createElement("div");
      sentinel.id = "scroll-sentinel";
      sentinel.style.height = "20px";
      sentinel.style.width = "100%";
      // Lo insertamos justo después de la tabla
      tabla.parentNode.appendChild(sentinel);
    }

    // 2. Configuramos el observador
    const observer = new IntersectionObserver((entries) => {
      const entry = entries[0];
      // Si el centinela es visible en la pantalla y hay más datos por cargar
      if (entry.isIntersecting && hasMoreData && !isLoadingMore) {
        currentPage++;
        loadProducts(currentPage, true);
      }
    }, {
      root: null,          // Usa la ventana del navegador como referencia
      rootMargin: "150px", // Se dispara 150px antes de que el usuario llegue físicamente al final
      threshold: 0.1
    });

    // 3. Ponemos a vigilar el centinela
    observer.observe(sentinel);
  }

  // CARGA INICIAL AUTOMÁTICA
  currentPage = 0;
  hasMoreData = true;
  loadProducts(0, false);
});