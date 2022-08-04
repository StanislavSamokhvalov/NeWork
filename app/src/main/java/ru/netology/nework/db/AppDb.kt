package ru.netology.nework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.netology.nework.dao.*
import ru.netology.nework.entity.EventEntity
import ru.netology.nework.entity.EventRemoteKeyEntity
import ru.netology.nework.entity.PostEntity
import ru.netology.nework.entity.UserEntity

@Database(
    entities = [PostEntity::class, UserEntity::class, EventEntity::class, EventRemoteKeyEntity::class], version = 1
)
@TypeConverters(Converters::class)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun userDao(): UserDao
    abstract fun eventDao(): EventDao
    abstract fun eventRemoteKeyDao(): EventRemoteKeyDao
}