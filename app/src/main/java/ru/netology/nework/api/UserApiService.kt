package ru.netology.nework.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import ru.netology.nework.dto.User
import ru.netology.nework.model.UserModel
import ru.netology.nework.model.UserModelState

interface UserApiService {
    @GET("users")
    suspend fun getAll(): Response<List<User>>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<User>

    @POST("TODO")
    suspend fun uploadAvatar(@Part media: MultipartBody.Part): Response<UserModel>

}