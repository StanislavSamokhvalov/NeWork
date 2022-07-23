package ru.netology.nework.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nework.repository.PostRepository
import ru.netology.nework.repository.PostRepositoryImpl

@InstallIn(SingletonComponent::class)
@Module
interface PostRepositoryModule {
    @Binds
    fun bindPostRepository(impl: PostRepositoryImpl): PostRepository
}