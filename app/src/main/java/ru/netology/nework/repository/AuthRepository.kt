package ru.netology.nework.repository

import ru.netology.nework.dto.MediaUpload
import ru.netology.nework.dto.PhotoUpload
import ru.netology.nework.dto.Token

interface AuthRepository {
    suspend fun authUser(login: String, password: String): Token
    suspend fun registerUser(name: String, login: String, pass: String): Token
    suspend fun registerUserWithAvatar(login: String, password: String, name: String, photoUpload: PhotoUpload): Token
}