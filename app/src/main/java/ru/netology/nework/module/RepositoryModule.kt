package ru.netology.nework.module

import ru.netology.nework.repository.UserRepository
import ru.netology.nework.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nework.repository.PostRepository
import ru.netology.nework.repository.PostRepositoryImpl
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
}