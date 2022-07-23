package ru.netology.nework.module

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AuthModule {
    @Provides
    fun provideAuthPrefs(
        @ApplicationContext context: Context
    ): SharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
}