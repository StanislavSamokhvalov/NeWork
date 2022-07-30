package ru.netology.nework.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nework.dto.User

interface UserRepository {
    val data: Flow<List<User>>

    suspend fun getAll()
}