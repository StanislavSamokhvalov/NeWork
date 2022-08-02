package ru.netology.nework.dto

data class Coordinates(
    val lat: Double,
    val long: Double
)

data class CoordinatesEmbeddable(
    val lat: Double,
    val long: Double,
) {
    fun toDto() = Coordinates(lat, long)

    companion object {
        fun fromDto(dto: Coordinates?) = dto?.let {
            CoordinatesEmbeddable(it.lat, it.long)
        }
    }
}