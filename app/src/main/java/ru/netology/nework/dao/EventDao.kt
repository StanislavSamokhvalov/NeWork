package ru.netology.nework.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.netology.nework.entity.EventEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM EventEntity ORDER BY id DESC")
    fun getPagingSource(): PagingSource<Int, EventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(events: List<EventEntity>)

    @Query("DELETE FROM EventEntity WHERE id = :id")
    suspend fun removeById(id: Int)

    @Query("UPDATE EventEntity SET participatedByMe = 1 WHERE id = :id AND participatedByMe = 0")
    suspend fun participateById(id: Int)

    @Query("UPDATE EventEntity SET participatedByMe = 0 WHERE id = :id AND participatedByMe = 1")
    suspend fun unParticipateById(id: Int)

    @Query("UPDATE EventEntity SET likedByMe = 1 WHERE id = :id AND likedByMe = 0")
    suspend fun likeById(id: Int)

    @Query("UPDATE EventEntity SET likedByMe = 0 WHERE id = :id AND likedByMe = 1")
    suspend fun unLikeById(id: Int)
}