package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.network.ImageOfDayNetworkService
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

enum class ImageApiStatus { LOADING, ERROR, DONE }

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _asteroids = MutableLiveData<MutableList<Asteroid>>()
    val asteroids: LiveData<MutableList<Asteroid>> get() = _asteroids

    private val _imageOfDay = MutableLiveData<PictureOfDay?>()
    val imageOfDay: MutableLiveData<PictureOfDay?> get() = _imageOfDay

    private val _navigateToDetailAsteroid = MutableLiveData<Asteroid?>()
    val navigateToDetailAsteroid: MutableLiveData<Asteroid?> get() = _navigateToDetailAsteroid

    private val _imageState = MutableLiveData<ImageApiStatus>()
    val imageState: LiveData<ImageApiStatus> get() = _imageState


//    private val database = getDatabase(application)
//    private val asteroidRepository = AsteroidRepository(database)


    init {
        _asteroids.value = mutableListOf(
            Asteroid(
                id = 123,
                closeApproachDate = "2022/8/8",
                isPotentiallyHazardous = false,
                codename = "mahmoud1",
                absoluteMagnitude = 2.6,
                estimatedDiameter = 1.6,
                distanceFromEarth = 4.9,
                relativeVelocity = 8.9
            ),
            Asteroid(
                id = 123,
                closeApproachDate = "555/5/8",
                isPotentiallyHazardous = true,
                codename = "mahmoud12",
                absoluteMagnitude = 2.6,
                estimatedDiameter = 1.6,
                distanceFromEarth = 4.9,
                relativeVelocity = 8.9
            )
        )
        initializeImage()

    }


    fun onAsteroidNavigated() {
        _navigateToDetailAsteroid.value = null
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetailAsteroid.value = asteroid
    }

    private fun initializeImage() {
        viewModelScope.launch {
            _imageState.value = ImageApiStatus.LOADING

            try {
                _imageOfDay.value =
                    ImageOfDayNetworkService.imageOfDayService.getImageAsync().await().let {
                        PictureOfDay(
                            mediaType = it.media_type,
                            title = it.title,
                            url = it.url
                        )
                    }
                _imageState.value = ImageApiStatus.DONE
            } catch (e: Exception) {
                _imageState.value = ImageApiStatus.ERROR
                _imageOfDay.value = null
            }
        }
    }
}