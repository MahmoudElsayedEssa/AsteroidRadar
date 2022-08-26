package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.network.AsteroidNetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AsteroidRepository(private val database: AsteroidDatabase) {

    private val startDate = LocalDateTime.now()

    private val endDate = LocalDateTime.now().minusDays(7)

    val allAsteroids: LiveData<List<Asteroid>> = database.dao.getAsteroids()

    val todayAsteroids: LiveData<List<Asteroid>> =
        database.dao.getAsteroidsDay(startDate.format(DateTimeFormatter.ISO_DATE))

    val weekAsteroids: LiveData<List<Asteroid>> = database.dao.getAsteroidsDate(
        startDate.format(DateTimeFormatter.ISO_DATE),
        endDate.format(DateTimeFormatter.ISO_DATE)
    )


    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val asteroids = AsteroidNetworkService.retrofitService.getAsteroids(API_KEY)
                val result = parseAsteroidsJsonResult(JSONObject(asteroids))
                Log.i("TAG", "refreshAsteroids: $result")
                database.dao.insertAll(*result.asDatabaseModel())
                Log.d("Refresh Asteroids", "Success")
            } catch (err: Exception) {
                Log.e("Failed: AsteroidRepFile", err.message.toString())
            }
        }
    }


}