package com.stoiclock.lockscreen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Recibe el botón "Otra frase" de la notificación y rota la frase sin abrir la app. */
@AndroidEntryPoint
class NextQuoteReceiver : BroadcastReceiver() {

    @Inject
    lateinit var updater: LockScreenUpdater

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ACTION_NEXT_QUOTE) return
        val pending = goAsync()
        scope.launch {
            try {
                updater.rotate()
            } finally {
                pending.finish()
            }
        }
    }

    companion object {
        const val ACTION_NEXT_QUOTE = "com.stoiclock.action.NEXT_QUOTE"
        private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
}
