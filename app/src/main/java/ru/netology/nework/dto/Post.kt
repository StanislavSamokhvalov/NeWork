package ru.netology.nework.dto

data class Post(
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val content: String,
    val published: String,
//    val coords: Coordinates? = null,
    val link: String?,
//    val likeOwnerIds: List<Int>,
//    val mentionIds: List<Int>,
    val mentionedMe: Boolean,
    val likedByMe: Boolean,
    val ownedByMe: Boolean = false,
    val attachment: Attachment?
)