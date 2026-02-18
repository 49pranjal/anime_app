package com.example.animeapp.ui.uiModels

data class AnimeDetail(
    val id: Int,
    val title: String,
    val synopsis: String?,
    val episodes: Int?,
    val score: Double?,
    val imageUrl: String?,
    val trailerUrl: String?,
    val genres: String,
    val cast: String
)
