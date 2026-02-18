package com.example.animeapp.data.remote

import com.example.animeapp.data.remote.dtoModels.AnimeDetailResponse
import com.example.animeapp.data.remote.dtoModels.TopAnimeResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface JikanApiService {

    @GET("v4/top/anime")
    suspend fun getTopAnime(): TopAnimeResponse


    @GET("v4/anime/{anime_id}")
    suspend fun getAnimeDetail(@Path("anime_id") animeId: Int): AnimeDetailResponse
}