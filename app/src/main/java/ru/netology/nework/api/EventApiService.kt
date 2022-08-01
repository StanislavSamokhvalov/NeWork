package ru.netology.nework.api

import retrofit2.Response
import retrofit2.http.GET
import ru.netology.nework.dto.Event

interface EventApiService {
    @GET("events")
    suspend fun getAll(): Response<List<Event>>
}