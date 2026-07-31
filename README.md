# Landing Page — Captación de Leads + Recurso Gratuito

Paquete completo de una landing page de alta conversión, pensada para captar
datos de potenciales clientes a cambio de un recurso digital gratuito
(ebook, guía, checklist o masterclass) y, después del registro, invitar al
usuario a seguir a la experta en redes sociales.

Todo el contenido usa placeholders entre corchetes (`[Nombre de la Experta]`,
`[Nombre del Recurso Gratuito]`, `[X]`, etc.) listos para reemplazar por la
información real de la marca.

## Contenido del repositorio

```
landing-page/                 → Maqueta HTML/CSS/JS de referencia visual
  index.html                    Landing page principal
  gracias.html                  Página de gracias (entrega + seguimiento en redes)
  assets/css/style.css          Sistema de diseño (colores, tipografías, componentes)
  assets/js/main.js             Animaciones de entrada + demo de envío de formulario

elementor-templates/          → Plantillas 100% importables en Elementor Pro
  landing-page.json             Plantilla de la Landing Page (sin código, solo widgets)
  pagina-gracias.json           Plantilla de la Página de Gracias
  README.md                     Instrucciones de importación y configuración del formulario

docs/
  guia-colores-tipografia.md    Guía de marca: paleta de colores y tipografías
  estructura-emails-automaticos.md  Secuencia de correos automáticos (CRM)
  guia-instalacion-hostinger.md Instalación/migración paso a paso en Hostinger
  checklist-seo-conversion.md   Checklist de velocidad, SEO on-page y CRO
  wireframes/                   Wireframes de baja fidelidad por sección (SVG + visor HTML)
```

## Cómo usar este paquete

1. **Referencia visual rápida**: abre `landing-page/index.html` y
   `landing-page/gracias.html` directamente en el navegador para ver el
   diseño terminado (usa Google Fonts y Font Awesome vía CDN, así que requiere
   conexión a internet para verse igual).
2. **Implementación real en WordPress**: sigue `elementor-templates/README.md`
   para importar las plantillas `.json` en Elementor Pro — ahí se configura
   también el formulario y sus integraciones (Mailchimp, ActiveCampaign,
   FluentCRM, Brevo, Zapier/Webhook).
3. **Automatización de correos**: usa
   `docs/estructura-emails-automaticos.md` para montar la secuencia de 5
   emails (entrega del recurso → nurturing → invitación a seguir en redes →
   oferta) en el CRM que elijas.
4. **Migración a Hostinger**: sigue `docs/guia-instalacion-hostinger.md` paso
   a paso, desde crear el sitio en hPanel hasta el checklist final de
   publicación.
5. **Antes de lanzar tráfico pagado**: repasa
   `docs/checklist-seo-conversion.md` (velocidad, SEO on-page y CRO).
6. **Wireframes**: abre `docs/wireframes/wireframes.html` para ver la
   estructura de cada sección antes del diseño visual final.

## Estructura de la Landing Page

1. **Hero** — título orientado al beneficio, subtítulo, mockup del recurso, CTA.
2. **Beneficios** — 5 beneficios clave de descargar el recurso.
3. **Sobre la experta** — foto, biografía, credenciales, estadísticas.
4. **Formulario de captura** — Nombre, Email, WhatsApp (opcional), checkbox
   de consentimiento, botón de envío.
5. **Prueba social** — estadísticas, testimonios y logos de marcas.
6. **CTA final** — último llamado a la acción antes del footer.

Tras enviar el formulario, el usuario es redirigido a la **Página de
Gracias**, que confirma la entrega del recurso e invita a seguir a la
experta en Instagram, Facebook, TikTok, LinkedIn y YouTube.

## Sistema de diseño

Paleta ciruela + oro + coral sobre fondo crema, tipografía Playfair Display
(títulos) + Inter (cuerpo/UI). Detalle completo en
`docs/guia-colores-tipografia.md` — configúralo en Elementor como Colores y
Fuentes Globales para que se propague a todo el sitio.

## Notas técnicas

- 100% responsive (desktop / tablet / mobile).
- Sin código hardcodeado en las plantillas Elementor: solo secciones,
  columnas y widgets nativos (Heading, Text Editor, Button, Icon Box, Icon
  List, Counter, Image, Testimonial, Formulario).
- Preparado para SEO (metadatos, jerarquía de encabezados, `noindex` en la
  página de gracias) y para velocidad (2 familias tipográficas, imágenes
  livianas, lazy loading nativo).
