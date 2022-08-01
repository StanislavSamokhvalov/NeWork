package ru.netology.nework.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nework.dto.Attachment
import ru.netology.nework.dto.Coordinates
import ru.netology.nework.dto.Event
import ru.netology.nework.enumeration.EventType

@Entity
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String?,
    val content: String,
    val datetime: String,
    val published: String,
    @Embedded
    val coords: Coordinates? = null,
    @Embedded
    val type: EventType,
    val likeOwnerIds: Set<Long> = emptySet(),
    val likedByMe: Boolean = false,
    val speakerIds: Set<Long> = emptySet(),
    val participantsIds: Set<Long> = emptySet(),
    val participatedByMe: Boolean = false,
    @Embedded
    val attachment: Attachment? = null,
    val link: String? = null,
    val ownedByMe: Boolean = false
) {
    fun toDto() =
        Event(
            id,
            authorId,
            author,
            authorAvatar,
            content,
            datetime,
            published,
            coords,
            type,
            likeOwnerIds,
            likedByMe,
            speakerIds
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
                dto.coords,
                dto.type,
                dto.likeOwnerIds,
                dto.likedByMe,
                dto.speakerIds
            )
    }
}

fun List<EventEntity>.toDto() = map(EventEntity::toDto)
fun List<Event>.toEventEntity() = map(EventEntity::fromDto)
