package ru.netology.nework.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.nework.entity.EventRemoteKeyEntity

@Dao
interface EventRemoteKeyDao {
    @Query("SELECT COUNT(*) == 0 FROM EventRemoteKeyEntity")
    suspend fun isEmpty(): Boolean

    @Query("SELECT MAX(id) FROM EventRemoteKeyEntity")
    suspend fun max(): Int?

    @Query("SELECT MIN(id) FROM EventRemoteKeyEntity")
    suspend fun min(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(key: EventRemoteKeyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keys: List<EventRemoteKeyEntity>)
}