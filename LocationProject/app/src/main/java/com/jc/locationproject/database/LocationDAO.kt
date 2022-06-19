package com.jc.locationproject.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationLogDAO {
    @Query("SELECT * FROM locationLog")
    suspend fun getAll(): List<LocationLog>

    @Query("SELECT * FROM locationLog WHERE userId = :userId AND isSynced = 0")
    suspend fun getAllUnsynced(userId: Int): List<LocationLog>

    @Query("SELECT * FROM locationLog WHERE userId IN (:userId)")
    suspend fun loadAllById(userId: Int): List<LocationLog>

    @Query("SELECT * FROM locationLog WHERE userId IN (:userId) ORDER BY updatedOn DESC LIMIT 1")
    suspend fun loadLatestById(userId: Int): LocationLog?

    @Insert
    suspend fun insertAll(vararg locationLog: LocationLog)

    @Query("UPDATE locationLog SET isSynced = :isSynced WHERE uid IN (:ids)")
    suspend fun update(ids: List<Long>, isSynced: Boolean)

    @Query("UPDATE locationLog SET stoppedTime = :stoppedTime, updatedOn = :updatedOn WHERE uid = :id")
    suspend fun update(id: Long, stoppedTime: Double, updatedOn: Long)

    @Delete
    suspend fun delete(locationLog: LocationLog)

    @Query("DELETE FROM locationLog")
    suspend fun deleteAll()

    @Query("DELETE FROM locationLog WHERE isSynced = 1")
    suspend fun deleteAllSynced()
}