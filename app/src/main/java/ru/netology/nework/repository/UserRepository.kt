package ru.netology.nework.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nework.dto.PhotoUpload
import ru.netology.nework.dto.User
import ru.netology.nework.model.UserModel
import ru.netology.nework.model.UserModelState

interface UserRepository {
    val data: Flow<List<User>>

    suspend fun getAll()
    suspend fun getUserById(id: Int): User
    suspend fun uploadAvatar(photoUpload: PhotoUpload): UserModel
}