package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MainFragment : Fragment() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//        delayedInit()

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

        asteroidViewModel.asteroids.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        asteroidViewModel.navigateToDetailAsteroid.observe(viewLifecycleOwner) { asteroid ->
            asteroid?.let {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                asteroidViewModel.onAsteroidNavigated()
            }

        }

        val picasso = Picasso.get()
        asteroidViewModel.imageOfDay.observe(viewLifecycleOwner) {
            if (it != null) {
                picasso.load(it.url).into(binding.activityMainImageOfTheDay)
                binding.activityMainImageOfTheDay.contentDescription = it.title
            }
        }

        return binding.root
    }

//    private fun delayedInit() = applicationScope.launch {
//        setupRecurringWork()
//    }
//
//    private fun setupRecurringWork() {
//
//        val constraints = Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.UNMETERED)
//            .setRequiresBatteryNotLow(true)
//            .setRequiresCharging(true)
//            .apply {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    setRequiresDeviceIdle(true)
//                }
//            }.build()
//
//
//        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWork>(
//            1,
//            TimeUnit.DAYS
//        )
//            .setConstraints(constraints)
//            .build()
//
//        WorkManager.getInstance().enqueueUniquePeriodicWork(
//            RefreshDataWork.WORK_NAME,
//            ExistingPeriodicWorkPolicy.KEEP,
//            repeatingRequest
//        )
//    }


}
