package ru.netology.nework.model

import ru.netology.nework.dto.Event

data class EventModelState(
    val loading: Boolean = false,
    val error: Boolean = false,
    val refreshing: Boolean = false,
    val retryId: Long = 0,
    val retryEvent: Event? = null
)