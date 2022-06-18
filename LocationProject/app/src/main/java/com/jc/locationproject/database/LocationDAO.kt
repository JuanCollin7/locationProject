package com.jc.locationproject.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationLogDAO {
    @Query("SELECT * FROM locationLog")
    suspend fun getAll(): List<LocationLog>

    @Query("SELECT * FROM locationLog WHERE userId IN (:userId)")
    suspend fun loadAllByIds(userId: Int): List<LocationLog>

    @Query("SELECT * FROM locationLog WHERE userId IN (:userId) ORDER BY timestamp DESC LIMIT 1")
    suspend fun loadLatestById(userId: Int): LocationLog?

    @Insert
    suspend fun insertAll(vararg locationLog: LocationLog)

    @Query("UPDATE locationLog SET isSynced=:isSynced WHERE uid IN (:ids)")
    suspend fun update(ids: List<Long>, isSynced: Boolean)

    @Delete
    suspend fun delete(locationLog: LocationLog)

    @Query("DELETE FROM locationLog")
    suspend fun deleteAll()
}