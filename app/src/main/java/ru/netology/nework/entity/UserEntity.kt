package ru.netology.nework.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nework.dto.User

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val login: String,
    val name: String,
    val avatar: String? = null
) {
    fun toDto() =
        User(
            id = id,
            login = login,
            name = name,
            avatar = avatar
        )

    companion object {
        fun fromDto(dto: User) =
            UserEntity(
                dto.id,
                dto.login,
                dto.name,
                dto.avatar
            )
    }
}

fun List<UserEntity>.toDto() = map(UserEntity::toDto)
fun List<User>.toUserEntity() = map(UserEntity::fromDto)