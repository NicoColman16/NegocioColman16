document.addEventListener("DOMContentLoaded", () => {
  const currentPath = window.location.pathname;
  const navLinks = document.querySelectorAll(".sidebar-menu a");

  navLinks.forEach(link => {
    // Obtenemos la ruta relativa del href (ej: "/venta/producto")
    const linkPath = link.getAttribute("href");

    // Comprueba coincidencia exacta o si la URL actual empieza con la ruta del link (para subpáginas)
    if (linkPath && (currentPath === linkPath || (linkPath !== '/' && currentPath.startsWith(linkPath)))) {
      link.parentElement.classList.add("active"); // Agrega 'active' al <li>
    }
  });
});