# Guía de Marca — Colores y Tipografías

Sistema de diseño usado en la Landing Page y la Página de Gracias. Úsalo para configurar
**Elementor → Site Settings → Global Colors / Global Fonts** y mantener consistencia en
todo el sitio.

## Paleta de colores

Estilo: premium, cálido, femenino-profesional, alto contraste para conversión.

| Rol | Nombre | Hex | Uso |
|---|---|---|---|
| Primario (marca) | Ciruela Profundo | `#3B1E54` | Header, fondos oscuros, títulos principales |
| Primario oscuro | Ciruela Noche | `#2A1440` | Hover de fondos oscuros, footer |
| Acento | Oro Suave | `#D4AF37` | Íconos, líneas divisorias, detalles, bordes |
| CTA / Conversión | Coral Cálido | `#E85D4E` | Botones principales, elementos de acción |
| CTA hover | Coral Oscuro | `#C8442F` | Hover/active de botones |
| Fondo base | Crema | `#FFFBF7` | Fondo general de la página |
| Fondo alterno | Blush Suave | `#F7EFEA` | Secciones alternas (beneficios, testimonios) |
| Texto principal | Carbón Ciruela | `#2B2440` | Texto de cuerpo |
| Texto secundario | Gris Malva | `#6B6478` | Subtítulos, texto de apoyo |
| Blanco | Blanco | `#FFFFFF` | Tarjetas, contraste |
| Éxito / confirmación | Verde Salvia | `#4C8B6E` | Página de gracias, checks |

Variables CSS (ya incluidas en `assets/css/style.css`):

```css
:root {
  --color-primary: #3B1E54;
  --color-primary-dark: #2A1440;
  --color-accent: #D4AF37;
  --color-cta: #E85D4E;
  --color-cta-hover: #C8442F;
  --color-bg: #FFFBF7;
  --color-bg-alt: #F7EFEA;
  --color-text: #2B2440;
  --color-text-light: #6B6478;
  --color-white: #FFFFFF;
  --color-success: #4C8B6E;
}
```

## Tipografía

- **Títulos (H1–H3):** `Playfair Display` (serif elegante) — Google Fonts, gratuita.
- **Subtítulos, botones, UI:** `Inter` (sans-serif, moderna, alta legibilidad) — Google Fonts.
- **Cuerpo de texto:** `Inter`, 16–18px, interlineado 1.6.

Configuración en Elementor Pro:
`Site Settings → Global Fonts` → crear:
1. **Primary Heading** = Playfair Display / 600–700
2. **Primary Body** = Inter / 400–500

Escala tipográfica sugerida (desktop / mobile):
- H1: 56px / 34px
- H2: 40px / 28px
- H3: 24px / 20px
- Body: 18px / 16px
- Botones: 18px / 16px, uppercase opcional, letter-spacing 0.5px

## Botones (estilo global)

- Border-radius: `50px` (píldora) para CTA principales, `8px` para secundarios.
- Sombra suave: `0 8px 24px rgba(232,93,78,0.25)`.
- Micro-animación: `transform: translateY(-2px)` + sombra al hover, transición `0.25s ease`.

## Espaciado

- Sección desktop: `100px` padding vertical.
- Sección mobile: `56px` padding vertical.
- Mucho "white space": separación mínima entre bloques de `32px`.

## Iconografía

- Estilo lineal (outline), grosor 1.5–2px, en color acento (oro) o coral según contexto.
- Usar sets compatibles con Elementor: Font Awesome (incluido) o Icons8 Line Awesome.
