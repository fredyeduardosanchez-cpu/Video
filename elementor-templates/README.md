# Plantillas Elementor Pro

Este directorio contiene las plantillas listas para importar en **Elementor →
Plantillas → Guardadas → Importar Plantillas**:

- `landing-page.json` — Contenido completo de la Landing Page (Hero, Beneficios,
  Sobre la Experta, Formulario, Prueba Social, CTA final).
- `pagina-gracias.json` — Contenido completo de la Página de Gracias (mensaje
  de confirmación + invitación a seguir en redes + CTA adicional).

Ambas son plantillas de **tipo "Página"**, 100% construidas con secciones,
columnas y widgets nativos de Elementor / Elementor Pro (Heading, Text Editor,
Button, Icon Box, Icon List, Counter, Image, Testimonial, Form). **No contienen
código, HTML personalizado ni shortcodes** — todo el contenido, colores y
textos son editables visualmente desde Elementor.

## Requisitos previos

1. WordPress instalado (5.8+).
2. Plugin **Elementor** (gratuito) + **Elementor Pro** (necesario para el
   widget "Formulario").
3. Un tema ligero compatible con Elementor, por ejemplo **Hello Elementor**
   o **Astra** (recomendado, ambos gratuitos y optimizados para velocidad).
4. Fuentes de Google **Playfair Display** e **Inter** (Elementor las detecta
   automáticamente al escribir el nombre en Typography → Google Fonts).

## Pasos de importación

1. En el escritorio de WordPress ve a **Plantillas → Guardadas**.
2. Click en **Importar plantillas** y sube `landing-page.json`.
3. Repite el proceso con `pagina-gracias.json`.
4. Abre cada plantilla desde **Plantillas → Guardadas → Editar con Elementor**.
5. Ajusta textos entre corchetes `[ ]` (nombre de la experta, recurso,
   estadísticas, testimonios, redes sociales, etc.) y reemplaza las imágenes
   de marcador de posición (`placehold.co`) por las fotos/artes reales.
6. Para publicarlas como páginas del sitio:
   - Crea una página nueva llamada **"Inicio"** (o el slug que prefieras) →
     Edítala con Elementor → en la barra inferior usa el ícono de carpeta
     ("Insertar plantilla") → pestaña **Mis plantillas** → inserta
     `Landing Page - Recurso Gratuito`.
   - Crea una página **"Gracias"** con slug `/gracias/` → inserta la plantilla
     `Página de Gracias` de la misma forma.
   - Define la página "Inicio" como **página estática de inicio** en
     Ajustes → Lectura.

## Configurar el formulario (captura de leads)

El widget **Formulario** dentro de `landing-page.json` ya incluye los 4 campos
solicitados (Nombre, Correo, WhatsApp opcional, Checkbox de aceptación) y el
botón "Quiero mi Recurso Gratuito". Debes configurar la pestaña **Acciones
después de enviar** en el panel del widget:

1. **Email** — ya viene activado. Cambia `email_to` por el correo real donde
   quieras recibir la notificación del lead, y personaliza el asunto/cuerpo.
2. **Redirigir** — ya apunta a `/gracias/`. Verifica que coincida con el slug
   real de tu página de gracias.
3. **Webhook** — reemplaza la URL de ejemplo por tu endpoint real. Esto es lo
   que te permite conectar con:
   - **Zapier**: crea un "Zap" con disparador *Webhooks by Zapian → Catch Hook*,
     copia la URL generada y pégala en el campo Webhook del formulario.
   - **Mailchimp / ActiveCampaign / Brevo**: usa el integrador nativo de
     Elementor Pro (**Acciones después de enviar → Mailchimp / ActiveCampaign /
     Brevo (Sendinblue)**) e ingresa tu API Key y la lista/audiencia destino —
     no necesitas el Webhook en ese caso, puedes tener varias acciones activas
     a la vez.
   - **FluentCRM**: si FluentCRM está instalado en el mismo WordPress, aparece
     como una acción adicional nativa ("FluentCRM") — selecciona la lista y
     las etiquetas a asignar al lead.
4. Para **enviar automáticamente el ebook/recurso** al correo del lead, dos
   opciones:
   - Configura la automatización de bienvenida en tu CRM (Mailchimp,
     ActiveCampaign, Brevo o FluentCRM) para que dispare un correo con el link
     de descarga apenas el contacto se agrega a la lista (ver
     `docs/estructura-emails-automaticos.md`).
   - O añade una segunda acción "Email" en el propio formulario de Elementor,
     dirigida al correo del lead (`[field id="email"]`), con el enlace de
     descarga en el cuerpo del mensaje.

## Personalización rápida de marca

Ve a **Elementor → Configuración del sitio → Colores globales** y **Fuentes
globales** para cargar la paleta y tipografías descritas en
`docs/guia-colores-tipografia.md`. Así, cualquier ajuste de color/fuente se
propaga a ambas plantillas automáticamente.

## Header y Footer

Estas plantillas cubren el **contenido** de cada página. Para el encabezado
(logo + botón CTA) y pie de página (enlaces legales), usa
**Elementor → Theme Builder → Añadir nuevo → Header / Footer**, replicando los
mismos colores y fuentes. Referencia visual en `landing-page/index.html`
(secciones `<header class="site-header">` y `<footer class="site-footer">`).
