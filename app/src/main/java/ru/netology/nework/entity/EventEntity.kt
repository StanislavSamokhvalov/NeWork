package ru.netology.nework.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nework.dto.*
import ru.netology.nework.dto.AttachmentEmbeddable

@Entity
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val content: String,
    val datetime: String,
    val published: String,
    @Embedded
    val coordinates: CoordinatesEmbeddable?,
    @Embedded
    val type: EventTypeEmbeddable,
    val likeOwnerIds: Set<Int> = emptySet(),
    val likedByMe: Boolean = false,
    val speakerIds: Set<Int> = emptySet(),
    val participantsIds: Set<Int> = emptySet(),
    val participatedByMe: Boolean = false,
    @Embedded
    val attachment: AttachmentEmbeddable? = null,
    val link: String? = null,
    val ownedByMe: Boolean = false
) {

    fun toDto() =
        Event(
            id = id,
            authorId = authorId,
            author = author,
            authorAvatar = authorAvatar,
            content = content,
            datetime = datetime,
            published = published,
            coordinates = coordinates?.toDto(),
            type = type.toDto(),
            likeOwnerIds = likeOwnerIds,
            likedByMe = likedByMe,
            speakerIds = speakerIds,
            participantsIds = participantsIds,
            participatedByMe = participatedByMe,
            attachment = attachment?.toDto(),
            link = link,
            ownedByMe = ownedByMe
        )

    companion object {
        fun fromDto(dto: Event) =
            EventEntity(
                dto.id,
                dto.authorId,
                dto.author,
                dto.authorAvatar,
                dto.content,
                dto.datetime,
                dto.published,
                CoordinatesEmbeddable.fromDto(dto.coordinates),
                EventTypeEmbeddable.fromDto(dto.type),
                dto.likeOwnerIds,
                dto.likedByMe,
                dto.speakerIds,
                dto.participantsIds,
                dto.participatedByMe,
                AttachmentEmbeddable.fromDto(dto.attachment),
                dto.link,
                dto.ownedByMe
            )
    }
}

fun List<EventEntity>.toDto() = map(EventEntity::toDto)
fun List<Event>.toEventEntity() = map(EventEntity::fromDto)
