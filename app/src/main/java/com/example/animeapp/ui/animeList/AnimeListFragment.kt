package com.example.animeapp.ui.animeList

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.animeapp.R
import com.example.animeapp.databinding.FragmentAnimeListBinding
import com.example.animeapp.ui.adapters.AnimeAdapter
import com.example.animeapp.ui.uiModels.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnimeListFragment : Fragment() {

    private var _binding: FragmentAnimeListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AnimeListViewModel by viewModels()
    private lateinit var adapter: AnimeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialiseRecyclerView()
        initObserver()
    }

    private fun initialiseRecyclerView() {
        adapter = AnimeAdapter(
            ::openAnimeDetailsPage
        )
        binding.recyclerViewAnimeList.adapter = adapter
        binding.recyclerViewAnimeList.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { state ->
                when (state) {

                    UiState.Loading -> {
                        binding.progressBarAnimeList.visibility = View.VISIBLE
                        binding.recyclerViewAnimeList.visibility = View.GONE
                        binding.tvErrorAnimeList.visibility = View.GONE
                    }

                    is UiState.Success -> {
                        binding.progressBarAnimeList.visibility = View.GONE

                        if (state.data.isEmpty()) {
                            binding.recyclerViewAnimeList.visibility = View.GONE
                            binding.tvErrorAnimeList.visibility = View.VISIBLE
                            binding.tvErrorAnimeList.text = getString(R.string.no_anime_available)
                        } else {
                            binding.recyclerViewAnimeList.visibility = View.VISIBLE
                            binding.tvErrorAnimeList.visibility = View.GONE
                            adapter.submitList(state.data)
                        }
                    }

                    is UiState.Error -> {
                        binding.progressBarAnimeList.visibility = View.GONE
                        binding.recyclerViewAnimeList.visibility = View.GONE
                        binding.tvErrorAnimeList.visibility = View.VISIBLE
                        binding.tvErrorAnimeList.text = state.message
                    }
                }
            }
        }
    }

    private fun openAnimeDetailsPage(animeId: Int) {
        findNavController().navigate(R.id.animeDetailFragment, Bundle().apply {
            putInt(getString(R.string.animeid_argKey), animeId)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}