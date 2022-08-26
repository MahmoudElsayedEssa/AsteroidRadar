package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.network.AsteroidNetworkService
import com.udacity.asteroidradar.network.ImageOfDayNetworkService
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidRepository(private val database: AsteroidDatabase) {
    val asteroids: LiveData<List<Asteroid>> = database.dao.getAsteroids()

    lateinit var imageOfDay: PictureOfDay

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroids = AsteroidNetworkService.asteroids.getAsteroidsAsync().await()
            database.dao.insertAll(*asteroids.asDatabaseModel())
        }
    }


}