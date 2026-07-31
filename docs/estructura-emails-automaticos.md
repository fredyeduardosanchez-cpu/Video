# Estructura de Correos Automáticos (Email Sequence)

Secuencia recomendada para configurar en Mailchimp, ActiveCampaign, FluentCRM
o Brevo, conectada al formulario de la Landing Page mediante Elementor Pro
(acción nativa del CRM) o vía Webhook/Zapier.

## Disparador (Trigger)

`Nuevo contacto agregado a la lista/audiencia` (o etiqueta `lead-recurso-gratuito`
asignada al enviarse el formulario).

## Automatización

| # | Envío | Momento | Objetivo |
|---|---|---|---|
| 1 | **Email de entrega** | Inmediato (0 min) | Entregar el recurso descargable y confirmar el registro |
| 2 | **Email de bienvenida / valor** | +1 día | Reforzar autoridad, generar cercanía, invitar a redes |
| 3 | **Email de aplicación práctica** | +3 días | Dar un tip aplicable del recurso, aumentar engagement |
| 4 | **Email de prueba social** | +5 días | Mostrar testimonios/resultados, sembrar siguiente oferta |
| 5 | **Email de oferta / siguiente paso** | +7 días | Invitar a programa, servicio o próxima masterclass (venta) |

---

### 1. Email de entrega inmediata

**Asunto:** Aquí está tu [Nombre del Recurso] 🎁
**Preheader:** Descárgalo ahora y empieza a aplicarlo hoy mismo.

```
Hola [Nombre],

¡Gracias por registrarte! Tal como prometí, aquí tienes tu acceso a:

📥 [Nombre del Recurso Gratuito]
[BOTÓN: Descargar Ahora → enlace directo al PDF/Drive/Área de miembros]

Dentro encontrarás [beneficio principal 1], [beneficio principal 2] y
[beneficio principal 3].

Antes de irte, sígueme en redes para más contenido gratuito cada semana:
[Instagram] [Facebook] [TikTok] [LinkedIn] [YouTube]

Un abrazo,
[Nombre de la Experta]
```

### 2. Email de bienvenida / valor (+1 día)

**Asunto:** ¿Ya revisaste tu [Nombre del Recurso]?
Contenido: mini historia de la experta + credibilidad + 1 consejo rápido +
recordatorio de seguir en redes.

### 3. Email de aplicación práctica (+3 días)

**Asunto:** El error #1 que veo al aplicar [tema del recurso]
Contenido: un tip accionable relacionado al recurso, con ejemplo concreto y
llamado a responder el correo (aumenta entregabilidad e interacción).

### 4. Email de prueba social (+5 días)

**Asunto:** Así le fue a [Nombre Cliente] aplicando esto
Contenido: 1-2 testimonios/resultados + estadística ("+[X] personas ya...")
+ CTA suave hacia redes o hacia la oferta.

### 5. Email de oferta / siguiente paso (+7 días)

**Asunto:** ¿Quieres ir más lejos con [tema]?
Contenido: presentación breve del programa/servicio/masterclass, beneficios,
y botón CTA hacia la página de venta o agenda de llamada.

---

## Configuración por plataforma

### Mailchimp
`Automatizaciones → Journeys → Sign up form submitted` → agrega los 5 emails
con los delays indicados. Etiqueta el contacto como `lead-recurso-gratuito`.

### ActiveCampaign
`Automatizaciones → Nueva automatización → Trigger: Suscrito a lista` →
agrega los `Send Email` con los `Wait` correspondientes. Aplica el Tag
`lead-recurso-gratuito` para segmentar campañas futuras de venta.

### FluentCRM (dentro del mismo WordPress)
`FluentCRM → Automations → Nueva → Trigger: Formulario de Elementor
enviado / Contacto agregado a lista` → construye la secuencia con los pasos
`Send Email` y `Wait X Days`. Ventaja: no requiere plugin externo ni costo
mensual de CRM externo.

### Brevo (ex Sendinblue)
`Automatización → Crear un escenario → Entra en una lista` → mismos 5 pasos
de email con retrasos. Brevo permite además enviar el recurso por WhatsApp
si se activa el canal (opcional, usando el dato de teléfono capturado).

### Zapier / Webhook (para conectar cualquier herramienta no nativa)
`Trigger: Webhook por Elementor Pro Form → Catch Hook` → `Action: Add/Update
contact` en la app de destino (Google Sheets, Notion, otro CRM, WhatsApp
Business API, etc.). Útil como respaldo o para centralizar leads en más de
una herramienta a la vez.

## Buenas prácticas de entregabilidad

- Verifica el dominio de envío (SPF, DKIM, DMARC) en el proveedor de correo
  y en Hostinger/DNS.
- Envía el primer correo (entrega del recurso) de forma **inmediata**: es el
  momento de mayor intención de apertura.
- Incluye siempre un enlace de baja (unsubscribe) — requisito legal y de
  buenas prácticas anti-spam.
- Usa el nombre de la experta como remitente (no un "no-reply@"), mejora la
  tasa de apertura.
