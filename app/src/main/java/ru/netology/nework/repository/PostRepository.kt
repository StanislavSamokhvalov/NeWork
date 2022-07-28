package ru.netology.nework.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nework.dto.MediaUpload
import ru.netology.nework.dto.Post
import ru.netology.nework.enumeration.AttachmentType

interface PostRepository {
    val data: Flow<List<Post>>

    suspend fun getAll()
    suspend fun save(post: Post)
    suspend fun removeById(id: Int)
    suspend fun likeById(id: Int)
    suspend fun unlikeById(id: Int)
//    suspend fun getNewerPosts()
//    suspend fun saveWithAttachment(post: Post, upload: MediaUpload, type: AttachmentType)
//    suspend fun uploadWithContent(upload: MediaUpload): Media
}