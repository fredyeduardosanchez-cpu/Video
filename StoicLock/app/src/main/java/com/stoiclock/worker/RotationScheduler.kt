package com.stoiclock.worker

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.stoiclock.domain.model.RotationFrequency
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/** Programa la rotación periódica con WorkManager (respetando Doze y sin wake locks propios). */
@Singleton
class RotationScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val workManager get() = WorkManager.getInstance(context)

    fun schedule(frequency: RotationFrequency) {
        val request = PeriodicWorkRequestBuilder<QuoteRotationWorker>(frequency.hours, TimeUnit.HOURS)
            .setInitialDelay(frequency.hours, TimeUnit.HOURS)
            .setConstraints(Constraints.Builder().setRequiresBatteryNotLow(true).build())
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 15, TimeUnit.MINUTES)
            .addTag(TAG)
            .build()
        // UPDATE conserva el ciclo si la frecuencia no cambió y lo reemplaza si cambió.
        workManager.enqueueUniquePeriodicWork(PERIODIC_WORK, ExistingPeriodicWorkPolicy.UPDATE, request)
    }

    /** Encola una actualización inmediata en segundo plano (no requiere que la app esté abierta). */
    fun refreshSoon(refreshOnly: Boolean) {
        val request = OneTimeWorkRequestBuilder<QuoteRotationWorker>()
            .setInputData(workDataOf(QuoteRotationWorker.KEY_REFRESH_ONLY to refreshOnly))
            .addTag(TAG)
            .build()
        workManager.enqueueUniqueWork(ONE_TIME_WORK, ExistingWorkPolicy.REPLACE, request)
    }

    fun cancel() = workManager.cancelUniqueWork(PERIODIC_WORK)

    companion object {
        const val PERIODIC_WORK = "stoic_quote_rotation"
        const val ONE_TIME_WORK = "stoic_quote_rotation_now"
        const val TAG = "stoic_rotation"
    }
}
