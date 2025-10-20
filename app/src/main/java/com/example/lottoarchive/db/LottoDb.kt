package com.example.lottoarchive.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Draw::class], version = 1)
abstract class LottoDb : RoomDatabase() {
    abstract fun drawDao(): DrawDao

    companion object {
        @Volatile private var INSTANCE: LottoDb? = null

        fun get(ctx: Context): LottoDb =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(ctx.applicationContext, LottoDb::class.java, "lotto.db").build().also { INSTANCE = it }
            }
    }
}
