package ru.netology.nework.dto

data class Post(
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val content: String,
    val published: String,
    val coordinates: Coordinates? = null,
    val link: String? = null,
    val likeOwnerIds: Set<Int> = emptySet(),
    val mentionIds: Set<Int> = emptySet(),
    val mentionedMe: Boolean = false,
    val likedByMe: Boolean = false,
    val ownedByMe: Boolean = true,
    val attachment: Attachment? = null
) {
    companion object {
        val empty = Post(
            id = 0,
            authorId = 0,
            author = "",
            authorAvatar = "",
            content = "",
            published = "2022-07-28"
        )
    }
}