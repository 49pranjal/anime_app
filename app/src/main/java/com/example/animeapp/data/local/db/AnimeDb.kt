package com.example.animeapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.animeapp.data.local.entity.AnimeDetailEntity
import com.example.animeapp.data.local.entity.AnimeEntity

@Database(
    entities = [AnimeEntity::class, AnimeDetailEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AnimeDb : RoomDatabase() {

    abstract fun getAnimeDao(): AnimeDao
}