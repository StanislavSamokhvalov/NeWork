package ru.netology.nework.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import ru.netology.nework.api.AuthApiService
import ru.netology.nework.api.PostApiService
import ru.netology.nework.dao.PostDao
import ru.netology.nework.dto.Attachment
import ru.netology.nework.dto.MediaUpload
import ru.netology.nework.dto.Post
import ru.netology.nework.entity.PostEntity
import ru.netology.nework.entity.toDto
import ru.netology.nework.entity.toEntity
import ru.netology.nework.enumeration.AttachmentType
import ru.netology.nework.error.ApiError
import ru.netology.nework.error.NetworkError
import ru.netology.nework.error.UnknownError
import java.io.IOException
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postDao: PostDao,
    private val apiPost: PostApiService
) : PostRepository {

    override val data = postDao.getAll()
        .map(List<PostEntity>::toDto)
        .flowOn(Dispatchers.Default)

    override suspend fun getAll() {
        try {
            postDao.getAll()
            val response = apiPost.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val data = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(data.toEntity())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun likeById(id: Int) {
        try {
            val response = apiPost.likeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val data = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(data))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun unlikeById(id: Int) {
        try {
            val response = apiPost.unlikeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val data = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(data))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

//    override suspend fun saveWithAttachment(post: Post, upload: MediaUpload, type: AttachmentType) {
//                try {
//            val media = uploadWithContent(upload)
//            val postWithAttachment =
//                post.copy(attachment = Attachment(media., type))
//            save(postWithAttachment)
//        } catch (e: IOException) {
//            throw NetworkError
//        } catch (e: Exception) {
//            throw UnknownError
//        }
//    }

//    override suspend fun uploadWithContent(upload: MediaUpload): Media {
//        try {
//            val media = MultipartBody.Part.createFormData(
//                "file", upload.file.name, upload.file.asRequestBody()
//            )
//
//            val content = MultipartBody
//                .Part.createFormData("content", "any text")
//
//            val response = api.uploadWithContent(media, content)
//            if (!response.isSuccessful) {
//                throw ApiError(response.code(), response.message())
//            }
//            return response.body() ?: throw ApiError(response.code(), response.message())
//
//        } catch (e: IOException) {
//            throw NetworkError
//        } catch (e: Exception) {
//            throw UnknownError
//        }
//    }

    override suspend fun save(post: Post) {
        try {
            val response = apiPost.save(post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val data = response.body() ?: throw ApiError(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(data))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun removeById(id: Int) {
        try {
            val response = apiPost.removeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            postDao.removeById(id)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    //    override suspend fun getNewerPosts() {
//        try {
//            postDao.getNewer()
//        } catch (e: IOException) {
//            throw NetworkError
//        } catch (e: Exception) {
//            throw UnknownError
//        }
//    }
//
//


//    override fun getNewerCount(id: Int): Flow<Int> = flow {
//        while (true) {
//            delay(10_000L)
//            val response = api.getNewer(id)
//            if (!response.isSuccessful) {
//                throw ApiError(response.code(), response.message())
//            }
//            val data = response.body() ?: throw ApiError(response.code(), response.message())
//            postDao.insert(data.toEntity())
//            emit(data.size)
//        }
//    }
//        .catch { e -> throw AppError.from(e) }
//        .flowOn(Dispatchers.Default)
}