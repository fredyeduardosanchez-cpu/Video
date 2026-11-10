package com.stoiclock.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontFamily
import androidx.glance.text.FontStyle
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.stoiclock.domain.model.Quote
import com.stoiclock.domain.usecase.ObserveCurrentQuoteUseCase
import com.stoiclock.lockscreen.LockScreenUpdater
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun observeCurrentQuote(): ObserveCurrentQuoteUseCase
    fun lockScreenUpdater(): LockScreenUpdater
}

internal fun Context.widgetEntryPoint(): WidgetEntryPoint =
    EntryPointAccessors.fromApplication(applicationContext, WidgetEntryPoint::class.java)

/** Modo 3: widget minimalista con la frase actual; al tocarlo cambia de frase. */
class StoicGlanceWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val entryPoint = context.widgetEntryPoint()
        val quoteFlow = entryPoint.observeCurrentQuote()()
        provideContent {
            val quote by quoteFlow.collectAsState(initial = null)
            WidgetContent(quote)
        }
    }

    @Composable
    private fun WidgetContent(quote: Quote?) {
        val ink = ColorProvider(Color(0xFFE7E3DC))
        val accent = ColorProvider(Color(0xFFD7B98E))
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(Color(0xFF121214))
                .cornerRadius(20.dp)
                .padding(16.dp)
                .clickable(actionRunCallback<NextQuoteAction>()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = quote?.let { "«${it.text}»" } ?: "Toca para ver una frase",
                style = TextStyle(
                    color = ink,
                    fontSize = 15.sp,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Center,
                ),
                maxLines = 7,
            )
            if (quote != null) {
                Spacer(GlanceModifier.height(10.dp))
                Text(
                    text = quote.author.uppercase(),
                    style = TextStyle(
                        color = accent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    ),
                    maxLines = 1,
                )
            }
        }
    }
}

class NextQuoteAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        context.widgetEntryPoint().lockScreenUpdater().rotate()
    }
}

class StoicWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = StoicGlanceWidget()
}
