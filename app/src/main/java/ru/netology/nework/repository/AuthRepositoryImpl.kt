package ru.netology.nework.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nework.api.AuthApiService
import ru.netology.nework.dto.PhotoUpload
import ru.netology.nework.dto.Token
import ru.netology.nework.error.ApiError
import ru.netology.nework.error.NetworkError
import ru.netology.nework.error.UnknownError
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    @ApplicationContext private val context: Context
) :
    AuthRepository {
    override suspend fun authUser(login: String, password: String): Token {
        try {
            val response = authApiService.authenticationRequest(login, password)
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
            val response = authApiService.registerUser(login, password, name)
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

    override suspend fun registerUserWithAvatar(
        login: String,
        password: String,
        name: String,
        photoUpload: PhotoUpload
    ): Token {

        val contentProvider = context.contentResolver
        val body = withContext(Dispatchers.IO) {
            contentProvider?.openInputStream(photoUpload.uri)?.readBytes()
        }?.toRequestBody() ?: error("File not found")

        val photo = MultipartBody.Part.createFormData(
            "file",
            "name",
            body
        )

        val response = authApiService.registrationUserWithAvatar(
            login.toRequestBody("text/plain".toMediaType()),
            password.toRequestBody("text/plain".toMediaType()),
            name.toRequestBody("text/plain".toMediaType()),
            photo
        )
        if (!response.isSuccessful) {
            throw ApiError(response.code(), response.message())
        }
        return response.body() ?: throw Exception()
    }
}