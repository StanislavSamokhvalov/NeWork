package ru.netology.nework.dto

data class CoordinatesEmbeddable(
    var lat: Double? = 0.0,
    var long: Double? = 0.0,
) {
    fun toDto() = Coordinates(lat, long)

    companion object {
        fun fromDto(dto: Coordinates?) = dto?.let {
            CoordinatesEmbeddable(it.lat, it.long)
        }
    }
}