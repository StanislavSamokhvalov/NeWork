package ru.netology.nework.model

import ru.netology.nework.dto.Job


data class JobModel (
    val job: List<Job> = emptyList(),
    val empty: Boolean = false
)

data class JobModelState(
    val loading: Boolean = false,
    val error: Boolean = false,
    val refreshing: Boolean = false
)