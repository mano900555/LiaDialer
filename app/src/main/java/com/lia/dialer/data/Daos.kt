package com.lia.dialer.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<Favorite>>

    @Insert
    suspend fun addFavorite(favorite: Favorite)

    @Delete
    suspend fun removeFavorite(favorite: Favorite)

    @Query("SELECT * FROM favorites WHERE phoneNumber = :phoneNumber")
    suspend fun getFavorite(phoneNumber: String): Favorite?
}

@Dao
interface BlockedNumberDao {
    @Query("SELECT * FROM blocked_numbers ORDER BY blockedAt DESC")
    fun getAllBlockedNumbers(): Flow<List<BlockedNumber>>

    @Insert
    suspend fun addBlockedNumber(blockedNumber: BlockedNumber)

    @Delete
    suspend fun removeBlockedNumber(blockedNumber: BlockedNumber)

    @Query("SELECT * FROM blocked_numbers WHERE phoneNumber = :phoneNumber")
    suspend fun isNumberBlocked(phoneNumber: String): BlockedNumber?
}

@Dao
interface RecentCallDao {
    @Query("SELECT * FROM recent_calls ORDER BY timestamp DESC LIMIT 100")
    fun getRecentCalls(): Flow<List<RecentCall>>

    @Insert
    suspend fun addRecentCall(recentCall: RecentCall)

    @Delete
    suspend fun deleteRecentCall(recentCall: RecentCall)

    @Query("DELETE FROM recent_calls")
    suspend fun clearRecentCalls()
}
