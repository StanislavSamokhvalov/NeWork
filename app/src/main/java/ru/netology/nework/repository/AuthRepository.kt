package ru.netology.nework.repository

import ru.netology.nework.dto.Token

interface AuthRepository {
    suspend fun authUser(login: String, password: String): Token
    suspend fun registerUser(name: String, login: String, pass: String): Token
}