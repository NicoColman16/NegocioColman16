document.addEventListener("DOMContentLoaded", () => {
    const btnToggle = document.getElementById("btn-menu-toggle");
    const menuNav = document.getElementById("topbar-menu");

    if (btnToggle && menuNav) {
        btnToggle.addEventListener("click", () => {
            // Alterna la clase 'activo' para mostrar u ocultar el menú en celulares
            menuNav.classList.toggle("activo");
        });
    }
    const btnSidebarToggle = document.getElementById("btn-sidebar-toggle"); // El de la barra superior
    const btnSidebarClose = document.getElementById("btn-sidebar-close");   // La X dentro del sidebar
    const sidebar = document.querySelector(".sidebar");

    // Abrir sidebar
    if (btnSidebarToggle && sidebar) {
        btnSidebarToggle.addEventListener("click", function() {
            sidebar.classList.add("activo"); // Usamos .add para forzar apertura
        });
    }

    // Cerrar sidebar con la X
    if (btnSidebarClose && sidebar) {
        btnSidebarClose.addEventListener("click", function() {
            sidebar.classList.remove("activo"); // Remueve la clase y lo esconde
        });
    }

    // Opcional: Cerrar el sidebar si el usuario hace clic en cualquier enlace de la lista (en celular)
    const sidebarLinks = document.querySelectorAll(".sidebar-menu a");
    sidebarLinks.forEach(link => {
        link.addEventListener("click", function() {
            if (window.innerWidth <= 768) {
                sidebar.classList.remove("activo");
            }
        });
    });
});