# Guía de Instalación y Migración en Hostinger

Pasos para poner en producción la Landing Page en un hosting de Hostinger
(plan Premium/Business de WordPress o Hosting Web con WordPress).

## 1. Crear el sitio WordPress en Hostinger

1. Entra a **hPanel → Sitios web → Crear sitio web → WordPress**.
2. Define usuario/contraseña de administrador y el dominio (propio o
   subdominio temporal `tusitio.hostingersite.com`).
3. Espera a que Hostinger finalice la instalación automática de WordPress.

## 2. Instalar plugins necesarios

Desde `wp-admin → Plugins → Añadir nuevo`, instala y activa:

1. **Elementor** (gratuito).
2. **Elementor Pro** (sube el `.zip` con tu licencia desde
   `Plugins → Añadir nuevo → Subir plugin`).
3. Tema **Hello Elementor** o **Astra** (activarlo en `Apariencia → Temas`).
4. (Opcional) **FluentCRM**, si prefieres gestionar leads/automatizaciones sin
   pagar un CRM externo.
5. Plugin de caché/velocidad: **LiteSpeed Cache** (viene preinstalado en la
   mayoría de planes Hostinger con LiteSpeed) o **WP Rocket**.
6. Plugin de SEO: **Rank Math** o **Yoast SEO**.

## 3. Importar las plantillas

Sigue `elementor-templates/README.md`:
1. `Plantillas → Guardadas → Importar plantillas` → sube `landing-page.json`
   y `pagina-gracias.json`.
2. Crea las páginas "Inicio" y "Gracias" e inserta cada plantilla.
3. Define "Inicio" como página estática de portada en
   `Ajustes → Lectura → La página de inicio muestra: Una página estática`.
4. Asegura que la página "Gracias" tenga el slug `/gracias/` (o actualiza el
   campo "Redirigir a" del formulario si usas otro slug).

## 4. Configurar dominio y SSL

1. Si el dominio se compró en Hostinger, se conecta automáticamente.
2. Si el dominio está en otro proveedor, apunta los **nameservers** de
   Hostinger (`hPanel → Dominios → Usar los DNS de Hostinger`) o crea un
   registro `A` apuntando a la IP del hosting.
3. Activa el **certificado SSL gratuito** en `hPanel → SSL` (Let's Encrypt) y
   fuerza HTTPS en `Ajustes → General` de WordPress (URL con `https://`).

## 5. Migración de un sitio existente (si ya tenías la landing en otro lugar)

1. Instala el plugin **All-in-One WP Migration** o usa el propio migrador de
   Hostinger (`hPanel → Sitios web → Migrar sitio web`).
2. Exporta el sitio de origen y súbelo desde el importador.
3. Actualiza los enlaces del dominio antiguo al nuevo (`Ajustes → General` y,
   si aplica, un plugin de búsqueda/reemplazo de URLs en base de datos).

## 6. Conexión con CRM / Automatización

Configura las integraciones descritas en `elementor-templates/README.md` y
`docs/estructura-emails-automaticos.md` (Mailchimp, ActiveCampaign, Brevo,
FluentCRM, Zapier/Webhook) directamente desde el panel del widget
**Formulario** de Elementor Pro.

## 7. Checklist final antes de publicar

- [ ] SSL activo y sitio cargando en `https://`.
- [ ] Formulario probado end-to-end (llega el lead al CRM + email + redirección).
- [ ] Página de gracias accesible y con los enlaces reales de redes sociales.
- [ ] Imágenes de marcador de posición reemplazadas por fotos/artes reales.
- [ ] Textos entre corchetes `[ ]` reemplazados por el contenido real.
- [ ] Caché (LiteSpeed/WP Rocket) activada.
- [ ] Sitemap XML generado (Rank Math/Yoast) y enviado a Google Search Console.
- [ ] Meta título/descripción revisados en ambas páginas.
- [ ] Prueba de velocidad en PageSpeed Insights / GTmetrix (ver
      `docs/checklist-seo-conversion.md`).
- [ ] Prueba en móvil, tablet y escritorio real (no solo el editor).
