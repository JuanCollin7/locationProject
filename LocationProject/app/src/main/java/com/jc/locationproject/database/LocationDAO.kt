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

    @Insert
    suspend fun insertAll(vararg locationLog: LocationLog)

    @Delete
    suspend fun delete(locationLog: LocationLog)

    @Query("DELETE FROM locationLog")
    suspend fun deleteAll()
}