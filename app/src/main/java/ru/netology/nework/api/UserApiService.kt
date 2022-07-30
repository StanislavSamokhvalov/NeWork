package ru.netology.nework.api

import retrofit2.Response
import retrofit2.http.GET
import ru.netology.nework.dto.User

interface UserApiService {
    @GET("users")
    suspend fun getAll(): Response<List<User>>
}