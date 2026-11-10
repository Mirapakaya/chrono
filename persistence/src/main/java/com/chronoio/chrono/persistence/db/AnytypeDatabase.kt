package com.chronoio.chrono.persistence.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.chronoio.chrono.persistence.common.Config
import com.chronoio.chrono.persistence.common.Provider
import com.chronoio.chrono.persistence.dao.AccountDao
import com.chronoio.chrono.persistence.model.AccountTable

@Database(
    entities = [AccountTable::class],
    exportSchema = false,
    version = 1
)
abstract class AnytypeDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao

    companion object : Provider<Context, AnytypeDatabase>() {
        override fun create(param: Context) = databaseBuilder(
            param.applicationContext,
            AnytypeDatabase::class.java,
            Config.DATABASE_NAME
        ).build()
    }
}