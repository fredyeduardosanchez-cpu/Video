# StoicLock — Roadmap

| Fase | Contenido | Estado |
|---|---|---|
| 1 | `CLAUDE.md`, `ROADMAP.md`, catálogo de versiones, Hilt, Room, DataStore, WorkManager, Glance. `assembleDebug` ✅ | ✅ |
| 2 | Capa `domain` + `data`: `QuoteEntity`, DAO, base de datos, semilla de 30 frases (Marco Aurelio, Séneca, Epicteto), preferencias en DataStore | ✅ |
| 3 | Motor de bloqueo: `LockScreenWallpaperGenerator`, `QuoteNotificationManager` + receiver "Otra frase", `QuoteRotationWorker` (`@HiltWorker`), `StoicGlanceWidget` | ✅ |
| 4 | ViewModels + pantallas Compose (Hoy, Biblioteca, Configuración) + formulario crear/editar, navegación inferior en `MainActivity` | ⏳ |
| 5 | `testDebugUnitTest` + `assembleDebug` limpios, APK listo para instalar | ⏳ |

## Ideas futuras (fuera de alcance v1)
- Importar/exportar frases (JSON) para copias de seguridad locales.
- Más estilos de fondo (texturas, tipografías).
- Frase del día en tile de Ajustes rápidos.
