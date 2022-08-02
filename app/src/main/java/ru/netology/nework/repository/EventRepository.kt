package ru.netology.nework.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nework.dto.Event
import ru.netology.nework.dto.User

interface EventRepository {
    val data: Flow<PagingData<Event>>

    suspend fun getAll()
}