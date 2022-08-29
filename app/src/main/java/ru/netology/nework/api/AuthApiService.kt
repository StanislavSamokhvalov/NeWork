package ru.netology.nework.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import ru.netology.nework.dto.Post
import ru.netology.nework.dto.PushToken
import ru.netology.nework.dto.Token
import ru.netology.nework.dto.User

interface AuthApiService {
    @FormUrlEncoded
    @POST("users/authentication")
    suspend fun authenticationRequest(
        @Field("login") login: String?,
        @Field("password") password: String?
    ): Response<Token>

    @FormUrlEncoded
    @POST("users/registration")
    suspend fun registerUser(
        @Field("login") login: String?,
        @Field("password") password: String?,
        @Field("name") name: String?
    ): Response<Token>

    @Multipart
    @POST("users/registration")
    suspend fun registrationUserWithAvatar(
        @Part("login") login: RequestBody,
        @Part("password") password: RequestBody,
        @Part("name") name: RequestBody,
        @Part media: MultipartBody.Part
    ): Response<Token>

    @POST("users/push-tokens")
    suspend fun savePushToken(@Body pushToken: PushToken): Response<Unit>
}