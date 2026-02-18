package com.example.animeapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.animeapp.R
import com.example.animeapp.databinding.ItemAnimeBinding
import com.example.animeapp.ui.uiModels.Anime

class AnimeAdapter(
    private val onAnimeClick: (Int) -> Unit
) : ListAdapter<Anime, AnimeAdapter.AnimeViewHolder>(DiffCallBack) {

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<Anime>() {
            override fun areItemsTheSame(old: Anime, new: Anime) = old.id == new.id
            override fun areContentsTheSame(old: Anime, new: Anime) = old == new

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val binding = ItemAnimeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AnimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AnimeViewHolder(
        private val binding: ItemAnimeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(anime: Anime) {
            binding.tvAnimeTitle.text = anime.title
            "Episodes: ${anime.episodes ?: "-"}".also { binding.tvAnimeEpisodes.text = it }
            binding.tvAnimeRating.text = anime.score?.toString() ?: "-"

            Glide.with(binding.ivAnimePoster)
                .load(anime.imageUrl)
                .placeholder(R.drawable.placeholder_anime)
                .error(R.drawable.placeholder_anime)
                .into(binding.ivAnimePoster)

            binding.root.setOnClickListener {
                onAnimeClick(anime.id)
            }
        }
    }
}
