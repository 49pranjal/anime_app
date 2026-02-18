package com.example.animeapp.ui.uiModels

data class Anime(
    val id: Int,
    val title: String,
    val episodes: Int?,
    val score: Double?,
    val imageUrl: String?
)
