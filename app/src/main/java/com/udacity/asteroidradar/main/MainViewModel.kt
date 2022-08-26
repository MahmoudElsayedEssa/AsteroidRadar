package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.network.AsteroidNetworkService
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import org.json.JSONObject

enum class ImageApiStatus { LOADING, ERROR, DONE }
enum class FilterAsteroid { TODAY, WEEK, ALL }

class MainViewModel(application: Application) : AndroidViewModel(application) {

//    val asteroids = MutableLiveData<List<Asteroid>>()

    private val _imageOfDay = MutableLiveData<PictureOfDay?>()
    val imageOfDay: MutableLiveData<PictureOfDay?> get() = _imageOfDay

    private val _navigateToDetailAsteroid = MutableLiveData<Asteroid?>()
    val navigateToDetailAsteroid: MutableLiveData<Asteroid?> get() = _navigateToDetailAsteroid

    private val _imageState = MutableLiveData<ImageApiStatus>()

    val imageState: LiveData<ImageApiStatus> get() = _imageState

    private var _filterAsteroid = MutableLiveData(FilterAsteroid.ALL)


    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    var asteroidList = Transformations.switchMap(_filterAsteroid) {
        when (it!!) {
            FilterAsteroid.WEEK -> asteroidRepository.weekAsteroids
            FilterAsteroid.TODAY -> asteroidRepository.todayAsteroids
            else -> asteroidRepository.allAsteroids
        }
    }

    init {
//        _asteroids.value = Transformations.switchMap(_filterAsteroid) { filter ->
//            when (filter) {
//                FilterAsteroid.WEEK -> asteroidRepository.weekAsteroids.value
//                FilterAsteroid.TODAY -> asteroidRepository.todayAsteroids.value
//                else -> asteroidRepository.allAsteroids.value
//
//            }
//        }

//        _asteroids.value = when (_filterAsteroid.value) {
//            FilterAsteroid.WEEK -> asteroidRepository.weekAsteroids.value
//            FilterAsteroid.TODAY -> asteroidRepository.todayAsteroids.value
//            else -> asteroidRepository.allAsteroids.value
//        }


            initializeImage()
    }

    fun onChangeFilter(filter: FilterAsteroid) {
        _filterAsteroid.value = filter
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
                    AsteroidNetworkService.retrofitService.getImageDay(API_KEY)
                _imageState.value = ImageApiStatus.DONE
//                asteroidRepository.refreshAsteroids()
                var asteroidss = AsteroidNetworkService.retrofitService.getAsteroids(API_KEY)
                Log.i("TAG", "the json: $asteroidss")
                val result = parseAsteroidsJsonResult(JSONObject(asteroidss))
                asteroidList = asteroidRepository.allAsteroids
            } catch (e: Exception) {
                _imageState.value = ImageApiStatus.ERROR
                _imageOfDay.value = null
            }
        }
    }
}