package ru.netology.nework.api

import retrofit2.Response
import retrofit2.http.*
import ru.netology.nework.dto.Event
import ru.netology.nework.dto.Post


interface PostApiService {
    @GET("posts")
    suspend fun getAll(): Response<List<Post>>

    @GET("posts/{id}/newer")
    suspend fun getNewer(@Path("id") id: Int): Response<List<Post>>

    @GET("posts/latest")
    suspend fun getLatest(@Query("count") count: Int): Response<List<Post>>

    @GET("posts/{id}/before")
    suspend fun getBefore(@Path("id") id: Int, @Query("count") count: Int): Response<List<Post>>

    @DELETE("posts/{id}")
    suspend fun removeById(@Path("id") id: Int): Response<Unit>

    @POST("posts")
    suspend fun save(@Body post: Post): Response<Post>

    @DELETE("posts/{id}/likes")
    suspend fun unlikeById(@Path("id") id: Int): Response<Post>

    @POST("posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Int): Response<Post>
}