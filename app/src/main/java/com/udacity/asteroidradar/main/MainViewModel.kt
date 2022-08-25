package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid

class MainViewModel : ViewModel() {
    private val _asteroids = MutableLiveData<MutableList<Asteroid>>()
    val asteroids: LiveData<MutableList<Asteroid>> get() = _asteroids

    private val _navigateToDetailAsteroid = MutableLiveData<Asteroid?>()
    val navigateToDetailAsteroid: MutableLiveData<Asteroid?> get() = _navigateToDetailAsteroid
    
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
    }

    fun onAsteroidNavigated() {
        _navigateToDetailAsteroid.value = null
    }

    fun onAsteroidClicked(asteroid:Asteroid){
        _navigateToDetailAsteroid.value = asteroid
    }
}