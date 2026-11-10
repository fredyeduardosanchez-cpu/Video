package com.stoiclock.lockscreen

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.Typeface
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.stoiclock.domain.model.BackgroundStyle
import com.stoiclock.domain.model.Quote
import com.stoiclock.domain.model.QuoteFontSize
import javax.inject.Inject
import kotlin.math.min

/**
 * Dibuja una frase sobre un lienzo minimalista (color sólido o degradado sutil) usando
 * [Canvas] + [StaticLayout]. Se usa tanto para el fondo de bloqueo como para compartir como imagen.
 */
class QuoteImageRenderer @Inject constructor() {

    /**
     * @param verticalBias posición vertical del bloque de texto (0 = arriba, 1 = abajo). En la
     * pantalla de bloqueo se baja un poco para no chocar con el reloj del sistema.
     */
    fun render(
        quote: Quote,
        style: BackgroundStyle,
        fontSize: QuoteFontSize,
        width: Int,
        height: Int,
        verticalBias: Float = 0.5f,
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawBackground(canvas, style, width, height)

        val shortSide = min(width, height).toFloat()
        val contentWidth = (width * 0.78f).toInt()
        val maxTextHeight = height * 0.55f

        val quotePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = style.textColor.toInt()
            typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC)
        }
        // Tamaño base proporcional al lado corto; se reduce hasta que la frase quepa.
        var textSize = shortSide * 0.062f * fontSize.scale
        var quoteLayout: StaticLayout
        do {
            quotePaint.textSize = textSize
            quoteLayout = buildLayout(quote.text, quotePaint, contentWidth, spacingMult = 1.25f)
            textSize *= 0.92f
        } while (quoteLayout.height > maxTextHeight && textSize > shortSide * 0.025f)

        val markPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = style.accentColor.toInt()
            typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
            textSize = shortSide * 0.16f
            textAlign = Paint.Align.CENTER
        }
        val authorPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = style.accentColor.toInt()
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            this.textSize = shortSide * 0.034f * fontSize.scale
            letterSpacing = 0.18f
        }
        val workPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ((style.textColor and 0x00FFFFFFL) or 0xAA000000L).toInt()
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            this.textSize = shortSide * 0.029f * fontSize.scale
        }
        val authorLayout = buildLayout(quote.author.uppercase(), authorPaint, contentWidth)
        val workLayout = quote.work?.takeIf { it.isNotBlank() }?.let { buildLayout(it, workPaint, contentWidth) }

        val markHeight = markPaint.textSize * 0.55f
        val gap = shortSide * 0.045f
        val dividerGap = shortSide * 0.035f
        val blockHeight = markHeight + gap + quoteLayout.height + gap + dividerGap +
            authorLayout.height + (workLayout?.let { it.height + shortSide * 0.01f } ?: 0f)

        var y = (height - blockHeight) * verticalBias
        val centerX = width / 2f
        val left = (width - contentWidth) / 2f

        // Comilla decorativa
        canvas.drawText("“", centerX, y + markPaint.textSize * 0.75f, markPaint)
        y += markHeight + gap

        canvas.save(); canvas.translate(left, y); quoteLayout.draw(canvas); canvas.restore()
        y += quoteLayout.height + gap

        // Línea divisoria fina
        val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = style.accentColor.toInt()
            strokeWidth = shortSide * 0.003f
        }
        val lineHalf = shortSide * 0.05f
        canvas.drawLine(centerX - lineHalf, y, centerX + lineHalf, y, linePaint)
        y += dividerGap

        canvas.save(); canvas.translate(left, y); authorLayout.draw(canvas); canvas.restore()
        y += authorLayout.height + shortSide * 0.01f

        workLayout?.let {
            canvas.save(); canvas.translate(left, y); it.draw(canvas); canvas.restore()
        }
        return bitmap
    }

    private fun drawBackground(canvas: Canvas, style: BackgroundStyle, width: Int, height: Int) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        if (style.startColor == style.endColor) {
            paint.color = style.startColor.toInt()
        } else {
            paint.shader = LinearGradient(
                0f, 0f, width * 0.3f, height.toFloat(),
                style.startColor.toInt(), style.endColor.toInt(), Shader.TileMode.CLAMP,
            )
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    private fun buildLayout(text: CharSequence, paint: TextPaint, width: Int, spacingMult: Float = 1f): StaticLayout =
        StaticLayout.Builder.obtain(text, 0, text.length, paint, width)
            .setAlignment(Layout.Alignment.ALIGN_CENTER)
            .setLineSpacing(0f, spacingMult)
            .setIncludePad(false)
            .build()
}
