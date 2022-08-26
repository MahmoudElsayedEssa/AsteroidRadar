package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val factory = MainViewModelFactory(requireNotNull(this.activity).application)
        val asteroidViewModel: MainViewModel =
            ViewModelProvider(this, factory)[MainViewModel::class.java]

        val binding = FragmentMainBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = requireActivity()
            viewModel = asteroidViewModel
        }


        val adapter = AsteroidAdapter(AsteroidAdapterListener {
            asteroidViewModel.onAsteroidClicked(it)
        })

        binding.asteroidRecycler.adapter = adapter

        asteroidViewModel.asteroidList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        asteroidViewModel.navigateToDetailAsteroid.observe(viewLifecycleOwner) { asteroid ->
            asteroid?.let {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                asteroidViewModel.onAsteroidNavigated()
            }

        }

        val picasso = Picasso.get()
        asteroidViewModel.imageOfDay.observe(viewLifecycleOwner) { pic ->
            if (pic != null) {
                picasso.load(pic.url).into(binding.activityMainImageOfTheDay)
                binding.activityMainImageOfTheDay.contentDescription = pic.title
            }
        }

        return binding.root
    }
}