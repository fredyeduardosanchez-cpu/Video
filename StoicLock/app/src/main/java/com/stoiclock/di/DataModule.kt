package com.stoiclock.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.stoiclock.data.local.QuoteDao
import com.stoiclock.data.local.StoicDatabase
import com.stoiclock.data.repository.QuoteRepositoryImpl
import com.stoiclock.data.settings.SettingsRepositoryImpl
import com.stoiclock.domain.repository.QuoteRepository
import com.stoiclock.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.lockScreenDataStore: DataStore<Preferences> by preferencesDataStore(name = "lock_screen_settings")

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): StoicDatabase =
        Room.databaseBuilder(context, StoicDatabase::class.java, StoicDatabase.NAME)
            .addCallback(StoicDatabase.SeedCallback())
            .build()

    @Provides
    fun provideQuoteDao(db: StoicDatabase): QuoteDao = db.quoteDao()

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.lockScreenDataStore
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindQuoteRepository(impl: QuoteRepositoryImpl): QuoteRepository

    @Binds
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}
