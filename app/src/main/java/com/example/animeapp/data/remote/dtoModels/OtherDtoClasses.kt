package com.example.animeapp.data.remote.dtoModels

import com.google.gson.annotations.SerializedName

data class AnimeImagesDto(
    val jpg: AnimeJpgDto
)

data class AnimeJpgDto(
    val image_url: String?
)

data class AnimeTrailerDto(
    @SerializedName("embed_url")
    val embed_url: String?,
    val youtube_id: String?
)

data class GenreDto(
    val name: String
)

data class ProducerDto(
    val name: String
)