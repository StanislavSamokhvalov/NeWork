package ru.netology.nework.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nework.dto.Media
import ru.netology.nework.dto.MediaUpload
import ru.netology.nework.dto.Post
import ru.netology.nework.enumeration.AttachmentType

interface PostRepository {
    val data: Flow<PagingData<Post>>

    suspend fun getAll()
    suspend fun save(post: Post)
    suspend fun removeById(id: Int)
    suspend fun likeById(id: Int)
    suspend fun unlikeById(id: Int)
    suspend fun saveWithAttachment(post: Post, upload: MediaUpload, type: AttachmentType)
    suspend fun upload(upload: MediaUpload): Media
}