package ru.netology.nework.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nework.repository.*
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    fun bindPostRepository(impl: PostRepositoryImpl): PostRepository

    @Binds
    @Singleton
    fun bindEventRepository(impl: EventRepositoryImpl): EventRepository
}