# StoicLock — Reglas del proyecto

App Android **100 % gratuita y Offline-First** que muestra frases estoicas en la pantalla de
bloqueo y permite al usuario crear, editar y gestionar sus propias frases.
No hay red, ni analíticas, ni anuncios: todo vive en el dispositivo.

## Stack obligatorio
- **UI:** Kotlin + Jetpack Compose (Material 3). Estética minimalista y sobria, modo oscuro nativo.
- **Arquitectura:** Clean Architecture ligera con los paquetes `data`, `domain`, `ui`, `worker`
  (+ `lockscreen` para el motor de bloqueo y `widget` para Glance, `di` para módulos Hilt).
- **MVVM + UDF:** cada pantalla expone **un único `UiState` inmutable** vía `StateFlow`.
  Los eventos de usuario entran como llamadas a funciones del ViewModel; la UI nunca muta estado.
- **DI:** Hilt (`@HiltAndroidApp`, `@AndroidEntryPoint`, `@HiltViewModel`, `@HiltWorker`).
  WorkManager se inicializa con `HiltWorkerFactory` (el inicializador por defecto está desactivado en el manifest).
- **Persistencia:**
  - Room → frases (`QuoteEntity`), predeterminadas y del usuario.
  - DataStore Preferences → configuración de bloqueo (frecuencia, filtro, mecanismos, estilo).
- **Motor de bloqueo:**
  - `WorkManager` (`CoroutineWorker`) periódico: 1 h, 6 h, 12 h o 24 h.
  - Modo 1: `WallpaperManager.setBitmap(bitmap, null, true, FLAG_LOCK)` con un bitmap generado con `Canvas` + `StaticLayout`.
  - Modo 2: notificación `VISIBILITY_PUBLIC` + `BigTextStyle`, canal de importancia baja, acción "Otra frase" vía `BroadcastReceiver`.
  - Modo 3: widget `GlanceAppWidget` que cambia de frase al tocarlo.

## Reglas de dependencias y compilación
- **Todas** las dependencias y plugins se declaran en `gradle/libs.versions.toml`. Nunca versiones inline.
- Al terminar cada cambio relevante: `./gradlew assembleDebug` (y `./gradlew testDebugUnitTest`) debe pasar.
- `minSdk 26`, `compileSdk/targetSdk 35`, JVM 17, Kotlin 2.0 + KSP (no kapt).
- Room exporta su esquema en `app/schemas/` (versionado en git).

## Convenciones de código
- Textos visibles en español. Nombres de código en inglés.
- El dominio (`domain/`) no depende de Android ni de Room: modelos puros + interfaces de repositorio.
- Mappers entidad ↔ modelo en `data/`.
- Lógica pura (selección de la siguiente frase, validación del formulario) testeada con JUnit en `src/test`.
- Sin `!!`. Coroutines con `Dispatchers.IO` sólo en `data`/`lockscreen`; los ViewModels usan `viewModelScope`.

## Entorno de compilación (sesiones en la nube)
- SDK Android en `/opt/android-sdk` (`local.properties` no se versiona).
- Maven Central puede responder `429 Too Many Requests` a través del proxy: reintentar
  `./gradlew assembleDebug --max-workers=2` hasta que resuelva (las dependencias quedan en caché).
