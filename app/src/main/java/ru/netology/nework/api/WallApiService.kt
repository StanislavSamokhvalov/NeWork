package ru.netology.nework.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.netology.nework.dto.Post

interface WallApiService {

    @GET("{id}/wall/latest")
    suspend fun getLatest(@Path("userId") userId: Int, @Query("count") count: Int): Response<List<Post>>

    @GET("{id}/wall/{postId}/before")
    suspend fun getBefore(
        @Path("userId") userId: Int,
        @Path("postId") postId: Int,
        @Query("count") count: Int
    ): Response<List<Post>>
}