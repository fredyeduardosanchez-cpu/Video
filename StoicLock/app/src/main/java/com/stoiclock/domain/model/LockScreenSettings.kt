package com.stoiclock.domain.model

/** Cada cuánto rota automáticamente la frase. */
enum class RotationFrequency(val hours: Long, val label: String) {
    HOURLY(1, "Cada hora"),
    EVERY_6_HOURS(6, "Cada 6 horas"),
    EVERY_12_HOURS(12, "Cada 12 horas"),
    DAILY(24, "Una vez al día"),
}

/** Qué frases entran en la rotación de la pantalla de bloqueo. */
enum class RotationFilter(val label: String) {
    ALL_ACTIVE("Todas las activas"),
    USER_ONLY("Solo Mis Frases"),
    FAVORITES_ONLY("Solo Favoritas"),
}

/** Paleta del fondo de bloqueo. Colores ARGB; si [endColor] == [startColor] es un color sólido. */
enum class BackgroundStyle(
    val label: String,
    val startColor: Long,
    val endColor: Long,
    val textColor: Long,
    val accentColor: Long,
) {
    OBSIDIAN("Obsidiana", 0xFF121214, 0xFF121214, 0xFFE7E3DC, 0xFFD7B98E),
    NIGHT("Noche", 0xFF0F1626, 0xFF1F2A44, 0xFFE6E9F0, 0xFF9DB4D9),
    BRONZE("Bronce", 0xFF1E1610, 0xFF3D2B1A, 0xFFF1E3CF, 0xFFD7B98E),
    FOREST("Bosque", 0xFF0E1A15, 0xFF1F3329, 0xFFE3EDE6, 0xFFA8C6B2),
    MARBLE("Mármol", 0xFFF5F2EC, 0xFFE4DED3, 0xFF1B1B1D, 0xFF8C6A43),
}

enum class QuoteFontSize(val label: String, val scale: Float) {
    SMALL("Pequeña", 0.85f),
    MEDIUM("Mediana", 1.0f),
    LARGE("Grande", 1.2f),
}

data class LockScreenSettings(
    val wallpaperEnabled: Boolean = false,
    val notificationEnabled: Boolean = false,
    val frequency: RotationFrequency = RotationFrequency.DAILY,
    val filter: RotationFilter = RotationFilter.ALL_ACTIVE,
    val backgroundStyle: BackgroundStyle = BackgroundStyle.OBSIDIAN,
    val fontSize: QuoteFontSize = QuoteFontSize.MEDIUM,
    /** Frase mostrada actualmente en bloqueo / notificación / widget. */
    val currentQuoteId: Long? = null,
) {
    val anyMechanismEnabled: Boolean get() = wallpaperEnabled || notificationEnabled
}
