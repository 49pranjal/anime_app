package com.example.animeapp.ui.animeDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeapp.data.repository.AnimeRepo
import com.example.animeapp.ui.uiModels.AnimeDetail
import com.example.animeapp.ui.uiModels.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeDetailViewModel @Inject constructor(
    private val repository: AnimeRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val animeId: Int =
        checkNotNull(savedStateHandle["animeId"])

    private val _uiState =
        MutableStateFlow<UiState<AnimeDetail>>(UiState.Loading)
    val uiState: StateFlow<UiState<AnimeDetail>> = _uiState

    init {
        loadDetail()
    }

    private fun loadDetail() {
        viewModelScope.launch {

            _uiState.value = UiState.Loading

            try {
                val data = repository.fetchAnimeDetail(animeId)
                _uiState.value = UiState.Success(data)

            } catch (e: Exception) {
                _uiState.value =
                    UiState.Error(e.message ?: "Failed to load details")
            }
        }
    }
}