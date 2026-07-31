// Landing Page — interacciones ligeras (no se migra a Elementor:
// Elementor Pro gestiona el envío del formulario y las animaciones
// de entrada de forma nativa mediante "Motion Effects").

document.addEventListener('DOMContentLoaded', function () {
  // Año dinámico en el footer
  document.querySelectorAll('#year').forEach(function (el) {
    el.textContent = new Date().getFullYear();
  });

  // Micro-animaciones de entrada al hacer scroll
  var reveals = document.querySelectorAll('.reveal');
  if ('IntersectionObserver' in window) {
    var observer = new IntersectionObserver(function (entries) {
      entries.forEach(function (entry) {
        if (entry.isIntersecting) {
          entry.target.classList.add('is-visible');
          observer.unobserve(entry.target);
        }
      });
    }, { threshold: 0.15 });
    reveals.forEach(function (el) { observer.observe(el); });
  } else {
    reveals.forEach(function (el) { el.classList.add('is-visible'); });
  }

  // Demo de validación del formulario de la maqueta HTML.
  // En WordPress esto lo reemplaza el Widget "Formulario" de Elementor Pro.
  var form = document.getElementById('lead-form');
  if (form) {
    form.addEventListener('submit', function (e) {
      e.preventDefault();
      window.location.href = 'gracias.html';
    });
  }
});
