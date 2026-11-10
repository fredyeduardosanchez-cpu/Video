package com.stoiclock.lockscreen

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import com.stoiclock.domain.model.LockScreenSettings
import com.stoiclock.domain.model.Quote
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/** Genera una imagen de la frase (formato 4:5) en caché y devuelve un content:// Uri para compartirla. */
@Singleton
class QuoteShareHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val renderer: QuoteImageRenderer,
) {
    suspend fun createShareImage(quote: Quote, settings: LockScreenSettings): Uri = withContext(Dispatchers.IO) {
        val bitmap = renderer.render(quote, settings.backgroundStyle, settings.fontSize, WIDTH, HEIGHT)
        val dir = File(context.cacheDir, "shared").apply { mkdirs() }
        val file = File(dir, "frase_estoica.png")
        file.outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        bitmap.recycle()
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }

    private companion object {
        const val WIDTH = 1080
        const val HEIGHT = 1350
    }
}
