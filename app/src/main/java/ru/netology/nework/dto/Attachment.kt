package ru.netology.nework.dto

import ru.netology.nework.enumeration.AttachmentType

data class Attachment(
    val url: String,
    val type: AttachmentType
)

data class AttachmentEmbeddable(
    var url: String,
    var type: AttachmentType,
) {
    fun toDto() = Attachment(url, type)

    companion object {
        fun fromDto(dto: Attachment?) = dto?.let {
            AttachmentEmbeddable(it.url, it.type)
        }
    }
}
