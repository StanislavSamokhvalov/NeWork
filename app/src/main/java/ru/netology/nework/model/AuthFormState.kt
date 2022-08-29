package ru.netology.nework.model

data class AuthFormState(
    val loading: Boolean = false,
    val error: Boolean = false,
    val errorRegistration: Boolean = false,
    val errorAuth: Boolean = false,
)

