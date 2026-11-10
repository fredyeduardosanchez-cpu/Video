package com.stoiclock.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.stoiclock.lockscreen.LockScreenUpdater
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/** Rota la frase de la pantalla de bloqueo periódicamente (o una vez, si se encola como OneTime). */
@HiltWorker
class QuoteRotationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val updater: LockScreenUpdater,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = try {
        val result = if (inputData.getBoolean(KEY_REFRESH_ONLY, false)) updater.refreshCurrent() else updater.rotate()
        Result.success(workDataOf(KEY_QUOTE_ID to (result.quote?.id ?: -1L)))
    } catch (e: Exception) {
        if (runAttemptCount < MAX_ATTEMPTS) Result.retry() else Result.failure()
    }

    companion object {
        const val KEY_REFRESH_ONLY = "refresh_only"
        const val KEY_QUOTE_ID = "quote_id"
        private const val MAX_ATTEMPTS = 3
    }
}
