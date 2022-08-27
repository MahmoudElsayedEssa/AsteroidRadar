package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.BuildConfig.NASA_API_KEY
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.network.AsteroidNetworkService
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        viewModelScope.launch {

            try {
                asteroidRepository.refreshAsteroids()
                initializeImage()
            } catch (e: Exception) {
                throw e
            }
        }
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

    private suspend fun initializeImage() {
        withContext(Dispatchers.IO) {
            _imageState.postValue(ImageApiStatus.LOADING)

            try {
                _imageOfDay.postValue(
                    AsteroidNetworkService.retrofitService.getImageDay(
                        NASA_API_KEY
                    )
                )

                _imageState.postValue(ImageApiStatus.DONE)
            } catch (e: Exception) {
                _imageState.postValue(ImageApiStatus.ERROR)
                _imageOfDay.postValue(null)
            }
        }
    }
}