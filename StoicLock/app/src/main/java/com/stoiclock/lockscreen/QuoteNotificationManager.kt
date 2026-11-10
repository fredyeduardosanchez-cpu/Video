package com.stoiclock.lockscreen

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.stoiclock.MainActivity
import com.stoiclock.R
import com.stoiclock.domain.model.Quote
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Modo 2: notificación fija, silenciosa y pública (VISIBILITY_PUBLIC) con BigTextStyle para que
 * la frase completa se lea en la pantalla de bloqueo, y una acción "Otra frase".
 */
@Singleton
class QuoteNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val manager = NotificationManagerCompat.from(context)

    fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_LOW,
            ).apply {
                description = context.getString(R.string.notification_channel_description)
                setSound(null, null)
                enableVibration(false)
                setShowBadge(false)
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
            }
            context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    fun canPostNotifications(): Boolean {
        val permissionOk = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        return permissionOk && manager.areNotificationsEnabled()
    }

    /** @return true si la notificación se publicó. */
    fun show(quote: Quote): Boolean {
        if (!canPostNotifications()) return false
        createChannel()

        val openApp = PendingIntent.getActivity(
            context, REQUEST_OPEN,
            Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        val nextQuote = PendingIntent.getBroadcast(
            context, REQUEST_NEXT,
            Intent(context, NextQuoteReceiver::class.java).setAction(NextQuoteReceiver.ACTION_NEXT_QUOTE),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        val attribution = buildString {
            append("— ").append(quote.author)
            quote.work?.takeIf { it.isNotBlank() }?.let { append(", ").append(it) }
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_quote)
            .setContentTitle(quote.author)
            .setContentText(quote.text)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("«${quote.text}»\n$attribution")
                    .setSummaryText(context.getString(R.string.app_name))
            )
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setSilent(true)
            .setShowWhen(false)
            .setContentIntent(openApp)
            .addAction(R.drawable.ic_refresh, context.getString(R.string.action_next_quote), nextQuote)
            .build()

        return try {
            manager.notify(NOTIFICATION_ID, notification)
            true
        } catch (e: SecurityException) {
            false
        }
    }

    fun cancel() = manager.cancel(NOTIFICATION_ID)

    companion object {
        const val CHANNEL_ID = "stoic_quote_lockscreen"
        const val NOTIFICATION_ID = 1001
        private const val REQUEST_OPEN = 10
        private const val REQUEST_NEXT = 11
    }
}
