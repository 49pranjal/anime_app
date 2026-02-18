package com.example.animeapp.di

import android.content.Context
import androidx.room.Room
import com.example.animeapp.data.local.db.AnimeDao
import com.example.animeapp.data.local.db.AnimeDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AnimeDb {
        return Room.databaseBuilder(
            context,
            AnimeDb::class.java,
            "anime_db"
        ).build()
    }

    @Provides
    fun provideAnimeDao(db: AnimeDb): AnimeDao {
        return db.getAnimeDao()
    }
}