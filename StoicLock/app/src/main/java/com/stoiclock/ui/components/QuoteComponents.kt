package com.stoiclock.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stoiclock.domain.model.BackgroundStyle
import com.stoiclock.domain.model.Quote
import com.stoiclock.domain.model.QuoteFontSize

fun BackgroundStyle.brush(): Brush =
    if (startColor == endColor) Brush.verticalGradient(listOf(Color(startColor), Color(startColor)))
    else Brush.linearGradient(listOf(Color(startColor), Color(endColor)))

/**
 * Bloque editorial de una frase con la misma estética que el fondo de bloqueo:
 * comilla decorativa, cita en serif itálica, línea fina y autor en versalitas.
 */
@Composable
fun EditorialQuote(
    quote: Quote,
    style: BackgroundStyle,
    modifier: Modifier = Modifier,
    fontSize: QuoteFontSize = QuoteFontSize.MEDIUM,
    baseTextSize: TextUnit = 22.sp,
    contentPadding: Dp = 28.dp,
    maxLines: Int = Int.MAX_VALUE,
) {
    val text = Color(style.textColor)
    val accent = Color(style.accentColor)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(style.brush())
            .padding(contentPadding),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "“",
                color = accent,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = baseTextSize * 2.4f,
                lineHeight = baseTextSize * 1.6f,
            )
            Text(
                text = quote.text,
                color = text,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic,
                fontSize = baseTextSize * fontSize.scale,
                lineHeight = baseTextSize * fontSize.scale * 1.4f,
                textAlign = TextAlign.Center,
                maxLines = maxLines,
            )
            Spacer(Modifier.height(20.dp))
            HorizontalDivider(Modifier.width(36.dp), thickness = 1.dp, color = accent)
            Spacer(Modifier.height(14.dp))
            Text(
                text = quote.author.uppercase(),
                color = accent,
                style = MaterialTheme.typography.labelLarge,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center,
            )
            quote.work?.takeIf { it.isNotBlank() }?.let {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = it,
                    color = text.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        letterSpacing = 1.5.sp,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(top = 8.dp, bottom = 4.dp),
    )
}

@Composable
fun EmptyState(title: String, body: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
        Text(
            body,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
