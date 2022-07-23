package ru.netology.nework.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nework.dto.Attachment
import ru.netology.nework.dto.Coordinates
import ru.netology.nework.dto.Post
import ru.netology.nework.enumeration.AttachmentType

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val authorId: Int = 0,
    val author: String = "",
    val authorAvatar: String? = "",
    val content: String = "",
    val published: String = "",
//    @Embedded
//    val coords: Coordinates? = null,
    val link: String? = "",
//    @Embedded
//    val likeOwnerIds: List<Int>,
//    @Embedded
//    val mentionIds: List<Int>,
    val mentionedMe: Boolean = false,
    val likedByMe: Boolean = false,
    val ownedByMe: Boolean = false,
    @Embedded
    val attachment: AttachmentEmbeddable?
) {
    fun toDto() = Post(
        id,
        authorId,
        author,
        authorAvatar,
        content,
        published,
//        coords,
        link,
//        likeOwnerIds,
//        mentionIds,
        mentionedMe,
        likedByMe,
        ownedByMe,
        attachment?.toDto()
    )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                dto.id,
                dto.authorId,
                dto.author,
                dto.authorAvatar,
                dto.content,
                dto.published,
//                dto.coords,
                dto.link,
//                dto.likeOwnerIds,
//                dto.mentionIds,
                dto.mentionedMe,
                dto.likedByMe,
                dto.ownedByMe,
                AttachmentEmbeddable.fromDto(dto.attachment)
            )

    }
}

data class AttachmentEmbeddable(
    var url: String,
    var type: AttachmentType
) {
    fun toDto() = Attachment(url = url, type = type)

    companion object {
        fun fromDto(dto: Attachment?) = dto?.let {
            AttachmentEmbeddable(it.url, it.type)
        }
    }
}

fun List<PostEntity>.toDto() = map(PostEntity::toDto)
fun List<Post>.toEntity() = map(PostEntity::fromDto)