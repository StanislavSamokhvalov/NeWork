package ru.netology.nework.dto

import ru.netology.nework.enumeration.EventType

data class Event(
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val content: String,
    val datetime: String,
    val published: String,
    val coords: Coordinates? = null,
    val type: EventType,
    val likeOwnerIds: Set<Int> = emptySet(),
    val likedByMe: Boolean = false,
    val speakerIds: Set<Int> = emptySet(),
    val participantsIds: Set<Int> = emptySet(),
    val participatedByMe: Boolean = false,
    val attachment: Attachment? = null,
    val link: String? = null,
    val ownedByMe: Boolean = false
) {
    companion object {
        val empty = Event(
            id = 0,
            authorId = 0,
            author = "",
            authorAvatar = "",
            content = "",
            published = "",
            datetime = "",
            type = EventType.ONLINE,
            speakerIds = emptySet()
        )
    }
}

data class EventTypeEmbeddable(
    val eventType: String
) {
    fun toDto() = EventType.valueOf(eventType)

    companion object {
        fun fromDto(dto: EventType) = EventTypeEmbeddable(dto.name)
    }

}