package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = requireActivity()
            viewModel = viewModel
        }


        val adapter = AsteroidAdapter(AsteroidAdapterListener {})

        binding.asteroidRecycler.adapter = adapter

        viewModel.asteroids.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }


        return binding.root
    }

}