package com.example.animeapp.data.remote.dtoModels

data class AnimeDto (
    val mal_id: Int,
    val title: String,
    val episodes: Int?,
    val score: Double?,
    val images: AnimeImagesDto
)