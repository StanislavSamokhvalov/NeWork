package ru.netology.nework.api

import retrofit2.Response
import retrofit2.http.*
import ru.netology.nework.dto.Event

interface EventApiService {
    @GET("events")
    suspend fun getAll(): Response<List<Event>>

    @POST("events/{id}/participants")
    suspend fun partyById(@Path("id") id: Int): Response<Event>

    @DELETE("events/{id}/participants")
    suspend fun unPartyById(@Path("id") id: Int): Response<Event>

    @DELETE("events/{event_id}")
    suspend fun removeById(@Path("id") id: Int): Response<Unit>

    @DELETE("events/{id}/likes")
    suspend fun unlikeById(@Path("id") id: Int): Response<Event>

    @POST("events/{id}/likes")
    suspend fun likeById(@Path("id") id: Int): Response<Event>

    @GET("events/latest")
    suspend fun getLatest(@Query("count") count: Int): Response<List<Event>>

    @GET("events/{id}/before")
    suspend fun getBefore(@Path("id") id: Int, @Query("count") count: Int): Response<List<Event>>

    @GET("events/{id}/after")
    suspend fun getAfter(@Path("id") id: Int, @Query("count") count: Int): Response<List<Event>>
}