package ru.netology.nework.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nework.dto.*

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val authorId: Int = 0,
    val author: String = "",
    val authorAvatar: String? = "",
    val content: String = "",
    val published: String = "",
    @Embedded
    val coordinates: CoordinatesEmbeddable? = null,
    val link: String? = "",
    val likeOwnerIds: Set<Int> = emptySet(),
    val mentionIds: Set<Int> = emptySet(),
    val mentionedMe: Boolean = false,
    val likedByMe: Boolean = false,
    val ownedByMe: Boolean = false,
    @Embedded
    val attachment: AttachmentEmbeddable?
) {
    fun toDto() = Post(
        id = id,
        authorId = authorId,
        author = author,
        authorAvatar = authorAvatar,
        content = content,
        published = published,
        coordinates = coordinates?.toDto(),
        link = link,
        likeOwnerIds = likeOwnerIds,
        mentionIds = mentionIds,
        mentionedMe = mentionedMe,
        likedByMe = likedByMe,
        ownedByMe = ownedByMe,
        attachment = attachment?.toDto()
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
                CoordinatesEmbeddable.fromDto(dto.coordinates),
                dto.link,
                dto.likeOwnerIds,
                dto.mentionIds,
                dto.mentionedMe,
                dto.likedByMe,
                dto.ownedByMe,
                AttachmentEmbeddable.fromDto(dto.attachment)
            )

    }
}

fun List<PostEntity>.toDto() = map(PostEntity::toDto)
fun List<Post>.toPostEntity() = map(PostEntity::fromDto)