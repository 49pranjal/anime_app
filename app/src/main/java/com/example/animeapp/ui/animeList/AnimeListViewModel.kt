package com.example.animeapp.ui.animeList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.data.repository.AnimeRepo
import com.example.animeapp.ui.uiModels.Anime
import com.example.animeapp.ui.uiModels.UiState
import com.example.animeapp.utils.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeListViewModel @Inject constructor(
    private val repository: AnimeRepo,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<Anime>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Anime>>> = _uiState

    private var isFirstLoad = true

    init {
        observeAnime()
        observeNetwork()
        refresh()
    }

    private fun observeNetwork() {
        viewModelScope.launch {
            networkMonitor.isOnline.collect { isOnline ->

                if (isOnline) {
                    // 🔥 attempt refresh when internet returns
                    try {
                        repository.refreshTopAnime()
                        isFirstLoad = false
                    } catch (_: Exception) {
                        // ignore — refresh() already handles UX
                    }
                }
            }
        }
    }

    private fun observeAnime() {
        viewModelScope.launch {
            repository.observeTopAnime().collect { list ->
                // First Time Loading
                if (isFirstLoad) {
                    val hasCache = list.isNotEmpty()

                    if (hasCache) {
                        _uiState.value = UiState.Success(list)
                        isFirstLoad = false
                    }
                    // else → wait for refresh result
                } else {
                    // normal updates
                    _uiState.value = UiState.Success(list)
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {

            val hasCacheBefore = repository.hasCache()

            // 🔥 Show loader if no cache present
            if (!hasCacheBefore) {
                _uiState.value = UiState.Loading
            }
            try {
                repository.refreshTopAnime()
                isFirstLoad = false
            } catch (e: Exception) {
                val hasCacheAfter = repository.hasCache()

                // 🔥 If no cache then show error
                if (!hasCacheAfter) {
                    _uiState.value =
                        UiState.Error(e.message ?: "No internet and no cached data")
                }

                // show existing data
            }
        }
    }
}