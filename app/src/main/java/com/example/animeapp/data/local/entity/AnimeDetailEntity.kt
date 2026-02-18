package com.example.animeapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime_detail")
data class AnimeDetailEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val synopsis: String?,
    val episodes: Int?,
    val score: Double?,
    val imageUrl: String?,
    val trailerUrl: String?,
    val embedUrl: String?,
    val genres: String,
    val cast: String,
    val updatedAt: Long
)