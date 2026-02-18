package com.example.animeapp.ui.animeDetail

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.animeapp.R
import com.example.animeapp.databinding.FragmentAnimeDetailBinding
import com.example.animeapp.ui.uiModels.AnimeDetail
import com.example.animeapp.ui.uiModels.UiState
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.animeapp.MainActivity
import com.example.animeapp.utils.Constant
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

@AndroidEntryPoint
class AnimeDetailFragment : Fragment() {


    private var _binding: FragmentAnimeDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AnimeDetailViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(binding.trailerAnimeDetail)
        initObserver()

    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { state ->
                when (state) {

                    UiState.Loading -> {
                        binding.progressBarAnimeDetail.visibility = View.VISIBLE
                        binding.contentGroup.visibility = View.GONE
                        binding.tvErrorAnimeDetail.visibility = View.GONE
                    }

                    is UiState.Success -> {
                        binding.progressBarAnimeDetail.visibility = View.GONE
                        binding.contentGroup.visibility = View.VISIBLE
                        binding.tvErrorAnimeDetail.visibility = View.GONE
                        (requireActivity() as MainActivity)
                            .setToolbarTitle(state.data.title)

                        bindData(state.data)
                    }

                    is UiState.Error -> {
                        binding.progressBarAnimeDetail.visibility = View.GONE
                        binding.contentGroup.visibility = View.GONE
                        binding.tvErrorAnimeDetail.visibility = View.VISIBLE
                        binding.tvErrorAnimeDetail.text = state.message
                    }
                }
            }
        }
    }

    private fun bindData(data: AnimeDetail) {

        binding.tvTitleAnimeDetail.text = data.title
        binding.tvRatingAnimeDetail.text = data.score?.toString() ?: "-"
        "Episodes: ${data.episodes ?: "-"}".also { binding.tvEpisodesAnimeDetail.text = it }
        "Genres: ${data.genres}".also { binding.tvGenresAnimeDetail.text = it }
        "Cast: ${data.cast}".also { binding.tvCastAnimeDetail.text = it }
        binding.tvSynopsisAnimeDetail.text = data.synopsis ?: "-"

        // Trailer vs poster logic
        if (!data.trailerUrl.isNullOrBlank()) {
            binding.ivPosterAnimeDetail.visibility = View.GONE
            binding.trailerAnimeDetail.visibility = View.VISIBLE
            binding.btnYoutubeAnimeDetail.visibility = View.GONE

            // Code for running You Tube player
            binding.trailerAnimeDetail.addYouTubePlayerListener(
                object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        //youTubePlayer.loadVideo(data.trailerUrl, 0f)
                        youTubePlayer.loadVideo(data.trailerUrl, 0f)
                    }
                }
            )
        } else {
            binding.ivPosterAnimeDetail.visibility = View.VISIBLE
            binding.trailerAnimeDetail.visibility = View.GONE

            Glide.with(binding.ivPosterAnimeDetail)
                .load(data.imageUrl)
                .placeholder(R.drawable.placeholder_anime)
                .error(R.drawable.placeholder_anime)
                .into(binding.ivPosterAnimeDetail)

            if (!data.embedUrl.isNullOrBlank()) {
                binding.btnYoutubeAnimeDetail.visibility = View.VISIBLE

                binding.btnYoutubeAnimeDetail.setOnClickListener {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        (Constant.YOUTUBE_BASE_URL + data.embedUrl).toUri()
                    )
                    startActivity(intent)
                }
            } else {
                binding.btnYoutubeAnimeDetail.visibility = View.GONE
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}