package ru.netology.nework.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.netology.nework.dao.*
import ru.netology.nework.db.AppDb
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DbModule {
    @Provides
    @Singleton
    fun provideDb(
        @ApplicationContext context: Context
    ): AppDb = Room.databaseBuilder(context, AppDb::class.java, "app.db")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun providePostDao(appDb: AppDb): PostDao = appDb.postDao()

    @Provides
    fun providePostKeyDao(appDb: AppDb): PostRemoteKeyDao = appDb.postRemoteKeyDao()

    @Provides
    fun provideUserDao(appDb: AppDb): UserDao = appDb.userDao()

    @Provides
    fun provideJobDao(appDb: AppDb): JobDao = appDb.jobDao()

    @Provides
    fun provideEventDao(appDb: AppDb): EventDao = appDb.eventDao()

    @Provides
    fun providesEventKeyDao(appDb: AppDb): EventRemoteKeyDao = appDb.eventRemoteKeyDao()
}