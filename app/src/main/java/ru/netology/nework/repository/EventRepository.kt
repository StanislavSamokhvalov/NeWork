package ru.netology.nework.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nework.dto.Event
import ru.netology.nework.dto.User

interface EventRepository {
    val data: Flow<PagingData<Event>>

    suspend fun getAll()
    suspend fun save(event: Event)
    suspend fun joinById(id: Int)
    suspend fun unJoinById(id: Int)
    suspend fun removeById(id: Int)
    suspend fun likeById(id: Int)
    suspend fun unLikeById(id: Int)
}