package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
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

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_overflow_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                asteroidViewModel.onChangeFilter(
                    when (menuItem.itemId) {
                        R.id.show_rent_menu -> {
                            FilterAsteroid.TODAY
                        }
                        R.id.show_all_menu -> {
                            FilterAsteroid.WEEK
                        }
                        else -> {
                            FilterAsteroid.ALL
                        }
                    }
                )
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding.root
    }
}