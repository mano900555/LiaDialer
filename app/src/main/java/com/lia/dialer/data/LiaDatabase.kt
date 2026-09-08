package com.lia.dialer.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Favorite::class, BlockedNumber::class, RecentCall::class],
    version = 1,
    exportSchema = false
)
abstract class LiaDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun blockedNumberDao(): BlockedNumberDao
    abstract fun recentCallDao(): RecentCallDao

    companion object {
        @Volatile
        private var instance: LiaDatabase? = null

        fun getInstance(context: Context): LiaDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    LiaDatabase::class.java,
                    "lia_database"
                ).build().also { instance = it }
            }
        }
    }
}
