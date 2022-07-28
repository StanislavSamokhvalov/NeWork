package ru.netology.nework.repository

import ru.netology.nework.api.AuthApiService
import ru.netology.nework.dto.Token
import ru.netology.nework.error.ApiError
import ru.netology.nework.error.NetworkError
import ru.netology.nework.error.UnknownError
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val apiAuth: AuthApiService) : AuthRepository {
    override suspend fun authUser(login: String, password: String): Token {
        try {
            val response = apiAuth.authenticationRequest(login, password)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw Exception()
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun registerUser(login: String, password: String, name: String): Token {
        try {
            val response = apiAuth.registerUser(login, password, name)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw Exception()
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}