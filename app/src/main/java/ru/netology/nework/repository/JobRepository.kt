package ru.netology.nework.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nework.dto.Job


interface JobRepository {
    val data : Flow<List<Job>>

    suspend fun getByUserId(id: Int)
    suspend fun save(job: Job)
    suspend fun removeById(id: Int)
}