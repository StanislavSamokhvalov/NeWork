package ru.netology.nework.dto

data class Job(
    val id: Int,
    val name: String,
    val position: String,
    val start: String,
    val finish: String? = null,
    val link: String? = null,
    val ownedByMe: Boolean = false
) {
    companion object {
        val empty = Job(
            id = 0,
            name = "",
            position = "",
            start = "",
            finish = null
        )
    }
}
