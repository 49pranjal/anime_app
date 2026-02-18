package com.example.animeapp.data.repository

import android.content.Context
import com.example.animeapp.R
import com.example.animeapp.data.local.db.AnimeDao
import com.example.animeapp.data.remote.JikanApiService
import com.example.animeapp.ui.uiModels.Anime
import com.example.animeapp.ui.uiModels.AnimeDetail
import com.example.animeapp.utils.NetworkMonitor
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeRepo @Inject constructor(
    private val api: JikanApiService,
    private val dao: AnimeDao,
    private val networkMonitor: NetworkMonitor,
    @ApplicationContext private val context: Context
) {
    // AnimeList Page Repo Functions
    fun observeTopAnime(): Flow<List<Anime>> {
        return dao.observeTopAnime()
            .map { list -> list.map { it.toUi() } }
    }

    suspend fun refreshTopAnime() {
        val isOnline = networkMonitor.isOnline.first()
        if (!isOnline) {
            throw IOException(context.getString(R.string.no_internet_connection))
        }

        try {
            val response = api.getTopAnime()

            val entities = response.data.mapIndexed { index, dto ->
                dto.toEntity(index)
            }

            //clear all data before inserting data -- Not implemented paging yet
            dao.clearAll()
            dao.insertAll(entities)

        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun hasCache(): Boolean {
        return dao.getCount() > 0
    }

    //Anime Detail Repo Functions
    suspend fun fetchAnimeDetail(id: Int): AnimeDetail {
        try {
            val isOnline = networkMonitor.isOnline.first()

            val cached = dao.getAnimeDetail(id)

            //  Offline fallback
            if (!isOnline) {
                return cached?.toUI()
                    ?: throw IOException(context.getString(R.string.no_internet_connection))
            }

            // Online → fetch fresh → sync in db
            val response = api.getAnimeDetail(id)
            val entity = response.data.toEntity()

            dao.insertAnimeDetail(entity)

            return entity.toUI()

        } catch (e: Exception) {
            // Final fallback to cache
            val cached = dao.getAnimeDetail(id)
            if (cached != null) return cached.toUI()

            throw e
        }
    }

}