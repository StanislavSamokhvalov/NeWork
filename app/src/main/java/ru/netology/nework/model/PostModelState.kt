package ru.netology.nework.model

import ru.netology.nework.dto.Post

data class PostModel(
    val posts: List<Post> = emptyList(),
    val empty: Boolean = false
)

data class PostModelState(
    val error: Boolean = false,
    val loading: Boolean = false,
    val refreshing: Boolean = false,
    val errorLogin: Boolean = false,
    val errorRegistration: Boolean = false
)