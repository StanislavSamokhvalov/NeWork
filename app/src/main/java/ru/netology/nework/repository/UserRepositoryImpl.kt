package ru.netology.nework.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.netology.nework.api.UserApiService
import ru.netology.nework.dao.UserDao
import ru.netology.nework.entity.UserEntity
import ru.netology.nework.entity.toDto
import ru.netology.nework.entity.toUserEntity
import ru.netology.nework.error.ApiError
import ru.netology.nework.error.NetworkError
import ru.netology.nework.error.UnknownError
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val apiUser: UserApiService
) : UserRepository {

    override val data = userDao.getAll()
        .map(List<UserEntity>::toDto)
        .flowOn(Dispatchers.Default)

    override suspend fun getAll() {
        try {
            userDao.getAll()
            val response = apiUser.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val data = response.body() ?: throw ApiError(response.code(), response.message())
            userDao.insert(data.toUserEntity())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}