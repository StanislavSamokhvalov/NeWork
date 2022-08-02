package ru.netology.nework.repository

import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.netology.nework.api.EventApiService
import ru.netology.nework.dao.EventDao
import ru.netology.nework.dao.EventRemoteKeyDao
import ru.netology.nework.db.AppDb
import ru.netology.nework.dto.Event
import ru.netology.nework.entity.EventEntity
import ru.netology.nework.entity.toEventEntity
import ru.netology.nework.error.ApiError
import ru.netology.nework.error.NetworkError
import ru.netology.nework.error.UnknownError
import ru.netology.nework.mediator.EventRemoteMediator
import java.io.IOException
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val eventDao: EventDao,
    private val eventApiService: EventApiService,
    eventRemoteKeyDao: EventRemoteKeyDao,
    appDb: AppDb
) : EventRepository {
    @OptIn(ExperimentalPagingApi::class)
    override val data: Flow<PagingData<Event>> = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        remoteMediator = EventRemoteMediator(eventApiService, eventDao, eventRemoteKeyDao, appDb),
        pagingSourceFactory = { eventDao.getPagingSource() },
    )
        .flow
        .map { it.map(EventEntity::toDto) }

    override suspend fun getAll() {
        try {
            val response = eventApiService.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw Exception()
            eventDao.insert(body.toEventEntity())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}