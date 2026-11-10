package com.stoiclock.lockscreen

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.util.Log
import android.view.WindowManager
import com.stoiclock.domain.model.LockScreenSettings
import com.stoiclock.domain.model.Quote
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/** Modo 1: genera el bitmap de la frase y lo aplica SOLO a la pantalla de bloqueo (FLAG_LOCK). */
@Singleton
class LockScreenWallpaperGenerator @Inject constructor(
    @ApplicationContext private val context: Context,
    private val renderer: QuoteImageRenderer,
) {
    /** @return true si se aplicó correctamente. */
    suspend fun apply(quote: Quote, settings: LockScreenSettings): Boolean = withContext(Dispatchers.Default) {
        val wallpaperManager = WallpaperManager.getInstance(context)
        if (!wallpaperManager.isWallpaperSupported || !wallpaperManager.isSetWallpaperAllowed) {
            Log.w(TAG, "El dispositivo no permite cambiar el fondo de pantalla")
            return@withContext false
        }
        val size = screenSize()
        val bitmap = renderer.render(
            quote = quote,
            style = settings.backgroundStyle,
            fontSize = settings.fontSize,
            width = size.x,
            height = size.y,
            verticalBias = 0.6f,
        )
        try {
            withContext(Dispatchers.IO) {
                wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
            }
            true
        } catch (e: Exception) {
            Log.e(TAG, "No se pudo aplicar el fondo de bloqueo", e)
            false
        } finally {
            bitmap.recycle()
        }
    }

    private fun screenSize(): Point {
        val wm = context.getSystemService(WindowManager::class.java)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val bounds = wm.maximumWindowMetrics.bounds
            Point(bounds.width(), bounds.height())
        } else {
            Point().also {
                @Suppress("DEPRECATION")
                wm.defaultDisplay.getRealSize(it)
            }
        }.let { p ->
            // Siempre en vertical (la pantalla de bloqueo de un teléfono lo es).
            if (p.x > p.y) Point(p.y, p.x) else p
        }
    }

    private companion object {
        const val TAG = "LockWallpaper"
    }
}
