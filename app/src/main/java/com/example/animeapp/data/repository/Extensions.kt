package com.example.animeapp.data.repository

import com.example.animeapp.data.local.entity.AnimeDetailEntity
import com.example.animeapp.data.local.entity.AnimeEntity
import com.example.animeapp.data.remote.dtoModels.AnimeDetailDto
import com.example.animeapp.data.remote.dtoModels.AnimeDto
import com.example.animeapp.ui.uiModels.Anime
import com.example.animeapp.ui.uiModels.AnimeDetail


fun AnimeDto.toEntity(indexOrder: Int): AnimeEntity {
    return AnimeEntity(
        id = mal_id,
        title = title,
        episodes = episodes,
        score = score,
        imageUrl = images.jpg.image_url,
        indexOrder = indexOrder,
        updatedAt = System.currentTimeMillis()
    )
}


fun AnimeEntity.toUi(): Anime {
    return Anime(
        id = id,
        title = title,
        episodes = episodes,
        score = score,
        imageUrl = imageUrl
    )
}

fun AnimeDetailDto.toEntity(): AnimeDetailEntity {

    val youtubeId = trailer?.youtube_id
        ?: extractYoutubeId(trailer?.embed_url)

    return AnimeDetailEntity(
        id = mal_id,
        title = title,
        synopsis = synopsis,
        episodes = episodes,
        score = score,
        imageUrl = images.jpg.image_url,
        trailerUrl = youtubeId,
        genres = genres.joinToString { it.name },
        cast = producers?.joinToString { it.name } ?: "",
        updatedAt = System.currentTimeMillis()
    )
}

fun AnimeDetailEntity.toUI(): AnimeDetail {
    return AnimeDetail(
        id = id,
        title = title,
        synopsis = synopsis,
        episodes = episodes,
        score = score,
        imageUrl = imageUrl,
        trailerUrl = trailerUrl,
        genres = genres,
        cast = cast
    )
}

fun extractYoutubeId(embedUrl: String?): String? {
    if (embedUrl.isNullOrBlank()) return null

    val regex = Regex("""/embed/([a-zA-Z0-9_-]+)""")
    return regex.find(embedUrl)?.groupValues?.getOrNull(1)
}
