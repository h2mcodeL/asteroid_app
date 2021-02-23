package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object AsteroidApi {

interface AsteroidApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getResults(
            @Query("start_date") start_date: String,    //ArrayList<NetworkAsteroid>,
            @Query("end_date") end_date: String,
            @Query("api_key") api_key: String): String      //this is used to filter for the menu
}

interface PictureOfTheDay {
    @GET("planetary/apod")
    suspend fun getImage(
        @Query("api_key") api_key: String): NetworkImageOfTheDay
        // PictureOfDay
}

    private val retrofit = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))     //this is used for the image of the day
            .baseUrl(Constants.BASE_URL)
            .build()

    val asteroidApiService: AsteroidApiService = retrofit.create(AsteroidApiService::class.java)
    val imageApiService: PictureOfTheDay = retrofit.create(PictureOfTheDay::class.java)

}
