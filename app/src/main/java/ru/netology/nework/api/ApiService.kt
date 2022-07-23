package ru.netology.nework.api

import retrofit2.Response
import retrofit2.http.*
import ru.netology.nework.dto.Post
import ru.netology.nework.dto.PushToken
import ru.netology.nework.dto.Token
import ru.netology.nework.dto.User

interface ApiService {
    @GET("posts")
    suspend fun getAll(): Response<List<Post>>

    @FormUrlEncoded
    @POST("users/authentication")
    suspend fun authenticationRequest(
        @Field("Login") login: String?,
        @Field("Password") password: String?
    ): Response<Token>

    @FormUrlEncoded
    @POST("users/registration")
    suspend fun registerUser(
        @Field("login") login: String?,
        @Field("pass") pass: String?,
        @Field("name") name: String?
    ): Response<Token>

    @POST("users/push-tokens")
    suspend fun savePushToken(@Body pushToken: PushToken): Response<Unit>
}