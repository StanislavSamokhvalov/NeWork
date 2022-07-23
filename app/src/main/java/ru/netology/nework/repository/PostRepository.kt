package ru.netology.nework.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nework.dto.Post

interface PostRepository {
    val data: Flow<List<Post>>

    suspend fun getAll()
    suspend fun removeById(id: Int)
}