package com.example.animeapp.data.remote.dtoModels

data class AnimeDetailDto(
    val mal_id: Int,
    val title: String,
    val synopsis: String?,
    val episodes: Int?,
    val score: Double?,
    val images: AnimeImagesDto,
    val trailer: AnimeTrailerDto?,
    val genres: List<GenreDto>,
    val producers: List<ProducerDto>?
)
