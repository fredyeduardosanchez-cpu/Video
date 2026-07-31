# Checklist de Optimización — SEO y Conversión (CRO)

## Velocidad de carga

- [ ] Usar tema ligero (Hello Elementor / Astra), no un tema pesado multipropósito.
- [ ] Activar caché: LiteSpeed Cache (Hostinger) o WP Rocket.
- [ ] Comprimir y servir imágenes en **WebP** (plugin "Imagify" o "ShortPixel").
- [ ] Activar **lazy loading** de imágenes (nativo en WordPress 5.5+ y Elementor).
- [ ] Minificar CSS/JS (opción del plugin de caché).
- [ ] Usar CDN (Hostinger ofrece Cloudflare integrado gratis en hPanel).
- [ ] Limitar el uso de fuentes a 2 familias (ya definido: Playfair Display + Inter).
- [ ] Evitar plugins innecesarios; cada uno agrega peso.
- [ ] Meta objetivo: **LCP < 2.5s**, **CLS < 0.1**, **INP < 200ms** (Core Web Vitals).

## SEO on-page

- [ ] Un solo `<h1>` por página (el título del Hero).
- [ ] Meta título único y descriptivo (50–60 caracteres) por página.
- [ ] Meta descripción con beneficio + CTA (140–160 caracteres).
- [ ] URL amigable: `/` para la landing, `/gracias/` con `noindex` (no debe
      indexarse ni recibir tráfico orgánico directo).
- [ ] Texto alternativo (`alt`) descriptivo en todas las imágenes.
- [ ] Encabezados jerárquicos correctos (H1 → H2 → H3, sin saltos).
- [ ] Datos estructurados básicos (Organization / Person) vía Rank Math si se
      desea reforzar autoridad de la experta en buscadores.
- [ ] Enlaza la landing desde el menú principal o desde contenido relevante
      del blog para ganar autoridad interna.
- [ ] Configura Google Search Console y envía el sitemap.

## Conversión (CRO)

- [ ] **Una sola CTA principal** repetida (Hero, tras beneficios, CTA final):
      "Descargar Gratis" / "Quiero Acceder" — evitar distraer con múltiples
      acciones.
- [ ] Formulario corto: solo Nombre + Email (+ WhatsApp opcional). Cada campo
      extra reduce la tasa de conversión.
- [ ] Copy centrado en el **beneficio**, no en la característica ("Aprende a
      lograr X" en vez de "Ebook de 30 páginas").
- [ ] Prueba social visible **antes** del formulario si el tráfico es frío
      (considerar mover testimonios más arriba en variantes A/B).
- [ ] Microcopy de confianza junto al botón ("Sin spam", "Tus datos están
      seguros").
- [ ] Página de Gracias reutilizada como paso de conversión secundaria (seguir
      en redes) — no dejar una página "muerta" de solo agradecimiento.
- [ ] Agregar pixel de conversión (Meta Pixel / Google Ads / GA4 evento
      `generate_lead`) en la página de gracias para medir y optimizar pauta.
- [ ] Probar A/B el texto del botón CTA ("Descargar Gratis" vs "Quiero
      Acceder") y el color si se cambia la paleta.
- [ ] Revisar el formulario en móvil: campos grandes, teclado numérico en
      WhatsApp (`type="tel"` ya configurado).
- [ ] Tiempo de respuesta del lead: el email de entrega debe llegar en
      segundos (probar el flujo completo antes de lanzar campañas pagadas).

## Analítica y seguimiento

- [ ] Instalar Google Analytics 4 (o Site Kit by Google).
- [ ] Configurar evento de conversión al enviar el formulario (Elementor Pro
      permite añadir un webhook a GA4 Measurement Protocol, o usar
      Google Tag Manager escuchando el evento `elementor-pro/forms/new_record`
      vía dataLayer).
- [ ] Medir: tasa de conversión de la landing (visitas → leads), tasa de
      apertura del email de entrega, y tasa de clic hacia redes sociales desde
      la página de gracias.
