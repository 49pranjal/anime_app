package com.example.animeapp.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.animeapp.data.local.entity.AnimeDetailEntity
import com.example.animeapp.data.local.entity.AnimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {

    //AnimeList Queries
    @Query("SELECT * FROM anime ORDER BY indexOrder ASC")
    fun observeTopAnime(): Flow<List<AnimeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<AnimeEntity>)

    @Query("DELETE FROM anime")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM anime")
    suspend fun getCount(): Int

    //AnimeDetail Queries
    @Query("SELECT * FROM anime_detail WHERE id = :id")
    suspend fun getAnimeDetail(id: Int): AnimeDetailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimeDetail(entity: AnimeDetailEntity)
}