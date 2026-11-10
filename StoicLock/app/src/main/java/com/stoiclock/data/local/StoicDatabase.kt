package com.stoiclock.data.local

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [QuoteEntity::class], version = 1, exportSchema = true)
abstract class StoicDatabase : RoomDatabase() {
    abstract fun quoteDao(): QuoteDao

    companion object {
        const val NAME = "stoiclock.db"
    }

    /**
     * Pre-poblado: al crearse la base de datos (primer arranque) se insertan las frases de fábrica
     * dentro de la misma transacción de creación, por lo que nunca hay una base vacía.
     */
    class SeedCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            seed(db)
        }

        companion object {
            fun seed(db: SupportSQLiteDatabase, now: Long = System.currentTimeMillis()) {
                SeedQuotes.all.forEachIndexed { index, seed ->
                    val values = ContentValues().apply {
                        put("text", seed.text)
                        put("author", seed.author)
                        put("work", seed.work)
                        put("isUserCreated", 0)
                        put("isFavorite", 0)
                        put("includeInLockScreen", 1)
                        put("createdAt", now + index)
                    }
                    db.insert(QuoteEntity.TABLE, SQLiteDatabase.CONFLICT_IGNORE, values)
                }
            }
        }
    }
}
