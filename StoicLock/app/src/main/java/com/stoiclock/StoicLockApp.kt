package com.stoiclock

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.stoiclock.domain.repository.SettingsRepository
import com.stoiclock.lockscreen.QuoteNotificationManager
import com.stoiclock.worker.RotationScheduler
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class StoicLockApp : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory
    @Inject lateinit var notificationManager: QuoteNotificationManager
    @Inject lateinit var settingsRepository: SettingsRepository
    @Inject lateinit var rotationScheduler: RotationScheduler

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        notificationManager.createChannel()
        // Asegura que la rotación periódica esté programada con la frecuencia guardada.
        appScope.launch {
            rotationScheduler.schedule(settingsRepository.current().frequency)
        }
    }
}
