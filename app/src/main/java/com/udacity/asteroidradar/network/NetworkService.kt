package com.udacity.asteroidradar.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.Constants.IMAGE_GET
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


interface AsteroidService {
    @GET
    fun getAsteroidsAsync(): Deferred<NetworkAsteroidContainer>
}

interface ImageOfDayService {
    @GET(IMAGE_GET)
    fun getImageAsync(): Deferred<GetImageOfDayResponse>
}


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object AsteroidNetworkService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://devbytes.udacity.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val asteroids: AsteroidService = retrofit.create(AsteroidService::class.java)
}

object ImageOfDayNetworkService {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val imageOfDayService: ImageOfDayService = retrofit.create(ImageOfDayService::class.java)
}


